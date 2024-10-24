package com.Libreria.Domain;

import java.util.List;

public class Autor {
    private Integer id;
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private List<Libro> libros;

    public Autor() {}

    public Autor(
            Integer id,
            String nombre,
            String apellido,
            String nacionalidad,
            List<Libro> libros
    ) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.libros = libros;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
