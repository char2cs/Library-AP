package com.Libreria.Imp.MySQL;

import com.Libreria.DAO.EditorialDAO;
import com.Libreria.Domain.Editorial;
import com.Libreria.Domain.Libro;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Utils.MySQLconn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditorialMySQL implements EditorialDAO {
    private Connection connection;

    public EditorialMySQL() {
        try {
            this.connection = MySQLconn.getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Editorial editorial) throws ObjectAlreadyExistsException {
        String query = "INSERT INTO Editorial (nombre, direccion, telefono, url, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, editorial.getNombre());
            stmt.setString(2, editorial.getDireccion());
            stmt.setInt(3, editorial.getTelefono());
            stmt.setString(4, editorial.getUrl());
            stmt.setString(5, editorial.getEmail());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectAlreadyExistsException("No se pudo crear la editorial: " + editorial.getNombre());

            try (ResultSet generatedKeys = stmt.getGeneratedKeys())
            {
                if (generatedKeys.next())
                    editorial.setId(generatedKeys.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al crear la editorial: " + e.getMessage());
        }
    }

    @Override
    public Editorial get(Integer id) throws ObjectNotFoundException {
        String query = "SELECT id, nombre, direccion, telefono, url, emial FROM Editorial WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    Editorial editorial = new Editorial();
                    editorial.setId(rs.getInt("id"));
                    editorial.setNombre(rs.getString("nombre"));
                    editorial.setDireccion(rs.getString("direccion"));
                    editorial.setTelefono(rs.getInt("telefono"));
                    editorial.setUrl(rs.getString("url"));
                    editorial.setEmail(rs.getString("email"));

                    editorial.setLibro(new ArrayList<>());

                    return editorial;
                }
                else
                    throw new ObjectNotFoundException("Editorial con id " + id + " no encontrada.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener la editorial: " + e.getMessage());
        }
    }

    @Override
    public boolean update(Editorial editorial) throws ObjectNotFoundException, OperationFailedException {
        String query = "UPDATE Editorial SET nombre = ?, direccion = ?, telefono = ?, url = ?, email = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, editorial.getNombre());
            stmt.setString(2, editorial.getDireccion());
            stmt.setInt(3, editorial.getTelefono());
            stmt.setString(4, editorial.getUrl());
            stmt.setString(5, editorial.getEmail());
            stmt.setInt(6, editorial.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("Editorial con id " + editorial.getId() + " no encontrada.");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al actualizar la editorial: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        String query = "DELETE FROM Editorial WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("Editorial con id " + id + " no encontrada.");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al eliminar la editorial: " + e.getMessage());
        }
    }

    @Override
    public List<Editorial> getAll() {
        String query = "SELECT id, nombre, direccion, telefono, url, email FROM Editorial";
        List<Editorial> editoriales = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            while (rs.next())
            {
                Editorial editorial = new Editorial();
                editorial.setId(rs.getInt("id"));
                editorial.setNombre(rs.getString("nombre"));
                editorial.setDireccion(rs.getString("direccion"));
                editorial.setTelefono(rs.getInt("telefono"));
                editorial.setUrl(rs.getString("url"));
                editorial.setEmail(rs.getString("email"));

                editorial.setLibro(new ArrayList<>());

                editoriales.add(editorial);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las editoriales: " + e.getMessage());
        }

        return editoriales;
    }

    /**
     * ... TRANSACTION ...
     */

    @Override
    public void updateEditorialLibro(Editorial editorial) throws ObjectNotFoundException, OperationFailedException {
        String deleteQuery = "DELETE FROM Editorial_Libro WHERE editorial_id = ?";
        String insertQuery = "INSERT INTO Editorial_Libro (editorial_id, libro_id) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement deletePS = connection.prepareStatement(deleteQuery))
            {
                deletePS.setInt(1, editorial.getId());
                int affectedRows = deletePS.executeUpdate();

                if (affectedRows == 0)
                    throw new ObjectNotFoundException("No se encontraron libros relacionados con la editorial con id: " + editorial.getId());
            }

            try (PreparedStatement insertPS = connection.prepareStatement(insertQuery))
            {
                for (Libro libro : editorial.getLibro())
                {
                    insertPS.setInt(1, editorial.getId());
                    insertPS.setInt(2, libro.getId());
                    insertPS.addBatch();
                }
                insertPS.executeBatch();
            }

            connection.commit();
        }
        catch (SQLException e) {
            try {
                connection.rollback();
            }
            catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            throw new OperationFailedException("Error al actualizar la relación editorial-libro.");
        }
        finally {
            try {
                connection.setAutoCommit(true);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Editorial obtenerEditorialPorInstancia(Integer idInstancia) throws ObjectNotFoundException {
        String query = "SELECT e.id, e.nombre, e.direccion, e.telefono, e.url, e.email " +
                "FROM libreria.Instancia i " +
                "JOIN libreria.Editorial_Libro el ON i.editorial_libro_id = el.id " +
                "JOIN libreria.Editorial e ON el.editorial_id = e.id " +
                "WHERE i.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, idInstancia);
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    Editorial editorial = new Editorial();
                    editorial.setId(rs.getInt("id"));
                    editorial.setNombre(rs.getString("nombre"));
                    editorial.setDireccion(rs.getString("direccion"));
                    editorial.setTelefono(rs.getInt("telefono"));
                    editorial.setUrl(rs.getString("url"));
                    editorial.setEmail(rs.getString("email"));

                    return editorial;
                }
                else
                    throw new ObjectNotFoundException("Editorial no encontrada para la instancia con ID: " + idInstancia);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Editorial> obtenerEditorialPorLibro(Integer idLibro) {
        String query = "SELECT e.id, e.nombre, e.direccion, e.telefono, e.url, e.email " +
                "FROM Editorial e " +
                "JOIN Editorial_Libro el ON e.id = el.editorial_id " +
                "WHERE el.libro_id = ?";

        List<Editorial> editoriales = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query))
        {
            ps.setInt(1, idLibro);

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    Editorial editorial = new Editorial();
                    editorial.setId(rs.getInt("id"));
                    editorial.setNombre(rs.getString("nombre"));
                    editorial.setDireccion(rs.getString("direccion"));
                    editorial.setTelefono(rs.getInt("telefono"));
                    editorial.setUrl(rs.getString("url"));
                    editorial.setEmail(rs.getString("email"));

                    editorial.setLibro(new ArrayList<>());

                    editoriales.add(editorial);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return editoriales;
    }

}
