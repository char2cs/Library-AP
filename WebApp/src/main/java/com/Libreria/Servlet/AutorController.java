package com.Libreria.Servlet;

import com.Libreria.DAO.AutorDAO;
import com.Libreria.Domain.Autor;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Factory.DAOFactory;
import com.Libreria.Service.AutorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/autores")
public class AutorController extends HttpServlet {
    private final static AutorDAO autorDAO;
    private final static AutorService autorService;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        autorDAO = DAOFactory.getAutorDAO(DAOFactory.DatabaseType.MYSQL);
        autorService = new AutorService(
                autorDAO,
                DAOFactory.getLibroDAO(DAOFactory.DatabaseType.MYSQL)
        );
    }

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        List<Autor> autores = autorService.getAll();
        if (autores == null)
            autores = new ArrayList<>();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(autores));
    }

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String nacionalidad = request.getParameter("nacionalidad");

        Autor newAutor = new Autor();
        newAutor.setNombre(nombre);
        newAutor.setApellido(apellido);
        newAutor.setNacionalidad(nacionalidad);

        try {
            autorDAO.create(newAutor);
            response.setStatus(HttpServletResponse.SC_CREATED); // 201
            response.getWriter().write("{\"message\":\"Autor creado exitosamente\"}");
        }
        catch (ObjectAlreadyExistsException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
            response.getWriter().write("{\"error\":\"El autor ya existe\"}");
        }
    }

    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String idString = request.getParameter("id");
        try {
            int id = Integer.parseInt(idString);

            Autor existingAutor = autorDAO.get(id);
            if (existingAutor == null)
            {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                response.getWriter().write("{\"error\":\"Autor no encontrado\"}");
                return;
            }

            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String nacionalidad = request.getParameter("nacionalidad");

            existingAutor.setNombre(nombre);
            existingAutor.setApellido(apellido);
            existingAutor.setNacionalidad(nacionalidad);

            autorDAO.update(existingAutor);

            response.setStatus(HttpServletResponse.SC_OK); // 200
            response.getWriter().write("{\"message\":\"Autor actualizado exitosamente\"}");
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID inválido\"}");
        }
        catch (ObjectNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            response.getWriter().write("{\"error\":\"Autor no encontrado\"}");
        }
        catch (OperationFailedException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Operacion ha fallado\"}");
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");

        try {
            int id = Integer.parseInt(idString);
            autorDAO.delete(id);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID inválido\"}");
        }
        catch (ObjectNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            response.getWriter().write("{\"error\":\"Autor no encontrado\"}");
        }
        catch (OperationFailedException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Operacion ha fallado\"}");
        }

    }
}
