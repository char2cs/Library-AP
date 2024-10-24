package com.Libreria.Servlet;

import com.Libreria.DAO.ClienteDAO;
import com.Libreria.Domain.Cliente;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Factory.DAOFactory;
import com.Libreria.Service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/clientes")
public class ClienteController extends HttpServlet {
    private final static ClienteDAO clienteDAO;
    private final static ClienteService clienteService;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        clienteDAO = DAOFactory.getClienteDAO(DAOFactory.DatabaseType.MYSQL);
        clienteService = new ClienteService(
                clienteDAO,
                DAOFactory.getPrestamoDAO(DAOFactory.DatabaseType.MYSQL)
        );
    }

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        List<Cliente> clientes = clienteService.getAll();
        if (clientes == null)
            clientes = new ArrayList<>();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(clientes));
    }

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String email = request.getParameter("email");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String dniString = request.getParameter("dni");
        String telefonoString = request.getParameter("telefono");

        Cliente newClient = new Cliente();
        newClient.setEmail(email);
        newClient.setNombre(nombre);
        newClient.setApellido(apellido);

        try {
            int dni = Integer.parseInt(dniString);
            int telefono = Integer.parseInt(telefonoString);
            newClient.setDni(dni);
            newClient.setTelefono(telefono);
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Número de teléfono o DNI inválido\"}");
            return;
        }

        try {
            clienteDAO.create(newClient);
            response.setStatus(HttpServletResponse.SC_CREATED); // 201
            response.getWriter().write("{\"message\":\"Cliente creado exitosamente\"}");
        }
        catch (ObjectAlreadyExistsException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
            response.getWriter().write("{\"error\":\"El cliente ya existe\"}");
        }
    }

    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String idString = request.getParameter("id");
        try {
            int id = Integer.parseInt(idString);

            Cliente existingClient = clienteDAO.get(id);
            if (existingClient == null)
            {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                response.getWriter().write("{\"error\":\"Cliente no encontrado\"}");
                return;
            }

            String email = request.getParameter("email");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String dniString = request.getParameter("dni");
            String telefonoString = request.getParameter("telefono");

            // Update client details
            existingClient.setEmail(email);
            existingClient.setNombre(nombre);
            existingClient.setApellido(apellido);
            existingClient.setDni(Integer.parseInt(dniString));
            existingClient.setTelefono(Integer.parseInt(telefonoString));

            clienteDAO.update(existingClient);

            response.setStatus(HttpServletResponse.SC_OK); // 200
            response.getWriter().write("{\"message\":\"Cliente actualizado exitosamente\"}");
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Número de teléfono o DNI inválido\"}");
        }
        catch (ObjectNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            response.getWriter().write("{\"error\":\"Cliente no encontrado\"}");
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
            clienteDAO.delete(id);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID inválido\"}");
        }
        catch (ObjectNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            response.getWriter().write("{\"error\":\"Cliente no encontrado\"}");
        }
        catch (OperationFailedException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Operacion ha fallado\"}");
        }

    }
}
