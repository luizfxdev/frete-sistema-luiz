<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Fretes" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-2xl font-semibold text-slate-900">Fretes</h1>
        <a href="${pageContext.request.contextPath}/fretes/novo" class="btn btn-primary">+ Novo frete</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/fretes" class="card mb-4">
        <div class="flex gap-3">
            <input type="text" name="filtro" value="<c:out value='${filtro}'/>"
                   placeholder="Número, remetente ou destinatário"
                   class="flex-1 px-3 py-2 border border-slate-300 rounded text-sm">
            <select name="statusFiltro" class="px-3 py-2 border border-slate-300 rounded text-sm">
                <option value="">Todos os status</option>
                <c:forEach var="s" items="${statusList}">
                    <option value="${s}" ${statusFiltro == s.name() ? 'selected' : ''}>${s}</option>
                </c:forEach>
            </select>
            <button type="submit" class="btn btn-secondary">Filtrar</button>
        </div>
    </form>

    <table class="table">
        <thead>
        <tr>
            <th>Número</th>
            <th>Remetente</th>
            <th>Destinatário</th>
            <th>Motorista</th>
            <th>Origem → Destino</th>
            <th>Emissão</th>
            <th>Status</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="f" items="${fretes}">
            <c:set var="badgeCls" value="badge-${fn:toLowerCase(fn:replace(f.status, '_', '-'))}"/>
            <tr>
                <td class="font-mono">${f.numero}</td>
                <td><c:out value="${f.remetenteRazaoSocial}"/></td>
                <td><c:out value="${f.destinatarioRazaoSocial}"/></td>
                <td><c:out value="${f.motoristaNome}"/></td>
                <td>${f.municipioOrigem}/${f.ufOrigem} → ${f.municipioDestino}/${f.ufDestino}</td>
                <td>${f.dataEmissao}</td>
                <td><span class="badge ${badgeCls}">${f.status}</span></td>
                <td class="text-right">
                    <a href="${pageContext.request.contextPath}/fretes/detalhe/${f.id}" class="btn btn-sm btn-secondary">Detalhe</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty fretes}">
            <tr><td colspan="8" class="text-center py-8 text-slate-500">Nenhum frete encontrado.</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="pagination">
        <c:forEach begin="1" end="${totalPaginas}" var="p">
            <c:choose>
                <c:when test="${p == pagina}"><span class="active">${p}</span></c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/fretes?pagina=${p}<c:if test='${not empty filtro}'>&filtro=<c:out value='${filtro}'/></c:if><c:if test='${not empty statusFiltro}'>&statusFiltro=<c:out value='${statusFiltro}'/></c:if>">${p}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>