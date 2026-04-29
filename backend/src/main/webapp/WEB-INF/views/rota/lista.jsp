<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Tabela de Rotas" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-2xl font-semibold text-slate-900">Tabela de rotas</h1>
        <a href="${pageContext.request.contextPath}/rotas/novo" class="btn btn-primary">+ Nova rota</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <table class="table">
        <thead>
        <tr>
            <th>Origem</th>
            <th>Destino</th>
            <th>Valor base (R$)</th>
            <th>Valor por kg (R$)</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${rotas}">
            <tr>
                <td>${r.municipioOrigem}/${r.ufOrigem}</td>
                <td>${r.municipioDestino}/${r.ufDestino}</td>
                <td>${r.valorBase}</td>
                <td>${r.valorPorKg}</td>
                <td class="text-right">
                    <a href="${pageContext.request.contextPath}/rotas/editar/${r.id}" class="btn btn-sm btn-secondary">Editar</a>
                    <form method="post" action="${pageContext.request.contextPath}/rotas/excluir/${r.id}"
                          data-confirm="Excluir rota?" class="inline">
                        <button type="submit" class="btn btn-sm btn-danger">Excluir</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty rotas}">
            <tr><td colspan="5" class="text-center py-8 text-slate-500">Nenhuma rota cadastrada.</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="pagination">
        <c:forEach begin="1" end="${totalPaginas}" var="p">
            <c:choose>
                <c:when test="${p == pagina}"><span class="active">${p}</span></c:when>
                <c:otherwise><a href="${pageContext.request.contextPath}/rotas?pagina=${p}">${p}</a></c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>