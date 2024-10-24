<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="/WEB-INF/head.jsp" />
<body>
<jsp:include page="/WEB-INF/header.jsp" />

<style>
    h2 {
        width: min-content;
        margin: 0 auto;
    }
    table {
        width: 70%;
        border-collapse: collapse;
        margin: 20px auto;
    }
    table, th, td {
        border: 1px solid black;
    }
    th, td {
        padding: 8px;
        text-align: left;
    }
    form {
        width: 70%;
        margin: 20px auto;
        display: flex;
        flex-direction: column;
        gap: 10px;
    }
    input, button {
        padding: 8px;
        font-size: 16px;
    }
</style>

<h2>Autores</h2>
<table id="autoresTable">
    <thead>
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Apellido</th>
        <th>Nacionalidad</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody id="autoresBody">
    </tbody>
</table>

<h2>Agregar Nuevo Autor</h2>
<form id="addAutorForm">
    <input type="text" name="nombre" placeholder="Nombre" required />
    <input type="text" name="apellido" placeholder="Apellido" required />
    <input type="text" name="nacionalidad" placeholder="Nacionalidad" required />
    <button type="submit">Agregar Autor</button>
</form>

<script>
    function fetchAutores() {
        fetch('${pageContext.request.contextPath}/api/autores')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                const autoresBody = document.getElementById('autoresBody');
                const autoresCount = document.getElementById('autoresCount');
                autoresBody.innerHTML = '';
                data.forEach(autor => {
                    const row = document.createElement('tr');

                    row.innerHTML = `
                        <td>` + autor.id + `</td>
                        <td>` + autor.nombre + `</td>
                        <td>` + autor.apellido + `</td>
                        <td>` + autor.nacionalidad + `</td>
                        <td>
                            <button onclick="deleteAutor(` + autor.id + `)">Eliminar</button>
                            <button onclick="editAutor(` + autor.id + `)">Editar</button>
                        </td>
                    `;
                    autoresBody.appendChild(row);
                });
                autoresCount.textContent = data.length;
            })
            .catch(error => console.error('Error fetching autores:', error));
    }

    function deleteAutor(id) {
        fetch(`${pageContext.request.contextPath}/api/autores?id=${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) fetchAutores();
                else console.error('Error deleting autor');
            })
            .catch(error => console.error('Error:', error));
    }

    document.getElementById('addAutorForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);

        fetch('${pageContext.request.contextPath}/api/autores', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.ok)
            {
                fetchAutores();
                this.reset();
            }
            else
                console.error('Error adding autor');
        }).catch(error => console.error('Error:', error));
    });

    fetchAutores();
</script>
</body>
</html>
