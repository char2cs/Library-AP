package com.Libreria.Tests;

import com.Libreria.DAO.AdministrativoDAO;
import com.Libreria.DAO.ClienteDAO;
import com.Libreria.Domain.Administrativo;
import com.Libreria.Domain.Autor;
import com.Libreria.Domain.Cliente;
import com.Libreria.Domain.Editorial;
import com.Libreria.Exception.ObjectAlreadyExistsException;
import com.Libreria.Exception.ObjectNotFoundException;
import com.Libreria.Exception.OperationFailedException;
import com.Libreria.Factory.DAOFactory;
import com.Libreria.Imp.MySQL.AdministrativoMySQL;
import com.Libreria.Imp.MySQL.AutorMySQL;
import com.Libreria.Imp.MySQL.EditorialMySQL;

import java.util.ArrayList;

public class TestDAO {

    public static void main(String[] args) {
        testAutorMySQL();
//        testAdminsitrativoMySQL();
        testClienteMySQL();
//        testEditorialMySQL();
//        testLibroMySQL();
//        testInstanciaMySQL();
//        testPrestamoMySQL();
    }

    public static void testAutorMySQL() {
        AutorMySQL autorDAO = new AutorMySQL();

        // Crear un autor
        Autor nuevoAutor = new Autor(null, "Gabriel", "García Márquez", "Colombiano", new ArrayList<>());
        try {
            autorDAO.create(nuevoAutor);
        } catch (ObjectAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Autor creado: " + nuevoAutor.getNombre());

        // Obtener autor
        Autor autorRecuperado = null;
        try {
            autorRecuperado = autorDAO.get(nuevoAutor.getId());
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Autor recuperado: " + autorRecuperado.getNombre() + " " + autorRecuperado.getApellido());

        // Actualizar autor
        autorRecuperado.setNombre("Gabriel Updated");
        try {
            autorDAO.update(autorRecuperado);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        } catch (OperationFailedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Autor actualizado: " + autorRecuperado.getNombre());

        // Eliminar autor
//        try {
//            autorDAO.delete(nuevoAutor.getId());
//        } catch (ObjectNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (OperationFailedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Autor eliminado.");
    }

    public static void testAdminsitrativoMySQL() {
        AdministrativoDAO administrativoMySQL = DAOFactory.getAdministrativoDAO(
                DAOFactory.DatabaseType.MYSQL
        );

        try {
            administrativoMySQL.create(new Administrativo(
                    null,
                    45518340,
                    "Mateo",
                    "Urrutia",
                    1128447029,
                    23421
            ));
        }
        catch (ObjectAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    public static void testClienteMySQL() {
        ClienteDAO clienteDAO = DAOFactory.getClienteDAO(
                DAOFactory.DatabaseType.MYSQL
        );

        try {
            clienteDAO.create(new Cliente(
                    null,
                    45518341,
                    "Gordon",
                    "Freeman",
                    1128447027,
                    "gordon@rounds.com.ar",
                    null
            ));
        }
        catch (ObjectAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void testEditorialMySQL() {
//        EditorialMySQL editorialDAO = new EditorialMySQL();
//
//        // Crear una editorial
//        Editorial nuevaEditorial = new Editorial(null, "Penguin Books", "testing");
//        try {
//            editorialDAO.create(nuevaEditorial);
//        } catch (ObjectAlreadyExistsException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Editorial creada: " + nuevaEditorial.getNombre());
//
//        // Obtener editorial
//        Editorial editorialRecuperada = null;
//        try {
//            editorialRecuperada = editorialDAO.get(nuevaEditorial.getId());
//        } catch (ObjectNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Editorial recuperada: " + editorialRecuperada.getNombre());
//
//        // Actualizar editorial
//        editorialRecuperada.setNombre("Penguin Updated");
//        try {
//            editorialDAO.update(editorialRecuperada);
//        } catch (ObjectNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (OperationFailedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Editorial actualizada: " + editorialRecuperada.getNombre());
//
//        // Eliminar editorial
//        editorialDAO.delete(nuevaEditorial.getId());
//        System.out.println("Editorial eliminada.");
//    }
//
//    public static void testLibroMySQL() {
//        LibroMySQL libroDAO = new LibroMySQL();
//
//        // Crear un libro
//        Libro nuevoLibro = new Libro(null, "Cien Años de Soledad", "Primera", 417, 123456, "FICCION");
//        libroDAO.create(nuevoLibro);
//        System.out.println("Libro creado: " + nuevoLibro.getTitulo());
//
//        // Obtener libro
//        Libro libroRecuperado = libroDAO.get(nuevoLibro.getId());
//        System.out.println("Libro recuperado: " + libroRecuperado.getTitulo());
//
//        // Actualizar libro
//        libroRecuperado.setTitulo("Cien Años de Soledad - Actualizado");
//        libroDAO.update(libroRecuperado);
//        System.out.println("Libro actualizado: " + libroRecuperado.getTitulo());
//
//        // Eliminar libro
//        libroDAO.delete(nuevoLibro.getId());
//        System.out.println("Libro eliminado.");
//    }
//
//    public static void testInstanciaMySQL() {
//        InstanciaMySQL instanciaDAO = new InstanciaMySQL();
//
//        // Crear una instancia
//        Instancia nuevaInstancia = new Instancia(null, new Date());
//        instanciaDAO.create(nuevaInstancia);
//        System.out.println("Instancia creada con fecha de entrada: " + nuevaInstancia.getFechaEntrada());
//
//        // Obtener instancia
//        Instancia instanciaRecuperada = instanciaDAO.get(nuevaInstancia.getId());
//        System.out.println("Instancia recuperada con fecha de entrada: " + instanciaRecuperada.getFechaEntrada());
//
//        // Actualizar instancia
//        instanciaRecuperada.setFechaEntrada(new Date());
//        instanciaDAO.update(instanciaRecuperada);
//        System.out.println("Instancia actualizada con nueva fecha de entrada.");
//
//        // Eliminar instancia
//        instanciaDAO.delete(nuevaInstancia.getId());
//        System.out.println("Instancia eliminada.");
//    }
//
//    public static void testPrestamoMySQL() {
//        PrestamoMySQL prestamoDAO = new PrestamoMySQL();
//
//        // Crear un préstamo
//        Prestamo nuevoPrestamo = new Prestamo(null, new ArrayList<>(), new Cliente(), new Date(), new Date());
//        prestamoDAO.create(nuevoPrestamo);
//        System.out.println("Préstamo creado con fecha de inicio: " + nuevoPrestamo.getFechaInicio());
//
//        // Obtener préstamo
//        Prestamo prestamoRecuperado = prestamoDAO.get(nuevoPrestamo.getId());
//        System.out.println("Préstamo recuperado con fecha de inicio: " + prestamoRecuperado.getFechaInicio());
//
//        // Actualizar préstamo
//        prestamoRecuperado.setFechaFin(new Date());
//        prestamoDAO.update(prestamoRecuperado);
//        System.out.println("Préstamo actualizado con nueva fecha de fin.");
//
//        // Eliminar préstamo
//        prestamoDAO.delete(nuevoPrestamo.getId());
//        System.out.println("Préstamo eliminado.");
//    }
}

