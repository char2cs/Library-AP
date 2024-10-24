<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="/WEB-INF/head.jsp" />
<body>
    <jsp:include page="/WEB-INF/header.jsp" />

    <%
        Cookie[] cookies = request.getCookies();
        String perfilType = null;

        if (cookies != null)
            for (Cookie cookie : cookies)
                if ("perfil_type".equals(cookie.getName()))
                {
                    perfilType = cookie.getValue();
                    break;
                }
    %>

    <% if ("cliente".equals(perfilType)) { %>
        <h1>Bienvenido, Cliente!</h1>
        <jsp:include page="index/buscarLibros.jsp"/>
    <% }
        else if ("administrativo".equals(perfilType)) { %>
        <h1>Bienvenido, Administrativo!</h1>
        <jsp:include page="index/administrativo.jsp"/>
    <% }
        else { %>
        <h1>Bienvenido, visitante!</h1>
        <p><%=perfilType%></p>
    <% } %>
</body>
</html>