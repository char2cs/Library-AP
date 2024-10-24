package com.Libreria.Service;

import com.Libreria.DAO.CrudDAO;
import com.Libreria.DAO.EditorialDAO;
import com.Libreria.DAO.InstanciaDAO;
import com.Libreria.DAO.LibroDAO;
import com.Libreria.Domain.Instancia;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;

import java.util.List;

public class InstanciaService implements CrudDAO<Instancia> {
    private final InstanciaDAO instanciaDAO;
    private final EditorialDAO editorialDAO;
    private final LibroDAO libroDAO;

    public InstanciaService(
            InstanciaDAO instanciaDAO,
            EditorialDAO editorialDAO,
            LibroDAO libroDAO
    ) {
        this.instanciaDAO = instanciaDAO;
        this.editorialDAO = editorialDAO;
        this.libroDAO = libroDAO;
    }

    @Override
    public void create(Instancia instancia) throws ObjectAlreadyExistsException {
        instanciaDAO.create(instancia);

        instanciaDAO.updateEditorial(instancia);
        instanciaDAO.updateLibro(instancia);
    }

    @Override
    public Instancia get(Integer id) throws ObjectNotFoundException {
        Instancia instancia = instanciaDAO.get(id);

        try {
            instancia.setEditorial(
                    editorialDAO.obtenerEditorialPorInstancia(
                            instancia.getId()
                    )
            );

            instancia.setLibro(
                    libroDAO.obtenerLibroPorInstancia(
                            instancia.getId()
                    )
            );
        }
        catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }

        return instancia;
    }

    @Override
    public boolean update(Instancia instancia) throws ObjectNotFoundException, OperationFailedException {
        instanciaDAO.updateEditorial(instancia);
        instanciaDAO.updateLibro(instancia);

        return instanciaDAO.update(instancia);
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        return instanciaDAO.delete(id);
    }

    @Override
    public List<Instancia> getAll() {
        List<Instancia> instancias = instanciaDAO.getAll();

        for (Instancia instancia : instancias)
            try {
                instancia.setEditorial(
                        editorialDAO.obtenerEditorialPorInstancia(
                                instancia.getId()
                        )
                );

                instancia.setLibro(
                        libroDAO.obtenerLibroPorInstancia(
                                instancia.getId()
                        )
                );
            }
            catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }

        return instancias;
    }
}
