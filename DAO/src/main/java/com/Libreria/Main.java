package com.Libreria;

import com.Libreria.DAO.ClienteDAO;
import com.Libreria.Domain.Cliente;
import com.Libreria.Domain.Prestamo;
import com.Libreria.Factory.DAOFactory;
import com.Libreria.Utils.MySQLconn;

import java.sql.Connection;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = MySQLconn.getConnection();

            ClienteDAO clienteDAO = DAOFactory.getClienteDAO(DAOFactory.DatabaseType.MYSQL);

            Cliente cliente = new Cliente(
                    null,
                    95518340,
                    "Mateo",
                    "Urrutia",
                    1128447029,
                    "mateo@rounds.com.ar",
                    new ArrayList<Prestamo>()
            );

            clienteDAO.create(cliente);

            System.out.println(cliente.toString());

            cliente.setEmail("mateourru@gmail.com");
            clienteDAO.update(cliente);

            System.out.println(cliente.toString());

            clienteDAO.delete(cliente.getId());
        }
        catch (Exception e) {
            e.printStackTrace();
            assert(false) : "UNFORESEEN CONSEQUENCES: MySQL connection failed";
        }

        System.out.println("Working...!");
    }
}