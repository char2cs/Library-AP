package com.Libreria.Factory;

import com.Libreria.DAO.*;
import com.Libreria.Imp.MySQL.*;

public class DAOFactory {

    public enum DatabaseType {
        MYSQL,
    }

    public static ClienteDAO getClienteDAO(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return new ClienteMySQL();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }

    public static AdministrativoDAO getAdministrativoDAO(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return new AdministrativoMySQL();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }

    public static PrestamoDAO getPrestamoDAO(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return new PrestamoMySQL();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }

    public static LibroDAO getLibroDAO(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return new LibroMySQL();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }

    public static AutorDAO getAutorDAO(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return new AutorMySQL();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }

    public static EditorialDAO getEditorialDAO(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return new EditorialMySQL();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }

    public static InstanciaDAO getInstanciaDAO(DatabaseType type) {
        switch (type) {
            case MYSQL:
                return new InstanciaMySQL();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }
}

