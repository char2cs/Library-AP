package com.Libreria.DAO;

import com.Libreria.Domain.Instancia;

import java.util.List;

public interface InstanciaDAO extends CrudDAO<Instancia> {
    List<Instancia> obtenerInstanciasPorPrestamo(Integer idPrestamo);

    void updateLibro(Instancia instancia);

    void updateEditorial(Instancia instancia);

    Instancia obtenerInstanciasDisponibles(Integer idLibro);
}
