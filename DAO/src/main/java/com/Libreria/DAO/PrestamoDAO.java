package com.Libreria.DAO;

import com.Libreria.Domain.Prestamo;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;

import java.util.List;

public interface PrestamoDAO extends CrudDAO<Prestamo> {
    List<Prestamo> obtenerPrestamosPorCliente(Integer idCliente);
    List<Prestamo> obtenerPrestamosPorInstancia(Integer idInstancia);
    void updatePrestamoLibro(Prestamo prestamo) throws OperationFailedException;
    void updateClient(Prestamo prestamo) throws ObjectNotFoundException, OperationFailedException;
}
