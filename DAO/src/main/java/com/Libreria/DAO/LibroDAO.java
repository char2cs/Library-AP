package com.Libreria.DAO;

import com.Libreria.Domain.Libro;

import java.util.List;

public interface LibroDAO extends CrudDAO<Libro> {
    List<Libro> obtenerLibrosPorAutor(Integer idAutor);

    List<Libro> obtenerLibrosPorEditorial(Integer idEditorial);

    Libro obtenerLibroPorInstancia(Integer idInstancia);

    List<Libro> searchLibros(String seachkey);
}
