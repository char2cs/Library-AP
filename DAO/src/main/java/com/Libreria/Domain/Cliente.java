package com.Libreria.Domain;

import java.util.List;

public class Cliente extends Perfil {
    private String email;
    private List<Prestamo> prestamos;

    public Cliente() {}

    public Cliente(
            Integer id,
            Integer dni,
            String nombre,
            String apellido,
            Integer telefono,
            String email,
            List<Prestamo> prestamos
    ) {
        super(id, dni, nombre, apellido, telefono);
        this.email = email;
        this.prestamos = prestamos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
}
