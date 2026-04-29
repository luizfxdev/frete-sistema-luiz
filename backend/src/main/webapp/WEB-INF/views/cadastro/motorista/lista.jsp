<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Motoristas" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-2xl font-semibold text-slate-900">Motoristas</h1>
        <a href="${pageContext.request.contextPath}/motoristas/novo" class="btn btn-primary">+ Novo motorista</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/motoristas" class="card mb-4">
        <div class="flex gap-3">
            <input type="text" name="filtro" value="<c:out value='${filtro}'/>"
                   placeholder="Filtrar por nome ou CPF" class="flex-1 px-3 py-2 border border-slate-300 rounded text-sm">
            <button type="submit" class="btn btn-secondary">Filtrar</button>
        </div>
    </form>

    <table class="table">
        <thead>
        <tr>
            <th>Nome</th>
            <th>CPF</th>
            <th>CNH</th>
            <th>Validade</th>
            <th>Vínculo</th>
            <th>Status</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="m" items="${motoristas}">
            <tr>
                <td><c:out value="${m.nome}"/></td>
                <td><c:out value="${m.cpf}"/></td>
                <td>${m.cnhCategoria} - ${m.cnhNumero}</td>
                <td>
                    ${m.cnhValidade}
                    <c:if test="${m.cnhVencida}">
                        <span class="badge badge-suspenso ml-2">Vencida</span>
                    </c:if>
                </td>
                <td>${m.tipoVinculo}</td>
                <td>
                    <span class="badge ${fn:toLowerCase(m.status) == 'ativo' ? 'badge-ativo' : (fn:toLowerCase(m.status) == 'inativo' ? 'badge-inativo' : 'badge-suspenso')}">
                        ${m.status}
                    </span>
                </td>
                <td class="text-right">
                    <a href="${pageContext.request.contextPath}/motoristas/editar/${m.id}" class="btn btn-sm btn-secondary">Editar</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty motoristas}">
            <tr><td colspan="7" class="text-center py-8 text-slate-500">Nenhum motorista cadastrado.</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="pagination">
        <c:forEach begin="1" end="${totalPaginas}" var="p">
            <c:choose>
                <c:when test="${p == pagina}"><span class="active">${p}</span></c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/motoristas?pagina=${p}<c:if test='${not empty filtro}'>&filtro=<c:out value='${filtro}'/></c:if>">${p}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>