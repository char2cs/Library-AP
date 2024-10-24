package com.Libreria.Imp.MySQL;

import com.Libreria.DAO.ClienteDAO;
import com.Libreria.Domain.Cliente;
import com.Libreria.Domain.Prestamo;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Utils.MySQLconn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteMySQL implements ClienteDAO {
    private Connection connection;

    public ClienteMySQL() {
        try {
            this.connection = MySQLconn.getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Cliente cliente) throws ObjectAlreadyExistsException {
        String sql = "{CALL CreateCliente(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql))
        {
            statement.setInt(1, cliente.getDni());
            statement.setString(2, cliente.getNombre());
            statement.setString(3, cliente.getApellido());
            statement.setInt(4, cliente.getTelefono());
            statement.setString(5, cliente.getEmail());

            statement.registerOutParameter(6, java.sql.Types.INTEGER);

            statement.executeUpdate();

            cliente.setId(
                    statement.getInt(6)
            );
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new ObjectAlreadyExistsException("El cliente ya existe!");
        }
    }

    @Override
    public Cliente get(Integer id) throws ObjectNotFoundException {
        String sql = "SELECT p.id, email, p.dni, p.nombre, p.apellido, p.telefono " +
                "FROM Cliente c" +
                "LEFT JOIN Perfil p ON perfil_id = p.id " +
                "WHERE p.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return new Cliente(
                        resultSet.getInt("id"),
                        resultSet.getInt("dni"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getInt("telefono"),
                        resultSet.getString("email"),
                        new ArrayList<Prestamo>()
                );
            else
                throw new ObjectNotFoundException("Cliente no encontrado con ID: " + id);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(Cliente cliente) throws ObjectNotFoundException, OperationFailedException {
        this.get(
                cliente.getId()
        );

        String sql = "{CALL UpdateCliente(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql))
        {
            statement.setInt(1, cliente.getId());
            statement.setInt(2, cliente.getDni());
            statement.setString(3, cliente.getNombre());
            statement.setString(4, cliente.getApellido());
            statement.setInt(5, cliente.getTelefono());
            statement.setString(6, cliente.getEmail());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al actualizar el cliente");
        }
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        String sql = "{CALL DeleteCliente(?)}";

        try (CallableStatement statement = connection.prepareCall(sql))
        {
            statement.setInt(1, id);
            statement.executeUpdate();

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al eliminar el cliente");
        }
    }

    @Override
    public List<Cliente> getAll() {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT perfil_id, email, dni, nombre, apellido, telefono " +
                "FROM Cliente " +
                "LEFT JOIN Perfil p ON perfil_id = p.id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next()) {
                clientes.add(new Cliente(
                        resultSet.getInt("perfil_id"),
                        resultSet.getInt("dni"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getInt("telefono"),
                        resultSet.getString("email"),
                        new ArrayList<Prestamo>()
                ));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    @Override
    public Cliente obtenerClientePorPrestamo(int idPrestamo) throws ObjectNotFoundException {
        String query = "SELECT c.id FROM Cliente c " +
                "JOIN Prestamo p ON c.id = p.id_cliente " +
                "WHERE p.id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query))
        {
            ps.setInt(1, idPrestamo);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return this.get(
                        rs.getInt("id")
                );

            throw new ObjectNotFoundException("No se encontró ningún cliente para el préstamo con ID: " + idPrestamo);
        }
        catch (SQLException e) {
            throw new RuntimeException("Error al obtener el cliente por préstamo", e);
        }
    }
}
