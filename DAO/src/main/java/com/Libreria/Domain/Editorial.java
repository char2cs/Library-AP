package com.Libreria.Domain;

import java.util.List;

public class Editorial {
    private Integer id;
    private String nombre;
    private String direccion;
    private String url;
    private String email;
    private Integer telefono;
    private List<Libro> libro;

    public Editorial() {}

    public Editorial(
            Integer id,
            String nombre,
            String direccion,
            String url,
            String email,
            Integer telefono,
            List<Libro> libro
    ) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.url = url;
        this.email = email;
        this.telefono = telefono;
        this.libro = libro;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public List<Libro> getLibro() {
        return libro;
    }

    public void setLibro(List<Libro> libro) {
        this.libro = libro;
    }
}
