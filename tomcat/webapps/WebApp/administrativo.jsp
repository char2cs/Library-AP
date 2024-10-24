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

<h2>Administrativos: <span id="administradoresCount">0</span></h2>
<table id="administradoresTable">
    <thead>
    <tr>
        <th>ID</th>
        <th>Legajo</th>
        <th>Nombre</th>
        <th>Apellido</th>
        <th>DNI</th>
        <th>Teléfono</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody id="administradoresBody">
    </tbody>
</table>

<h2>Agregar Nuevo Administrativo</h2>
<form id="addAdministradorForm">
    <input type="text" name="nombre" placeholder="Nombre" required />
    <input type="text" name="apellido" placeholder="Apellido" required />
    <input type="text" name="dni" placeholder="DNI" required />
    <input type="text" name="telefono" placeholder="Teléfono" required />
    <input type="text" name="legajo" placeholder="Legajo" required />
    <button type="submit">Agregar Administrativo</button>
</form>

<script>
    function fetchAdministradores() {
        fetch('/WebApp/api/administrativos')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                const administradoresBody = document.getElementById('administradoresBody');
                const administradoresCount = document.getElementById('administradoresCount');
                administradoresBody.innerHTML = '';
                data.forEach(administrador => {
                    const row = document.createElement('tr');

                    row.innerHTML = `
                        <td>` + administrador.id + `</td>
                        <td>` + administrador.email + `</td>
                        <td>` + administrador.nombre + `</td>
                        <td>` + administrador.apellido + `</td>
                        <td>` + administrador.dni + `</td>
                        <td>` + administrador.telefono + `</td>
                        <td>
                            <button onclick="deleteCliente(` + administrador.id + `)">Eliminar</button>
                            <button onclick="useAdministrativo('` + administrador.id + `', '` + administrador.nombre + `')">Iniciar Session</button>
                        </td>
                    `;
                    administradoresBody.appendChild(row);
                });
                administradoresCount.textContent = data.length;
            })
            .catch(error => console.error('Error fetching administradores:', error));
    }

    function deleteAdministrador(id) {
        fetch(`/WebApp/api/administrativos?id=${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok)
                    fetchAdministradores();
                else
                    console.error('Error deleting administrador');
            })
            .catch(error => console.error('Error:', error));
    }

    document.getElementById('addAdministradorForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);

        fetch('/WebApp/api/administrativos', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.ok)
            {
                fetchAdministradores();
                this.reset();
            }
            else
                console.error('Error adding administrador');
        }).catch(error => console.error('Error:', error));
    });

    function useAdministrativo(
        id,
        nombre
    ) {
        const clienteData = {
            id: id,
            nombre: nombre,
            type: 'administrativo'
        };

        fetch('/WebApp/api/login', {
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

    fetchAdministradores();
</script>
</body>
</html>