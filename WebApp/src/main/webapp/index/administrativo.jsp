<style>
    div {
        height: 100%;
        width: 100%;

        display: flex;
        gap: 1rem;
        justify-content: center;
        align-items: center;
    }
</style>

<div>
    <a href="${pageContext.request.contextPath}/autor.jsp">
        <button>
            CRUD Autor
        </button>
    </a>
    <a href="${pageContext.request.contextPath}/editorial.jsp">
        <button>
            CRUD Editorial
        </button>
    </a>
    <a href="${pageContext.request.contextPath}/libro.jsp">
        <button>
            CRUD Libro
        </button>
    </a>
</div>