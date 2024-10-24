package com.Libreria.Servlet;

import com.Libreria.DAO.PrestamoDAO;
import com.Libreria.Domain.Cliente;
import com.Libreria.Domain.Libro;
import com.Libreria.Domain.Prestamo;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Factory.DAOFactory;
import com.Libreria.Service.ClienteService;
import com.Libreria.Service.LibroService;
import com.Libreria.Service.PrestamoService;
import com.Libreria.Utils.PrestamoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/prestamo")
public class PrestamoController extends HttpServlet {
    private final static PrestamoDAO prestamoDAO;
    private final static PrestamoService prestamoService;
    private final static ClienteService clienteService;
    private final static LibroService libroService;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        prestamoDAO = DAOFactory.getPrestamoDAO(DAOFactory.DatabaseType.MYSQL);
        prestamoService = new PrestamoService(
                prestamoDAO,
                DAOFactory.getInstanciaDAO(DAOFactory.DatabaseType.MYSQL),
                DAOFactory.getClienteDAO(DAOFactory.DatabaseType.MYSQL)
        );
        clienteService = new ClienteService(
                DAOFactory.getClienteDAO(DAOFactory.DatabaseType.MYSQL),
                prestamoDAO
        );
        libroService = new LibroService(
                DAOFactory.getLibroDAO(DAOFactory.DatabaseType.MYSQL),
                DAOFactory.getAutorDAO(DAOFactory.DatabaseType.MYSQL),
                DAOFactory.getEditorialDAO(DAOFactory.DatabaseType.MYSQL),
                DAOFactory.getInstanciaDAO(DAOFactory.DatabaseType.MYSQL)
        );
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        response.setContentType("application/json");

        try {
            if (idParam != null)
            {
                Integer id = Integer.parseInt(idParam);
                Prestamo prestamo = prestamoService.get(id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(prestamo));
            }
            else {
                List<Prestamo> prestamos = prestamoService.getAll();
                if (prestamos == null) prestamos = new ArrayList<>();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(prestamos));
            }
        }
        catch (ObjectNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Préstamo no encontrado\"}");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error al procesar la solicitud\"}");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PrestamoRequest prestamoRequest = objectMapper.readValue(request.getReader(), PrestamoRequest.class);

            Prestamo newPrestamo = new Prestamo();

            Cliente cliente = clienteService.get(prestamoRequest.getCliente_id());
            newPrestamo.setCliente(cliente);

            List<Libro> libros = new ArrayList<>();
            for (Libro libro : prestamoRequest.getLibros())
                libros.add(
                        libroService.get(libro.getId())
                );

            prestamoService.crearPrestamo(newPrestamo, libros);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"message\":\"Préstamo creado con éxito\"}");
        }
        catch (ObjectAlreadyExistsException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"error\":\"El préstamo ya existe\"}");
        }
        catch (ObjectNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Cliente o libro no encontrado\"}");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error al crear el préstamo\"}");
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        try {
            Integer id = Integer.parseInt(idParam);
            Prestamo prestamoExistente = prestamoService.get(id);

            if (prestamoExistente == null)
            {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Préstamo no encontrado\"}");
                return;
            }

            Prestamo updatedPrestamo = objectMapper.readValue(request.getReader(), Prestamo.class);
            updatedPrestamo.setId(id);

            if (prestamoService.update(updatedPrestamo)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\":\"Préstamo actualizado con éxito\"}");
            }
            else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Error al actualizar el préstamo\"}");
            }
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID inválido\"}");
        }
        catch (ObjectNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Préstamo no encontrado\"}");
        }
        catch (OperationFailedException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error en la operación\"}");
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        try {
            Integer id = Integer.parseInt(idParam);
            if (prestamoService.delete(id))
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Préstamo no encontrado\"}");
            }
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID inválido\"}");
        }
        catch (ObjectNotFoundException | OperationFailedException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error al eliminar el préstamo\"}");
        }
    }
}
