package com.Libreria.Utils;

import com.Libreria.Domain.Libro;

import java.util.List;

public class PrestamoRequest {
    private Integer cliente_id;
    private List<Libro> libros;

    public PrestamoRequest() {}

    public PrestamoRequest(
            Integer cliente_id,
            List<Libro> libros
    ) {
        this.cliente_id = cliente_id;
        this.libros = libros;
    }

    public Integer getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Integer cliente_id) {
        this.cliente_id = cliente_id;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}

