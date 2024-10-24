package com.Libreria.Domain;

public class Administrativo extends Perfil {
    private Integer legajo;

    public Administrativo() {}

    public Administrativo(
            Integer id,
            Integer dni,
            String nombre,
            String apellido,
            Integer telefono,
            Integer legajo
    ) {
        super(id, dni, nombre, apellido, telefono);
        this.legajo = legajo;
    }

    public Integer getLegajo() {
        return legajo;
    }

    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }

    @Override
    public String toString() {
        return super.toString() + "\n Administrativo{" +
                "legajo=" + legajo +
                '}';
    }
}
