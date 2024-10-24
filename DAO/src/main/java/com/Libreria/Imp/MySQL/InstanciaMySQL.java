package com.Libreria.Imp.MySQL;

import com.Libreria.DAO.InstanciaDAO;
import com.Libreria.Domain.Instancia;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Utils.MySQLconn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstanciaMySQL implements InstanciaDAO {
    private Connection connection;

    public InstanciaMySQL() {
        try {
            this.connection = MySQLconn.getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Instancia instancia) throws ObjectAlreadyExistsException {
        String query = "INSERT INTO Instancia (fecha_entrada) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(instancia.getFechaEntrada().getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectAlreadyExistsException("No se pudo crear la instancia.");

            try (ResultSet generatedKeys = stmt.getGeneratedKeys())
            {
                if (generatedKeys.next())
                    instancia.setId(generatedKeys.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al crear la instancia: " + e.getMessage());
        }
    }

    @Override
    public Instancia get(Integer id) throws ObjectNotFoundException {
        String query = "SELECT id, fecha_entrada FROM Instancia WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    Instancia instancia = new Instancia();
                    instancia.setId(rs.getInt("id"));
                    instancia.setFechaEntrada(rs.getDate("fecha_entrada"));

                    return instancia;
                }
                else
                    throw new ObjectNotFoundException("Instancia con id " + id + " no encontrada.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener la instancia: " + e.getMessage());
        }
    }

    @Override
    public boolean update(Instancia instancia) throws ObjectNotFoundException, OperationFailedException {
        String query = "UPDATE Instancia SET fecha_entrada = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setDate(1, new java.sql.Date(instancia.getFechaEntrada().getTime()));
            stmt.setInt(2, instancia.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("Instancia con id " + instancia.getId() + " no encontrada.");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al actualizar la instancia: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        String query = "DELETE FROM Instancia WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("Instancia con id " + id + " no encontrada.");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al eliminar la instancia: " + e.getMessage());
        }
    }

    @Override
    public List<Instancia> getAll() {
        String query = "SELECT id, fecha_entrada FROM Instancia";
        List<Instancia> instancias = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query))
        {

            while (rs.next())
            {
                Instancia instancia = new Instancia();
                instancia.setId(rs.getInt("id"));
                instancia.setFechaEntrada(rs.getDate("fecha_entrada"));

                instancias.add(instancia);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las instancias: " + e.getMessage());
        }

        return instancias;
    }

    @Override
    public List<Instancia> obtenerInstanciasPorPrestamo(Integer idPrestamo) {
        List<Instancia> instancias = new ArrayList<>();
        String query = "SELECT i.id, i.fecha_entrada " +
                "FROM Instancia i " +
                "JOIN Prestamo_Libro pl ON i.id = pl.id_instancia " +
                "WHERE pl.id_prestamo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, idPrestamo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Instancia instancia = new Instancia();
                instancia.setId(rs.getInt("id"));
                instancia.setFechaEntrada(rs.getDate("fecha_entrada"));
                instancias.add(instancia);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener instancias por préstamo: " + e.getMessage(), e);
        }

        return instancias;
    }

    @Override
    public void updateLibro(Instancia instancia) {
        String query = "UPDATE libreria.Editorial_Libro el " +
                "JOIN libreria.Instancia i ON i.editorial_libro_id = el.id " +
                "SET el.libro_id = ? " +
                "WHERE i.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, instancia.getLibro().getId());
            stmt.setInt(2, instancia.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new SQLException("No se pudo actualizar el libro. Instancia no encontrada.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEditorial(Instancia instancia) {
        String query = "UPDATE libreria.Editorial_Libro el " +
                "JOIN libreria.Instancia i ON i.editorial_libro_id = el.id " +
                "SET el.editorial_id = ? " +
                "WHERE i.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setInt(1, instancia.getEditorial().getId());
            stmt.setInt(2, instancia.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new SQLException("No se pudo actualizar la editorial. Instancia no encontrada.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Instancia obtenerInstanciasDisponibles(Integer idLibro) {
        String sql = "SELECT i.id, i.fecha_entrada FROM Instancia i " +
                "LEFT JOIN Prestamo_Libro pl ON i.id = pl.instancia_id " +
                "WHERE i.libro_id = ? AND pl.prestamo_id IS NULL";

        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, idLibro);
            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next())
                {
                    Instancia instancia = new Instancia();
                    instancia.setId(resultSet.getInt("id"));
                    instancia.setFechaEntrada(resultSet.getDate("fecha_entrada"));
                    return instancia;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
