package com.Libreria.Service;

import com.Libreria.DAO.ClienteDAO;
import com.Libreria.DAO.InstanciaDAO;
import com.Libreria.DAO.PrestamoDAO;
import com.Libreria.Domain.Instancia;
import com.Libreria.Domain.Libro;
import com.Libreria.Domain.Prestamo;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrestamoService implements PrestamoDAO {
    private final PrestamoDAO prestamoDAO;
    private final InstanciaDAO instanciaDAO;
    private final ClienteDAO clienteDAO;

    public PrestamoService(
            PrestamoDAO prestamoDAO,
            InstanciaDAO instanciaDAO,
            ClienteDAO clienteDAO
    ) {
        this.prestamoDAO = prestamoDAO;
        this.instanciaDAO = instanciaDAO;
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void create(Prestamo prestamo) throws ObjectAlreadyExistsException {
        prestamoDAO.create(prestamo);

        try {
            prestamoDAO.updatePrestamoLibro(prestamo);
            prestamoDAO.updateClient(prestamo);
        }
        catch (OperationFailedException e) {
            throw new RuntimeException(e);
        }
        catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Prestamo get(Integer id) throws ObjectNotFoundException {
        Prestamo prestamo = prestamoDAO.get(id);

        prestamo.setLibros(
                instanciaDAO.obtenerInstanciasPorPrestamo(id)
        );

        prestamo.setCliente(
                clienteDAO.obtenerClientePorPrestamo(id)
        );

        return prestamo;
    }

    @Override
    public boolean update(Prestamo prestamo) throws ObjectNotFoundException, OperationFailedException {
        prestamoDAO.updatePrestamoLibro(prestamo);
        prestamoDAO.updateClient(prestamo);

        return prestamoDAO.update(prestamo);
    }

    @Override
    public boolean delete(Integer id) throws ObjectNotFoundException, OperationFailedException {
        return prestamoDAO.delete(id);
    }

    @Override
    public List<Prestamo> getAll() {
        List<Prestamo> prestamos = prestamoDAO.getAll();

        for (Prestamo prestamo : prestamos)
        {
            try {
                prestamo.setCliente(
                        clienteDAO.obtenerClientePorPrestamo(
                                prestamo.getId()
                        )
                );
            }
            catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }

            prestamo.setLibros(
                    instanciaDAO.obtenerInstanciasPorPrestamo(
                            prestamo.getId()
                    )
            );
        }

        return prestamos;
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorCliente(Integer idCliente) {
        List<Prestamo> prestamos = prestamoDAO.obtenerPrestamosPorCliente(idCliente);

        for (Prestamo prestamo : prestamos)
        {
            try {
                prestamo.setCliente(
                        clienteDAO.obtenerClientePorPrestamo(
                                prestamo.getId()
                        )
                );
            }
            catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }

            prestamo.setLibros(
                    instanciaDAO.obtenerInstanciasPorPrestamo(
                            prestamo.getId()
                    )
            );
        }

        return prestamos;
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorInstancia(Integer idInstancia) {
        List<Prestamo> prestamos = prestamoDAO.obtenerPrestamosPorInstancia(idInstancia);

        for (Prestamo prestamo : prestamos)
        {
            try {
                prestamo.setCliente(
                        clienteDAO.obtenerClientePorPrestamo(
                                prestamo.getId()
                        )
                );
            }
            catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }

            prestamo.setLibros(
                    instanciaDAO.obtenerInstanciasPorPrestamo(
                            prestamo.getId()
                    )
            );
        }

        return prestamos;
    }

    /**
     * Logica del negocio...
     */
    public void crearPrestamo(
            Prestamo prestamo,
            List<Libro> libros
    ) throws ObjectAlreadyExistsException {
        prestamo.setFechaFin(
                Date.from(prestamo.getFechaInicio().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .plusDays(5)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant())
        );

        List<Instancia> instancias = new ArrayList<>();

        for ( Libro libro : libros )
            instancias.add(
                    instanciaDAO.obtenerInstanciasDisponibles(
                        libro.getId()
                    )
            );

        prestamo.setLibros(
            instancias
        );

        this.create(prestamo);
    }

    /**
     * Estos metodos estan vacios ya que no son de utilidad al usar
     * el servicio
     */

    @Override
    public void updatePrestamoLibro(Prestamo prestamo) throws OperationFailedException {}

    @Override
    public void updateClient(Prestamo prestamo) throws ObjectNotFoundException, OperationFailedException {}
}
