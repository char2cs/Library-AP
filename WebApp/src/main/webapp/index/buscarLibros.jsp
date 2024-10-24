<script>
    function searchBooks() {
        const searchKey = document.getElementById('searchKey').value;

        fetch(`/WebApp/api/libros?searchKey=`+encodeURIComponent(searchKey))
            .then(response => {
                if (!response.ok)
                    throw new Error('Error al buscar libros');

                return response.json();
            })
            .then(libros => {
                const tableBody = document.getElementById('resultsBody');
                tableBody.innerHTML = '';

                if (libros.length === 0)
                    tableBody.innerHTML = '<tr><td colspan="5">No se encontraron libros.</td></tr>';
                else {
                    libros.forEach(libro => {
                        const row = `<tr>
                                <td>` + libro.titulo + `</td>
                                <td>` + libro.edicion + `</td>
                                <td>` + libro.paginas + `</td>
                                <td>` + libro.lsbn + `</td>
                                <td>` + libro.genero + `</td>
                            </tr>`;
                        tableBody.innerHTML += row;
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Hubo un error al buscar los libros.');
            });

        return;
    }
</script>

<style>
    h2 {
        width: 100%;
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

<h2>Buscar Libros</h2>

<div>
    <label for="searchKey">Buscar:</label>
    <input type="text" id="searchKey" placeholder="Título o edición">
    <button type="button" onclick="searchBooks()">Buscar</button>
</div>

<table>
    <thead>
    <tr>
        <th>Titulo</th>
        <th>Edicion</th>
        <th>Paginas</th>
        <th>ISBN</th>
        <th>Genero</th>
    </tr>
    </thead>
    <tbody id="resultsBody">
    <tr>
        <td colspan="5">Introduce un termino de busqueda y presiona "Buscar".</td>
    </tr>
    </tbody>
</table>
