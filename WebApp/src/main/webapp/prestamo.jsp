<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<jsp:include page="/WEB-INF/head.jsp" />
<body>
<jsp:include page="/WEB-INF/header.jsp" />

<main>
    <h1>Registrar Préstamo</h1>

    <form id="prestamoForm">
        <div>
            <label for="clienteId">Cliente:</label>
            <select name="clienteId" id="clienteId" required>
                <option value="">Selecciona un cliente</option>
            </select>
        </div>

        <div>
            <label for="libroId">Libro:</label>
            <select name="libroId" id="libroId" multiple required>
                <option value="">Selecciona uno o más libros</option>
            </select>
        </div>

        <div>
            <button type="submit">Registrar Préstamo</button>
        </div>
    </form>
</main>

<script>
    fetch('${pageContext.request.contextPath}/api/clientes')
        .then(response => response.json())
        .then(clientes => {
            const clienteSelect = document.getElementById('clienteId');
            clientes.forEach(cliente => {
                const option = document.createElement('option');
                option.value = cliente.id;
                option.textContent = cliente.nombre;
                clienteSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error al cargar los clientes:', error));

    fetch('${pageContext.request.contextPath}/api/libros')
        .then(response => response.json())
        .then(libros => {
            const libroSelect = document.getElementById('libroId');
            libros.forEach(libro => {
                const option = document.createElement('option');
                option.value = libro.id;
                option.textContent = libro.titulo;
                libroSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error al cargar los libros:', error));

    document.getElementById('prestamoForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const clienteId = document.getElementById('clienteId').value;
        const libroSelect = document.getElementById('libroId');
        const selectedLibros = Array.from(libroSelect.selectedOptions).map(option => ({
            id: option.value
        }));

        const prestamoData = {
            cliente_id: clienteId,
            libros: selectedLibros
        };

        fetch('${pageContext.request.contextPath}/api/prestamo', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(prestamoData)
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Error al crear el préstamo');
            })
            .then(data => {
                alert('Préstamo creado con éxito');
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al crear el préstamo');
            });
    });
</script>

</body>
</html>