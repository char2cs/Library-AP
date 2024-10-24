package com.Libreria.Service;

import com.Libreria.DAO.CrudDAO;
import com.Libreria.DAO.EditorialDAO;
import com.Libreria.DAO.LibroDAO;
import com.Libreria.Domain.Editorial;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;

import java.util.List;

public class EditorialService implements CrudDAO<Editorial> {
    private final EditorialDAO editorialDAO;
    private final LibroDAO libroDAO;

    public EditorialService(
            EditorialDAO editorialDAO,
            LibroDAO libroDAO
    ) {
        this.editorialDAO = editorialDAO;
        this.libroDAO = libroDAO;
    }

    @Override
    public void create(Editorial editorial) throws ObjectAlreadyExistsException {
        editorialDAO.create(editorial);

        try {
            editorialDAO.updateEditorialLibro(editorial);
        }
        catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
        catch (OperationFailedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Editorial get(Integer id) throws ObjectNotFoundException {
        Editorial editorial = editorialDAO.get(id);

        editorial.setLibro(
                libroDAO.obtenerLibrosPorEditorial(id)
        );

        return editorial;
    }

    @Override
    public boolean update(Editorial editorial) throws ObjectNotFoundException, OperationFailedException {
        editorialDAO.updateEditorialLibro(editorial);
        return editorialDAO.update(editorial);
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        return editorialDAO.delete(id);
    }

    @Override
    public List<Editorial> getAll() {
        List<Editorial> editoriales = editorialDAO.getAll();

        for (Editorial editorial : editoriales)
            editorial.setLibro(
                    libroDAO.obtenerLibrosPorAutor(
                            editorial.getId()
                    )
            );

        return editoriales;
    }
}
