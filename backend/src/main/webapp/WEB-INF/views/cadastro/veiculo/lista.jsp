<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Veículos" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-2xl font-semibold text-slate-900">Veículos</h1>
        <a href="${pageContext.request.contextPath}/veiculos/novo" class="btn btn-primary">+ Novo veículo</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/veiculos" class="card mb-4">
        <div class="flex gap-3">
            <input type="text" name="filtro" value="<c:out value='${filtro}'/>"
                   placeholder="Filtrar por placa" class="flex-1 px-3 py-2 border border-slate-300 rounded text-sm">
            <button type="submit" class="btn btn-secondary">Filtrar</button>
        </div>
    </form>

    <table class="table">
        <thead>
        <tr>
            <th>Placa</th>
            <th>Tipo</th>
            <th>Ano</th>
            <th>Capacidade (kg)</th>
            <th>Status</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="v" items="${veiculos}">
            <c:set var="statusClass" value="badge-disponivel"/>
            <c:if test="${v.status == 'EM_VIAGEM'}"><c:set var="statusClass" value="badge-em-viagem"/></c:if>
            <c:if test="${v.status == 'EM_MANUTENCAO'}"><c:set var="statusClass" value="badge-em-manutencao"/></c:if>
            <tr>
                <td class="font-mono"><c:out value="${v.placa}"/></td>
                <td>${v.tipo}</td>
                <td>${v.anoFabricacao}</td>
                <td>${v.capacidadeKg}</td>
                <td><span class="badge ${statusClass}">${v.status}</span></td>
                <td class="text-right">
                    <a href="${pageContext.request.contextPath}/veiculos/editar/${v.id}" class="btn btn-sm btn-secondary">Editar</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty veiculos}">
            <tr><td colspan="6" class="text-center py-8 text-slate-500">Nenhum veículo cadastrado.</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="pagination">
        <c:forEach begin="1" end="${totalPaginas}" var="p">
            <c:choose>
                <c:when test="${p == pagina}"><span class="active">${p}</span></c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/veiculos?pagina=${p}<c:if test='${not empty filtro}'>&filtro=<c:out value='${filtro}'/></c:if>">${p}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>