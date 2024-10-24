package com.Libreria.Service;

import com.Libreria.DAO.ClienteDAO;
import com.Libreria.DAO.CrudDAO;
import com.Libreria.DAO.PrestamoDAO;
import com.Libreria.Domain.Cliente;
import com.Libreria.Domain.Prestamo;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;

import java.util.List;

public class ClienteService implements CrudDAO<Cliente> {
    private final ClienteDAO clienteDAO;
    private final PrestamoDAO prestamoDAO;

    public ClienteService(
            ClienteDAO clienteDAO,
            PrestamoDAO prestamoDAO
    ) {
        this.clienteDAO = clienteDAO;
        this.prestamoDAO = prestamoDAO;
    }

    @Override
    public void create(Cliente cliente) throws ObjectAlreadyExistsException {
        this.updatePrestamos(cliente);
        clienteDAO.create(cliente);
    }

    @Override
    public boolean update(Cliente cliente) throws ObjectNotFoundException, OperationFailedException {
        this.updatePrestamos(cliente);
        return clienteDAO.update(cliente);
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        return clienteDAO.delete(id);
    }

    @Override
    public List<Cliente> getAll() {
        List<Cliente> clientes = clienteDAO.getAll();

        for (Cliente cliente : clientes)
            cliente.setPrestamos(
                    prestamoDAO.obtenerPrestamosPorCliente(
                            cliente.getId()
                    )
            );

        return clientes;
    }

    @Override
    public Cliente get(Integer id) throws ObjectNotFoundException {
        Cliente cliente = clienteDAO.get(id);

        cliente.setPrestamos(
                prestamoDAO.obtenerPrestamosPorCliente(
                        cliente.getId()
                )
        );

        return cliente;
    }

    public boolean updatePrestamos(Cliente cliente) {
        boolean returnBool = true;

        for (Prestamo prestamo : cliente.getPrestamos())
        {
            prestamo.setCliente(cliente);

            try {
                prestamoDAO.updateClient(prestamo);
            }
            catch (ObjectNotFoundException e) {
                returnBool = false;
            }
            catch (OperationFailedException e) {
                returnBool = false;
            }
        }

        return returnBool;
    }
}
