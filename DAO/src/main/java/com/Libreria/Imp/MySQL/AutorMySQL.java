package com.Libreria.Imp.MySQL;

import com.Libreria.DAO.AutorDAO;
import com.Libreria.Domain.Autor;
import com.Libreria.Domain.Libro;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Utils.MySQLconn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorMySQL implements AutorDAO {
    private final Connection connection;

    public AutorMySQL() {
        try {
            this.connection = MySQLconn.getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Autor autor) throws ObjectAlreadyExistsException {
        String query = "INSERT INTO Autor (nombre, apellido, nacionalidad) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getApellido());
            stmt.setString(3, autor.getNacionalidad());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectAlreadyExistsException("No se pudo crear el autor: " + autor.getNombre() + " " + autor.getApellido());

            try (ResultSet generatedKeys = stmt.getGeneratedKeys())
            {
                if (generatedKeys.next())
                    autor.setId(generatedKeys.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al crear el autor: " + e.getMessage());
        }
    }

    @Override
    public Autor get(Integer id) throws ObjectNotFoundException {
        String query = "SELECT id, nombre, apellido, nacionalidad FROM Autor WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    Autor autor = new Autor();
                    autor.setId(rs.getInt("id"));
                    autor.setNombre(rs.getString("nombre"));
                    autor.setApellido(rs.getString("apellido"));
                    autor.setNacionalidad(rs.getString("nacionalidad"));

                    return autor;
                }
                else
                    throw new ObjectNotFoundException("Autor con id " + id + " no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener el autor: " + e.getMessage());
        }
    }

    @Override
    public boolean update(Autor autor) throws ObjectNotFoundException, OperationFailedException {
        String query = "UPDATE Autor SET nombre = ?, apellido = ?, nacionalidad = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getApellido());
            stmt.setString(3, autor.getNacionalidad());
            stmt.setInt(4, autor.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("Autor con id " + autor.getId() + " no encontrado.");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al actualizar el autor: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        String query = "DELETE FROM Autor WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("Autor con id " + id + " no encontrado.");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al eliminar el autor: " + e.getMessage());
        }
    }

    @Override
    public List<Autor> getAll() {
        String query = "SELECT id, nombre, apellido, nacionalidad FROM Autor";
        List<Autor> autores = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            while (rs.next())
            {
                Autor autor = new Autor();
                autor.setId(rs.getInt("id"));
                autor.setNombre(rs.getString("nombre"));
                autor.setApellido(rs.getString("apellido"));
                autor.setNacionalidad(rs.getString("nacionalidad"));

                autores.add(autor);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los autores: " + e.getMessage());
        }

        return autores;
    }

    @Override
    public void updateAutorLibro(Autor autor) throws ObjectNotFoundException, OperationFailedException {
        String deleteQuery = "DELETE FROM Autor_Libro WHERE autor_id = ?";
        String insertQuery = "INSERT INTO Autor_Libro (autor_id, libro_id) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement deletePS = connection.prepareStatement(deleteQuery))
            {
                deletePS.setInt(1, autor.getId());
                int affectedRows = deletePS.executeUpdate();

                if (affectedRows == 0)
                    throw new ObjectNotFoundException("No se encontraron libros relacionados con el autor con id: " + autor.getId());
            }

            try (PreparedStatement insertPS = connection.prepareStatement(insertQuery)) {
                for (Libro libro : autor.getLibros())
                {
                    insertPS.setInt(1, autor.getId());
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

            throw new OperationFailedException("Error al actualizar la relación autor-libro.");
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
    public List<Autor> obtenerAutoresPorLibro(Integer idLibro) {
        String query = "SELECT a.id, a.nombre, a.apellido, a.nacionalidad " +
                "FROM Autor a " +
                "JOIN Autor_Libro al ON a.id = al.autor_id " +
                "WHERE al.libro_id = ?";

        List<Autor> autores = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query))
        {
            ps.setInt(1, idLibro);

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    Autor autor = new Autor();
                    autor.setId(rs.getInt("id"));
                    autor.setNombre(rs.getString("nombre"));
                    autor.setApellido(rs.getString("apellido"));
                    autor.setNacionalidad(rs.getString("nacionalidad"));
                    autores.add(autor);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return autores;
    }

    @Override
    public List<Autor> getAutoresByNames(String name) {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM Autor WHERE nombre LIKE ? OR apellido LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");
            statement.setString(2, "%" + name + "%");

            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next())
                {
                    Autor autor = new Autor();
                    autor.setId(resultSet.getInt("id"));
                    autor.setNombre(resultSet.getString("nombre"));
                    autor.setApellido(resultSet.getString("apellido"));
                    autor.setNacionalidad(resultSet.getString("nacionalidad"));

                    autores.add(autor);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return autores;
    }
}
