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

<h2>Editoriales</h2>
<table id="editorialesTable">
    <thead>
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Dirección</th>
        <th>URL</th>
        <th>Email</th>
        <th>Teléfono</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody id="editorialesBody">
    </tbody>
</table>

<h2>Agregar Nueva Editorial</h2>
<form id="addEditorialForm">
    <input type="text" name="nombre" placeholder="Nombre" required />
    <input type="text" name="direccion" placeholder="Dirección" required />
    <input type="text" name="url" placeholder="URL" required />
    <input type="email" name="email" placeholder="Email" required />
    <input type="tel" name="telefono" placeholder="Teléfono" required />
    <button type="submit">Agregar Editorial</button>
</form>

<script>
    function fetchEditoriales() {
        fetch('${pageContext.request.contextPath}/api/editoriales')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                const editorialesBody = document.getElementById('editorialesBody');
                const editorialesCount = document.getElementById('editorialesCount');
                editorialesBody.innerHTML = '';
                data.forEach(editorial => {
                    const row = document.createElement('tr');

                    row.innerHTML = `
                        <td>` + editorial.id + `</td>
                        <td>` + editorial.nombre + `</td>
                        <td>` + editorial.direccion + `</td>
                        <td>` + editorial.url + `</td>
                        <td>` + editorial.email + `</td>
                        <td>` + editorial.telefono + `</td>
                        <td>
                            <button onclick="deleteEditorial(` + editorial.id + `)">Eliminar</button>
                        </td>
                    `;
                    editorialesBody.appendChild(row);
                });
                editorialesCount.textContent = data.length;
            })
            .catch(error => console.error('Error fetching editoriales:', error));
    }

    function deleteEditorial(id) {
        fetch(`${pageContext.request.contextPath}/api/editoriales?id=${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) fetchEditoriales();
                else console.error('Error deleting editorial');
            })
            .catch(error => console.error('Error:', error));
    }

    document.getElementById('addEditorialForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);

        fetch('${pageContext.request.contextPath}/api/editoriales', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.ok)
            {
                fetchEditoriales();
                this.reset();
            }
            else
                console.error('Error adding editorial');
        }).catch(error => console.error('Error:', error));
    });

    fetchEditoriales();
</script>
</body>
</html>
