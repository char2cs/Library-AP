<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="/WEB-INF/head.jsp" />
<body>
<jsp:include page="/WEB-INF/header.jsp" />

<%--
    TODO Manejo de cookies para que un cliente normal no pueda acceder aqui,
    algo ya hecho en el index.jsp
--%>

<style>
    h2 {
        width: min-content;
        margin: 0 auto;
    }
    table {
        width: 80%;
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
        width: 80%;
        margin: 20px auto;
        display: flex;
        flex-direction: column;
        gap: 10px;
    }
    input, select, button {
        padding: 8px;
        font-size: 16px;
    }
</style>

<h2>Libros: <span id="librosCount">0</span></h2>
<table id="librosTable">
    <thead>
    <tr>
        <th>ID</th>
        <th>ISBN</th>
        <th>Título</th>
        <th>Páginas</th>
        <th>Género</th>
        <th>Edición</th>
        <th>Autores</th>
        <th>Editoriales</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody id="librosBody">
    </tbody>
</table>

<h2>Agregar Nuevo Libro</h2>
<form id="addLibroForm">
    <input type="text" name="titulo" placeholder="Título" required />
    <input type="number" name="lsbn" placeholder="ISBN" required />
    <input type="number" name="paginas" placeholder="Páginas" required />
    <select name="genero" required>
        <option value="" disabled selected>Seleccionar Género</option>
        <option value="FICCION">Ficción</option>
        <option value="NO_FICCION">No Ficción</option>
        <option value="AVENTURA">Aventura</option>
        <option value="ROMANCE">Romance</option>
        <option value="FANTASIA">Fantasía</option>
        <option value="TERROR">Terror</option>
        <option value="SUSPENSO">Suspenso</option>
        <option value="BIOGRAFIA">Biografía</option>
        <option value="HISTORIA">Historia</option>
        <option value="CIENCIA">Ciencia</option>
        <option value="INFANTIL">Infantil</option>
        <option value="POESIA">Poesía</option>
        <option value="DRAMA">Drama</option>
        <option value="MISTERIO">Misterio</option>
        <option value="AUTO_AYUDA">Autoayuda</option>
    </select>
    <input type="text" name="edicion" placeholder="Edición" required />

    <select name="autores" id="autoresSelect" multiple required>
        <option value="" disabled selected>Cargar autores...</option>
    </select>

    <select name="editoriales" id="editorialesSelect" multiple required>
        <option value="" disabled selected>Cargar editoriales...</option>
    </select>

    <button type="submit">Agregar Libro</button>
</form>

<script>
    function fetchLibros() {
        fetch('${pageContext.request.contextPath}/api/libros')
            .then(response => response.json())
            .then(data => {
                const librosBody = document.getElementById('librosBody');
                const librosCount = document.getElementById('librosCount');
                librosBody.innerHTML = '';
                data.forEach(libro => {
                    const autores = libro.autores.map(autor => autor.nombre).join(', ');
                    const editoriales = libro.editoriales.map(editorial => editorial.nombre).join(', ');

                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${libro.id}</td>
                        <td>${libro.lsbn}</td>
                        <td>${libro.titulo}</td>
                        <td>${libro.paginas}</td>
                        <td>${libro.genero}</td>
                        <td>${libro.edicion}</td>
                        <td>${autores}</td>
                        <td>${editoriales}</td>
                        <td>
                            <button onclick="deleteLibro(${libro.id})">Eliminar</button>
                        </td>
                    `;
                    librosBody.appendChild(row);
                });
                librosCount.textContent = data.length;
            })
            .catch(error => console.error('Error fetching libros:', error));
    }

    function deleteLibro(id) {
        fetch(`${pageContext.request.contextPath}/api/libros?id=${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) fetchLibros();
                else console.error('Error deleting libro');
            })
            .catch(error => console.error('Error:', error));
    }

    function fetchAutores() {
        fetch('${pageContext.request.contextPath}/api/autores')
            .then(response => response.json())
            .then(data => {
                const autoresSelect = document.getElementById('autoresSelect');
                autoresSelect.innerHTML = '';

                data.forEach(autor => {
                    const option = document.createElement('option');
                    option.value = autor.id;
                    option.textContent = autor.nombre;
                    autoresSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error fetching autores:', error));
    }

    function fetchEditoriales() {
        fetch('${pageContext.request.contextPath}/api/editoriales')
            .then(response => response.json())
            .then(data => {
                const editorialesSelect = document.getElementById('editorialesSelect');
                editorialesSelect.innerHTML = '';

                data.forEach(editorial => {
                    const option = document.createElement('option');
                    option.value = editorial.id;
                    option.textContent = editorial.nombre;
                    editorialesSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error fetching editoriales:', error));
    }

    document.getElementById('addLibroForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);

        fetch('${pageContext.request.contextPath}/api/libros', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.ok) {
                fetchLibros();
                this.reset();
            } else console.error('Error adding libro');
        }).catch(error => console.error('Error:', error));
    });

    fetchLibros();
    fetchAutores();
    fetchEditoriales();
</script>
</body>
</html>
