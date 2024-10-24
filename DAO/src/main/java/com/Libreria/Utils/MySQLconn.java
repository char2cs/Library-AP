package com.Libreria.Utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Usamos Hikari para controlar de manera mas dinamica
 * el abrir y cerrar la conexion de MySQL. Haciendo un
 * uso mas efectivo del conector.
 */
public abstract class MySQLconn {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s",
                Property.get("MYSQL_HOST"),
                Property.get("MYSQL_PORT"),
                Property.get("MYSQL_DATABASE"))
        );
        config.setUsername(Property.get("MYSQL_USER"));
        config.setPassword(Property.get("MYSQL_PASSWORD"));
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
