package com.Libreria.Domain;

import java.util.Date;

public class Instancia {
    private Integer id;
    private Editorial editorial;
    private Libro libro;
    private Date fechaEntrada;

    public Instancia() {}

    public Instancia(
            Integer id,
            Editorial editorial,
            Libro libro,
            Date fechaEntrada
    ) {
        this.id = id;
        this.editorial = editorial;
        this.libro = libro;
        this.fechaEntrada = fechaEntrada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }
}
