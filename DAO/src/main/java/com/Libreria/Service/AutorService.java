package com.Libreria.Service;

import com.Libreria.DAO.AutorDAO;
import com.Libreria.DAO.CrudDAO;
import com.Libreria.DAO.LibroDAO;
import com.Libreria.Domain.Autor;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;

import java.util.List;

public class AutorService implements CrudDAO<Autor> {
    private AutorDAO autorDAO;
    private LibroDAO libroDAO;

    public AutorService(
            AutorDAO autorDAO,
            LibroDAO libroDAO
    ) {
        this.autorDAO = autorDAO;
        this.libroDAO = libroDAO;
    }

    @Override
    public void create(Autor autor) throws ObjectAlreadyExistsException {
        autorDAO.create(autor);

        try {
            autorDAO.updateAutorLibro(autor);
        }
        catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
        catch (OperationFailedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Autor autor) throws ObjectNotFoundException, OperationFailedException {
        boolean autorBool = autorDAO.update(autor);

        autorDAO.updateAutorLibro(autor);

        return autorBool;
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        return autorDAO.delete(id);
    }

    public Autor get(Integer id) throws ObjectNotFoundException {
        Autor autor = autorDAO.get(id);

        autor.setLibros(
                libroDAO.obtenerLibrosPorAutor(id)
        );

        return autor;
    }

    public List<Autor> getAll() {
        List<Autor> autores = autorDAO.getAll();

        for (Autor autor : autores)
            autor.setLibros(
                    libroDAO.obtenerLibrosPorAutor(
                            autor.getId()
                    )
            );

        return autores;
    }
}
