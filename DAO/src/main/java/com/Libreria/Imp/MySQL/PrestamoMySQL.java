package com.Libreria.Imp.MySQL;

import com.Libreria.DAO.PrestamoDAO;
import com.Libreria.Domain.Instancia;
import com.Libreria.Domain.Prestamo;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Utils.MySQLconn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoMySQL implements PrestamoDAO {
    private Connection connection;

    public PrestamoMySQL() {
        try {
            this.connection = MySQLconn.getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Prestamo prestamo) throws ObjectAlreadyExistsException {
        String sql = "INSERT INTO Prestamo (cliente_id, fecha_inicio, fecha_fin) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, prestamo.getCliente().getId());
            stmt.setDate(2, new java.sql.Date(prestamo.getFechaInicio().getTime()));
            stmt.setDate(3, new java.sql.Date(prestamo.getFechaFin().getTime()));
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0)
                throw new ObjectAlreadyExistsException("No se pudo crear el préstamo");

            try (ResultSet generatedKeys = stmt.getGeneratedKeys())
            {
                if (generatedKeys.next())
                    prestamo.setId(generatedKeys.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Prestamo get(Integer id) throws ObjectNotFoundException {
        String sql = "SELECT p.id, p.cliente_id, p.fecha_inicio, p.fecha_fin FROM Prestamo p WHERE p.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(rs.getInt("id"));
                    prestamo.setFechaInicio(rs.getDate("fecha_inicio"));
                    prestamo.setFechaFin(rs.getDate("fecha_fin"));

                    return prestamo;
                }
                else
                    throw new ObjectNotFoundException("Préstamo no encontrado con ID: " + id);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Prestamo prestamo) throws ObjectNotFoundException, OperationFailedException {
        String sql = "UPDATE Prestamo SET cliente_id = ?, fecha_inicio = ?, fecha_fin = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, prestamo.getCliente().getId());
            stmt.setDate(2, new java.sql.Date(prestamo.getFechaInicio().getTime()));
            stmt.setDate(3, new java.sql.Date(prestamo.getFechaFin().getTime()));
            stmt.setInt(4, prestamo.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0)
                throw new ObjectNotFoundException("No se encontró el préstamo para actualizar");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al actualizar el préstamo");
        }
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        String sql = "DELETE FROM Prestamo WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("No se encontró el préstamo para eliminar");

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al eliminar el préstamo");
        }
    }

    @Override
    public List<Prestamo> getAll() {
        String sql = "SELECT * FROM Prestamo";
        List<Prestamo> prestamos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                Prestamo prestamo = new Prestamo();
                prestamo.setId(rs.getInt("id"));
                prestamo.setFechaInicio(rs.getDate("fecha_inicio"));
                prestamo.setFechaFin(rs.getDate("fecha_fin"));

                prestamos.add(prestamo);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prestamos;
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorCliente(Integer idCliente) {
        String sql = "SELECT * FROM Prestamo WHERE cliente_id = ?";
        List<Prestamo> prestamos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(rs.getInt("id"));
                    prestamo.setFechaInicio(rs.getDate("fecha_inicio"));
                    prestamo.setFechaFin(rs.getDate("fecha_fin"));

                    prestamos.add(prestamo);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prestamos;
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorInstancia(Integer idInstancia) {
        String sql = "SELECT p.* FROM Prestamo p JOIN Prestamo_Libro pi ON p.id = pi.prestamo_id WHERE pi.instancia_id = ?";
        List<Prestamo> prestamos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, idInstancia);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(rs.getInt("id"));
                    prestamo.setFechaInicio(rs.getDate("fecha_inicio"));
                    prestamo.setFechaFin(rs.getDate("fecha_fin"));

                    prestamos.add(prestamo);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prestamos;
    }

    @Override

    public void updatePrestamoLibro(Prestamo prestamo) throws OperationFailedException {
        String deleteQuery = "DELETE FROM Prestamo_Libro WHERE prestamo_id = ?";
        String insertQuery = "INSERT INTO Prestamo_Libro (prestamo_id, instancia_id) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement deletePS = connection.prepareStatement(deleteQuery))
            {
                deletePS.setInt(1, prestamo.getId());
                deletePS.executeUpdate();
            }

            try (PreparedStatement insertPS = connection.prepareStatement(insertQuery))
            {
                for (Instancia instancia : prestamo.getInstancias())
                {
                    insertPS.setInt(1, prestamo.getId());
                    insertPS.setInt(2, instancia.getId());
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

            throw new OperationFailedException("Error al actualizar la relación préstamo-libro.");
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
    public void updateClient(Prestamo prestamo) throws ObjectNotFoundException, OperationFailedException {
        String query = "UPDATE Prestamo SET cliente_id = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query))
        {
            ps.setInt(1, prestamo.getCliente().getId());
            ps.setInt(2, prestamo.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0)
                throw new ObjectNotFoundException("No se pudo encontrar el préstamo con id: " + prestamo.getId());
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new OperationFailedException("Error al actualizar el cliente en el préstamo.");
        }
    }

}
