package com.Libreria.Imp.MySQL;

import com.Libreria.DAO.AdministrativoDAO;
import com.Libreria.Domain.Administrativo;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Utils.MySQLconn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministrativoMySQL implements AdministrativoDAO {
    private Connection connection;

    public AdministrativoMySQL() {
        try {
            this.connection = MySQLconn.getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Administrativo administrativo) throws ObjectAlreadyExistsException {
        String sql = "{CALL CreateAdministrativo(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql))
        {
            statement.setInt(1, administrativo.getDni());
            statement.setString(2, administrativo.getNombre());
            statement.setString(3, administrativo.getApellido());
            statement.setInt(4, administrativo.getTelefono());
            statement.setInt(5, administrativo.getLegajo());

            statement.registerOutParameter(6, java.sql.Types.INTEGER);

            statement.executeUpdate();

            administrativo.setId(
                    statement.getInt(6)
            );
        }
        catch (SQLException e) {
            throw new ObjectAlreadyExistsException("El administrativo ya existe!");
        }
    }

    @Override
    public Administrativo get(Integer id) throws ObjectNotFoundException {
        String sql = "SELECT p.id, a.legajo, p.dni, p.nombre, p.apellido, p.telefono " +
                "FROM Administrativo a " +
                "LEFT JOIN Perfil p ON a.perfil_id = p.id " +
                "WHERE p.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return new Administrativo(
                        resultSet.getInt("id"),
                        resultSet.getInt("dni"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getInt("telefono"),
                        resultSet.getInt("legajo")
                );
            else
                throw new ObjectNotFoundException("Administrativo no encontrado con ID: " + id);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(Administrativo administrativo) throws ObjectNotFoundException, OperationFailedException {
        this.get(administrativo.getId());

        String sql = "{CALL UpdateAdministrativo(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql))
        {
            statement.setInt(1, administrativo.getId());
            statement.setInt(2, administrativo.getDni());
            statement.setString(3, administrativo.getNombre());
            statement.setString(4, administrativo.getApellido());
            statement.setInt(5, administrativo.getTelefono());
            statement.setInt(6, administrativo.getLegajo());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al actualizar el administrativo");
        }
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        String sql = "{CALL DeleteAdministrativo(?)}";

        try (CallableStatement statement = connection.prepareCall(sql))
        {
            statement.setInt(1, id);
            statement.executeUpdate();

            return true;
        }
        catch (SQLException e) {
            throw new OperationFailedException("Error al eliminar el administrativo");
        }
    }

    @Override
    public List<Administrativo> getAll() {
        List<Administrativo> administrativos = new ArrayList<>();
        String sql = "SELECT p.id, a.legajo, p.dni, p.nombre, p.apellido, p.telefono " +
                "FROM Administrativo a " +
                "LEFT JOIN Perfil p ON a.perfil_id = p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next()) {
                administrativos.add(new Administrativo(
                        resultSet.getInt("id"),
                        resultSet.getInt("dni"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getInt("telefono"),
                        resultSet.getInt("legajo")
                ));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return administrativos;
    }


}
