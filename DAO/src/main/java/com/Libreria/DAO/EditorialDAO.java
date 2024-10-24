package com.Libreria.DAO;

import com.Libreria.Domain.Autor;
import com.Libreria.Domain.Editorial;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;

import java.util.List;

public interface EditorialDAO extends CrudDAO<Editorial> {
    void updateEditorialLibro(Editorial editorial) throws ObjectNotFoundException, OperationFailedException;
    Editorial obtenerEditorialPorInstancia(Integer idInstancia) throws ObjectNotFoundException;
    List<Editorial> obtenerEditorialPorLibro(Integer idLibro);
}
