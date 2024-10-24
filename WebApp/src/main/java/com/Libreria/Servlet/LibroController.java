package com.Libreria.Servlet;

import com.Libreria.DAO.AutorDAO;
import com.Libreria.DAO.EditorialDAO;
import com.Libreria.DAO.LibroDAO;
import com.Libreria.Domain.Autor;
import com.Libreria.Domain.Editorial;
import com.Libreria.Domain.Libro;
import com.Libreria.Domain.Genero;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Factory.DAOFactory;
import com.Libreria.Service.LibroService;
import com.Libreria.Utils.LibroRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/libros")
public class LibroController extends HttpServlet {
    private final static LibroDAO libroDAO;
    private final static LibroService libroService;
    private final static AutorDAO autorDAO;
    private final static EditorialDAO editorialDAO;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        libroDAO = DAOFactory.getLibroDAO(DAOFactory.DatabaseType.MYSQL);
        libroService = new LibroService(
                libroDAO,
                DAOFactory.getAutorDAO(DAOFactory.DatabaseType.MYSQL),
                DAOFactory.getEditorialDAO(DAOFactory.DatabaseType.MYSQL),
                DAOFactory.getInstanciaDAO(DAOFactory.DatabaseType.MYSQL)
        );
        autorDAO = DAOFactory.getAutorDAO(DAOFactory.DatabaseType.MYSQL);
        editorialDAO = DAOFactory.getEditorialDAO(DAOFactory.DatabaseType.MYSQL);
    }

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String titulo = request.getParameter("titulo");

        List<Libro> libros = new ArrayList<>();

        if (titulo != null && !titulo.trim().isEmpty())
            libros = libroService.searchLibros(titulo);
        else
            libros = libroService.getAll();

        if (libros == null || libros.isEmpty())
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            response.getWriter().write("{\"message\":\"No se encontraron libros\"}");
        }
        else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK); // 200
            response.getWriter().write(objectMapper.writeValueAsString(libros));
        }
    }

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            LibroRequest libroRequest = objectMapper.readValue(request.getInputStream(), LibroRequest.class);

            Libro newLibro = new Libro();
            newLibro.setTitulo(libroRequest.getTitulo());
            newLibro.setEdicion(libroRequest.getEdicion());

            newLibro.setLsbn(Integer.parseInt(libroRequest.getLsbn()));
            newLibro.setPaginas(Integer.parseInt(libroRequest.getPaginas()));
            newLibro.setGenero(Genero.valueOf(libroRequest.getGenero().toUpperCase()));

            // TODO CRUD de autores y editoriales...

            libroDAO.create(newLibro);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"message\":\"Libro creado exitosamente\"}");

        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ISBN o número de páginas inválido\"}");
        }
        catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Genero inválido\"}");
        }
        catch (ObjectAlreadyExistsException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"error\":\"El libro ya existe\"}");
        }
    }

    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String idString = request.getParameter("id");
        try {
            int id = Integer.parseInt(idString);

            Libro existingLibro = libroDAO.get(id);
            if (existingLibro == null)
            {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Libro no encontrado\"}");
                return;
            }

            String titulo = request.getParameter("titulo");
            String lsbnString = request.getParameter("lsbn");
            String paginasString = request.getParameter("paginas");
            String generoString = request.getParameter("genero");
            String edicion = request.getParameter("edicion");

            existingLibro.setTitulo(titulo);
            existingLibro.setEdicion(edicion);

            existingLibro.setLsbn(Integer.parseInt(lsbnString));
            existingLibro.setPaginas(Integer.parseInt(paginasString));
            existingLibro.setGenero(Genero.valueOf(generoString.toUpperCase()));

            libroDAO.update(existingLibro);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\":\"Libro actualizado exitosamente\"}");
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID inválido\"}");
        }
        catch (ObjectNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Libro no encontrado\"}");
        }
        catch (OperationFailedException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Operacion ha fallado\"}");
        }
        catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ISBN, número de páginas o genero inválido\"}");
        }

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");

        try {
            int id = Integer.parseInt(idString);
            libroDAO.delete(id);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID inválido\"}");
        }
        catch (ObjectNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Libro no encontrado\"}");
        }
        catch (OperationFailedException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Operacion ha fallado\"}");
        }

    }
}
