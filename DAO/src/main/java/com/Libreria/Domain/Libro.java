package com.Libreria.Domain;

import java.util.List;

public class Libro {
    private Integer id;
    private Integer lsbn;
    private String titulo;
    private Integer paginas;
    private Genero genero;
    private String edicion;
    private List<Autor> autores;
    private List<Editorial> editoriales;

    public Libro() {}

    public Libro(
            Integer id,
            Integer lsbn,
            String titulo,
            Integer paginas,
            Genero genero,
            String edicion,
            List<Autor> autores,
            List<Editorial> editoriales
    ) {
        this.id = id;
        this.lsbn = lsbn;
        this.titulo = titulo;
        this.paginas = paginas;
        this.genero = genero;
        this.edicion = edicion;
        this.autores = autores;
        this.editoriales = editoriales;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLsbn() {
        return lsbn;
    }

    public void setLsbn(Integer lsbn) {
        this.lsbn = lsbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<Editorial> getEditoriales() {
        return editoriales;
    }

    public void setEditoriales(List<Editorial> editoriales) {
        this.editoriales = editoriales;
    }
}
