package com.Libreria.Imp.MySQL;

import com.Libreria.DAO.LibroDAO;
import com.Libreria.Domain.Genero;
import com.Libreria.Domain.Libro;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Utils.MySQLconn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroMySQL implements LibroDAO {
    private Connection connection;

    public LibroMySQL() {
        try {
            this.connection = MySQLconn.getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Libro libro) throws ObjectAlreadyExistsException {
        String query = "INSERT INTO Libro (titulo, edicion, paginas, lsbn, genero) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getEdicion());
            stmt.setInt(3, libro.getPaginas());
            stmt.setInt(4, libro.getLsbn());
            stmt.setString(5, libro.getGenero().toString());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectAlreadyExistsException("No se pudo crear el libro.");

            try (ResultSet generatedKeys = stmt.getGeneratedKeys())
            {
                if (generatedKeys.next())
                    libro.setId(generatedKeys.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al crear el libro: " + e.getMessage());
        }
    }

    @Override
    public Libro get(Integer id) throws ObjectNotFoundException {
        String query = "SELECT id, titulo, edicion, paginas, lsbn, genero FROM Libro WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setEdicion(rs.getString("edicion"));
                    libro.setPaginas(rs.getInt("paginas"));
                    libro.setLsbn(rs.getInt("lsbn"));
                    libro.setGenero(Genero.valueOf(rs.getString("genero")));

                    return libro;
                }
                else
                    throw new ObjectNotFoundException("Libro con id " + id + " no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener el libro: " + e.getMessage());
        }
    }

    @Override
    public boolean update(Libro libro) throws ObjectNotFoundException, OperationFailedException {
        String query = "UPDATE Libro SET titulo = ?, edicion = ?, paginas = ?, lsbn = ?, genero = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getEdicion());
            stmt.setInt(3, libro.getPaginas());
            stmt.setInt(4, libro.getLsbn());
            stmt.setString(5, libro.getGenero().toString());
            stmt.setInt(6, libro.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("Libro con id " + libro.getId() + " no encontrado.");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al actualizar el libro: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        String query = "DELETE FROM Libro WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("Libro con id " + id + " no encontrado.");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al eliminar el libro: " + e.getMessage());
        }
    }

    @Override
    public List<Libro> getAll() {
        String query = "SELECT id, titulo, edicion, paginas, lsbn, genero FROM Libro";
        List<Libro> libros = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {
            while (rs.next())
            {
                Libro libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setEdicion(rs.getString("edicion"));
                libro.setPaginas(rs.getInt("paginas"));
                libro.setLsbn(rs.getInt("lsbn"));
                libro.setGenero(Genero.valueOf(rs.getString("genero")));

                libros.add(libro);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los libros: " + e.getMessage());
        }

        return libros;
    }

    @Override
    public List<Libro> obtenerLibrosPorAutor(Integer idAutor) {
        String query = "SELECT l.id, l.titulo, l.edicion, l.paginas, l.lsbn, l.genero " +
                "FROM Libro l " +
                "JOIN Autor_Libro al ON l.id = al.libro_id " +
                "WHERE al.autor_id = ?";
        List<Libro> libros = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, idAutor);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setEdicion(rs.getString("edicion"));
                    libro.setPaginas(rs.getInt("paginas"));
                    libro.setLsbn(rs.getInt("lsbn"));
                    libro.setGenero(Genero.valueOf(rs.getString("genero")));

                    libros.add(libro);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener los libros por autor: " + e.getMessage());
        }

        return libros;
    }

    @Override
    public List<Libro> obtenerLibrosPorEditorial(Integer idEditorial) {
        String query = "SELECT l.id, l.titulo, l.edicion, l.paginas, l.lsbn, l.genero " +
                "FROM Libro l " +
                "JOIN Editorial_Libro el ON l.id = el.libro_id " +
                "WHERE el.editorial_id = ?";
        List<Libro> libros = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, idEditorial);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setEdicion(rs.getString("edicion"));
                    libro.setPaginas(rs.getInt("paginas"));
                    libro.setLsbn(rs.getInt("lsbn"));
                    libro.setGenero(Genero.valueOf(rs.getString("genero")));

                    libros.add(libro);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener los libros por editorial: " + e.getMessage());
        }

        return libros;
    }

    @Override
    public Libro obtenerLibroPorInstancia(Integer idInstancia) {
        String query = "SELECT l.id, l.titulo, l.edicion, l.paginas, l.lsbn, l.genero " +
                "FROM libreria.Instancia i " +
                "JOIN libreria.Editorial_Libro el ON i.editorial_libro_id = el.id " +
                "JOIN libreria.Libro l ON el.libro_id = l.id " +
                "WHERE i.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, idInstancia);
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setEdicion(rs.getString("edicion"));
                    libro.setPaginas(rs.getInt("paginas"));
                    libro.setLsbn(rs.getInt("lsbn"));
                    libro.setGenero(Genero.valueOf(rs.getString("genero")));

                    return libro;
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Libro> searchLibros(String searchKey) {
        String query = "SELECT id, titulo, edicion, paginas, lsbn, genero FROM Libro WHERE titulo LIKE ? OR edicion LIKE ?";
        List<Libro> libros = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Adding wildcards for partial match search
            String wildcardSearchKey = "%" + searchKey + "%";
            stmt.setString(1, wildcardSearchKey);
            stmt.setString(2, wildcardSearchKey);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setEdicion(rs.getString("edicion"));
                    libro.setPaginas(rs.getInt("paginas"));
                    libro.setLsbn(rs.getInt("lsbn"));
                    libro.setGenero(Genero.valueOf(rs.getString("genero")));

                    libros.add(libro);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libros: " + e.getMessage());
        }

        return libros;
    }

}
