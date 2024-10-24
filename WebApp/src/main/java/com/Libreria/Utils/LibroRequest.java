package com.Libreria.Utils;

import java.util.List;

public class LibroRequest {
    private String titulo;
    private String lsbn;
    private String paginas;
    private String genero;
    private String edicion;
    private List<String> autores;
    private List<String> editoriales;

    public LibroRequest() {}

    public LibroRequest(
            String titulo,
            String lsbn,
            String paginas,
            String genero,
            String edicion,
            List<String> autores,
            List<String> editoriales
    ) {
        this.titulo = titulo;
        this.lsbn = lsbn;
        this.paginas = paginas;
        this.genero = genero;
        this.edicion = edicion;
        this.autores = autores;
        this.editoriales = editoriales;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLsbn() {
        return lsbn;
    }

    public void setLsbn(String lsbn) {
        this.lsbn = lsbn;
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public List<String> getAutores() {
        return autores;
    }

    public void setAutores(List<String> autores) {
        this.autores = autores;
    }

    public List<String> getEditoriales() {
        return editoriales;
    }

    public void setEditoriales(List<String> editoriales) {
        this.editoriales = editoriales;
    }
}

