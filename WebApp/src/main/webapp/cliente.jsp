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

<h2>Clientes: <span id="clientesCount">0</span></h2>
<table id="clientesTable">
    <thead>
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Nombre</th>
        <th>Apellido</th>
        <th>DNI</th>
        <th>Teléfono</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody id="clientesBody">
    </tbody>
</table>

<h2>Agregar Nuevo Cliente</h2>
<form id="addClienteForm">
    <input type="email" name="email" placeholder="Email" required />
    <input type="text" name="nombre" placeholder="Nombre" required />
    <input type="text" name="apellido" placeholder="Apellido" required />
    <input type="text" name="dni" placeholder="DNI" required />
    <input type="text" name="telefono" placeholder="Teléfono" required />
    <button type="submit">Agregar Cliente</button>
</form>

<script>
    function fetchClientes() {
        fetch('${pageContext.request.contextPath}/api/clientes')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                const clientesBody = document.getElementById('clientesBody');
                const clientesCount = document.getElementById('clientesCount');
                clientesBody.innerHTML = '';

                console.log( data );

                data.forEach(cliente => {
                    const row = document.createElement('tr');

                    row.innerHTML = `
                        <td>` + cliente.id + `</td>
                        <td>` + cliente.email + `</td>
                        <td>` + cliente.nombre + `</td>
                        <td>` + cliente.apellido + `</td>
                        <td>` + cliente.dni + `</td>
                        <td>` + cliente.telefono + `</td>
                        <td>
                            <button onclick="deleteCliente(` + cliente.id + `)">Eliminar</button>
                            <button onclick="useCliente('` + cliente.id + `', '` + cliente.nombre + `')">Iniciar Session</button>
                        </td>
                    `;
                    clientesBody.appendChild(row);

                });
                clientesCount.textContent = data.length;
            })
            .catch(error => console.error('Error fetching clientes:', error));
    }

    function deleteCliente(id) {
        fetch(`${pageContext.request.contextPath}/api/clientes?id=${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok)
                    fetchClientes();
                else
                    console.error('Error deleting client');
            })
            .catch(error => console.error('Error:', error));
    }

    function useCliente(
        id,
        nombre
    ) {
        const clienteData = {
            id: id,
            nombre: nombre,
            type: 'cliente'
        };

        fetch('${pageContext.request.contextPath}/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(clienteData)
        })
            .then(response => {
                if (response.ok)
                    console.log('Login successful');
                else
                    console.error('Login failed');
            })
            .catch(error => console.error('Error logging in:', error));
    }

    document.getElementById('addClienteForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);

        fetch('${pageContext.request.contextPath}/api/clientes', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.ok)
            {
                fetchClientes();
                this.reset();
            }
            else
                console.error('Error adding client');
        }).catch(error => console.error('Error:', error));
    });

    fetchClientes();
</script>
</body>
</html>
