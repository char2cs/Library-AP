package com.Libreria.Service;

import com.Libreria.DAO.*;
import com.Libreria.Domain.Editorial;
import com.Libreria.Domain.Instancia;
import com.Libreria.Domain.Libro;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;

import java.util.Date;
import java.util.List;

public class LibroService implements CrudDAO<Libro> {
    private final LibroDAO libroDAO;
    private final AutorDAO autorDAO;
    private final EditorialDAO editorialDAO;
    private final InstanciaDAO instanciaDAO;

    public LibroService(
            LibroDAO libroDAO,
            AutorDAO autorDAO,
            EditorialDAO editorialDAO,
            InstanciaDAO instanciaDAO
    ) {
        this.libroDAO = libroDAO;
        this.autorDAO = autorDAO;
        this.editorialDAO = editorialDAO;
        this.instanciaDAO = instanciaDAO;
    }

    @Override
    public void create(
            Libro libro
    ) throws ObjectAlreadyExistsException {
        libroDAO.create(libro);
    }

    @Override
    public Libro get(
            Integer id
    ) throws ObjectNotFoundException {
        Libro libro = libroDAO.get(id);

        libro.setAutores(
                autorDAO.obtenerAutoresPorLibro(id)
        );

        libro.setEditoriales(
                editorialDAO.obtenerEditorialPorLibro(id)
        );

        return libro;
    }

    @Override
    public boolean update(
            Libro libro
    ) throws ObjectNotFoundException, OperationFailedException {
        return libroDAO.update(libro);
    }

    @Override
    public boolean delete(
            Integer id
    ) throws ObjectNotFoundException, OperationFailedException {
        return libroDAO.delete(id);
    }

    @Override
    public List<Libro> getAll() {
        List<Libro> libros = libroDAO.getAll();

        for (Libro libro : libros)
        {
            libro.setAutores(
                    autorDAO.obtenerAutoresPorLibro(
                            libro.getId()
                    )
            );

            libro.setEditoriales(
                    editorialDAO.obtenerEditorialPorLibro(
                            libro.getId()
                    )
            );
        }

        return libros;
    }

    public List<Libro> searchLibros(
            String searchKey
    ) {
        List<Libro> libros = libroDAO.searchLibros(searchKey);

        for (Libro libro : libros)
        {
            libro.setAutores(
                    autorDAO.obtenerAutoresPorLibro(
                            libro.getId()
                    )
            );

            libro.setEditoriales(
                    editorialDAO.obtenerEditorialPorLibro(
                            libro.getId()
                    )
            );
        }

        return libros;
    }

    public void createLibro(
            Libro libro,
            Editorial editorial,
            Integer numeroInstancias
    ) throws ObjectAlreadyExistsException {
        this.create(libro);

        for (int i = 0; i < numeroInstancias; i++)
            instanciaDAO.create(new Instancia(
                    null,
                    editorial,
                    libro,
                    null
            ));
    }
}
