<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Clientes" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-2xl font-semibold text-slate-900">Clientes</h1>
        <a href="${pageContext.request.contextPath}/clientes/novo" class="btn btn-primary">+ Novo cliente</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/clientes" class="card mb-4">
        <div class="flex gap-3">
            <input type="text" name="filtro" value="<c:out value='${filtro}'/>"
                   placeholder="Filtrar por razão social ou nome fantasia"
                   class="flex-1 px-3 py-2 border border-slate-300 rounded text-sm">
            <button type="submit" class="btn btn-secondary">Filtrar</button>
        </div>
    </form>

    <table class="table">
        <thead>
        <tr>
            <th>Razão social</th>
            <th>CNPJ</th>
            <th>Tipo</th>
            <th>Município/UF</th>
            <th>Status</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${clientes}">
            <tr>
                <td><c:out value="${c.razaoSocial}"/></td>
                <td><c:out value="${c.cnpj}"/></td>
                <td><c:out value="${c.tipo}"/></td>
                <td><c:out value="${c.municipio}"/>/<c:out value="${c.uf}"/></td>
                <td>
                    <span class="badge ${c.status == 'ATIVO' ? 'badge-ativo' : 'badge-inativo'}">
                        <c:out value="${c.status}"/>
                    </span>
                </td>
                <td class="text-right">
                    <a href="${pageContext.request.contextPath}/clientes/editar/${c.id}" class="btn btn-sm btn-secondary">Editar</a>
                    <form method="post" action="${pageContext.request.contextPath}/clientes/excluir/${c.id}"
                          data-confirm="Excluir cliente?" class="inline">
                        <button type="submit" class="btn btn-sm btn-danger">Excluir</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty clientes}">
            <tr><td colspan="6" class="text-center py-8 text-slate-500">Nenhum cliente cadastrado.</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="pagination">
        <c:forEach begin="1" end="${totalPaginas}" var="p">
            <c:choose>
                <c:when test="${p == pagina}"><span class="active">${p}</span></c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/clientes?pagina=${p}<c:if test='${not empty filtro}'>&filtro=<c:out value='${filtro}'/></c:if>">${p}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>