package com.Libreria.Domain;

public abstract class Perfil {
    private Integer id;
    private Integer dni;
    private String nombre;
    private String apellido;
    private Integer telefono;

    public Perfil() {}

    public Perfil(
            Integer id,
            Integer dni,
            String nombre,
            String apellido,
            Integer telefono
    ) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
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

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "id=" + id +
                ", dni=" + dni +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono=" + telefono +
                '}';
    }
}
