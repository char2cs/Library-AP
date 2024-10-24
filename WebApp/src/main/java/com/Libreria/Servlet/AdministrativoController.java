package com.Libreria.Servlet;

import com.Libreria.DAO.AdministrativoDAO;
import com.Libreria.Domain.Administrativo;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Factory.DAOFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/administrativos")
public class AdministrativoController extends HttpServlet {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static AdministrativoDAO administrativoDAO;

    static {
        administrativoDAO = DAOFactory.getAdministrativoDAO(
                DAOFactory.DatabaseType.MYSQL
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Administrativo> administrativos = administrativoDAO.getAll();
        if (administrativos == null)
            administrativos = new ArrayList<>();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(administrativos));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Administrativo newAdmin = objectMapper.readValue(request.getReader(), Administrativo.class);

        try {
            administrativoDAO.create(newAdmin);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
        catch (ObjectAlreadyExistsException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "Administrativo already exists");
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Data invalida");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            administrativoDAO.delete(id);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        catch (ObjectNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Administrativo no existe");
        }
        catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalido ID");
        }
        catch (OperationFailedException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operacion fallida");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Administrativo updatedAdmin = objectMapper.readValue(request.getReader(), Administrativo.class);
            updatedAdmin.setId(id);

            administrativoDAO.update(updatedAdmin);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (ObjectNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Administrativo no existe");
        }
        catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalido ID");
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operacion fallida");
        }
    }
}
