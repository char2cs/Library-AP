package com.Libreria.DAO;

import com.Libreria.Domain.Autor;
import com.Libreria.Domain.Editorial;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;

import java.util.List;

public interface AutorDAO extends CrudDAO<Autor> {
    void updateAutorLibro(Autor autor) throws ObjectNotFoundException, OperationFailedException;
    List<Autor> obtenerAutoresPorLibro(Integer id);
    List<Autor> getAutoresByNames(String name);
}
