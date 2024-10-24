package com.Libreria.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to fetch specific settings from the 'env.properties' file.
 */
public abstract class Property {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Property.class.getClassLoader().getResourceAsStream("env.properties"))
        {
            if (input == null)
                System.out.println("UNFORESEEN CONSEQUENCES: No se encontro el archivo env.properties");

            properties.load(input);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String property) {
        return properties.getProperty(property);
    }
}
