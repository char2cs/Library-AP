package com.Libreria.Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader())
        {
            while ((line = reader.readLine()) != null)
                requestBody.append(line);
        }

        String bodyContent = requestBody.toString();

        String id = extractValue(bodyContent, "id");
        String nombre = extractValue(bodyContent, "nombre");
        String type = extractValue(bodyContent, "type");

        Cookie idCookie = new Cookie("perfil_id", id);
        Cookie nombreCookie = new Cookie("perfil_nombre", nombre);
        Cookie typeCookie = new Cookie("perfil_type", type);

        idCookie.setMaxAge(-1);
        nombreCookie.setMaxAge(-1);
        typeCookie.setMaxAge(-1);

        idCookie.setPath("/WebApp");
        nombreCookie.setPath("/WebApp");
        typeCookie.setPath("/WebApp");

        response.addCookie(idCookie);
        response.addCookie(nombreCookie);
        response.addCookie(typeCookie);
    }

    private String extractValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(json);
        if (m.find())
            return m.group(1);

        return "";
    }

}
