package com.Libreria.DAO;

import com.Libreria.Domain.Cliente;
import com.Libreria.Exception.ObjectNotFoundException;

public interface ClienteDAO extends CrudDAO<Cliente> {
    public Cliente obtenerClientePorPrestamo(int idPrestamo) throws ObjectNotFoundException;
}
