package com.Libreria.Domain;

import java.util.Date;
import java.util.List;

public class Prestamo {
    private Integer id;
    private List<Instancia> instancias;
    private Cliente cliente;
    private Date fechaInicio;
    private Date fechaFin;

    public Prestamo() {}

    public Prestamo(
            Integer id,
            List<Instancia> instancias,
            Cliente cliente,
            Date fechaInicio,
            Date fechaFin
    ) {
        this.id = id;
        this.instancias = instancias;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Instancia> getInstancias() {
        return instancias;
    }

    public void setLibros(List<Instancia> instancias) {
        this.instancias = instancias;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
