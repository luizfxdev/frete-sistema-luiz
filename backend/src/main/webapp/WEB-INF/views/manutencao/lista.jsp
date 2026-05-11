<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Manutenções" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>
<main class="app-content">
<div class="flex items-center justify-between mb-6">
<h1 class="text-2xl font-semibold text-slate-900">Manutenções de veículos</h1>
<a href="${pageContext.request.contextPath}/manutencoes/novo" class="btn btn-primary">+ Nova manutenção</a>
</div>
<c:if test="${not empty erro}">
<div class="alert alert-error"><c:out value="${erro}"/></div>
</c:if>
<form method="get" action="${pageContext.request.contextPath}/manutencoes" class="card mb-4">
<div class="flex gap-3">
<input type="text" name="filtro" value="<c:out value='${filtro}'/>"
placeholder="Filtrar por placa" class="flex-1 px-3 py-2 border border-slate-300 rounded text-sm">
<button type="submit" class="btn btn-secondary">Filtrar</button>
</div>
</form>
<table class="table">
<thead>
<tr>
<th>Veículo</th>
<th>Tipo</th>
<th>Início</th>
<th>Fim</th>
<th>Custo (R$)</th>
<th></th>
</tr>
</thead>
<tbody>
<c:forEach var="m" items="${manutencoes}">
<tr>
<td class="font-mono">${m.veiculoPlaca}</td>
<td>${m.tipo}</td>
<td>${m.dataInicio}</td>
<td><c:out value="${m.dataFim}" default="—"/></td>
<td><c:out value="${m.custo}" default="—"/></td>
<td class="text-right">
<a href="${pageContext.request.contextPath}/manutencoes/editar/${m.id}" class="btn btn-sm btn-secondary">Editar</a>
<form method="post" action="${pageContext.request.contextPath}/manutencoes/excluir/${m.id}" style="display:inline">
<button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Confirmar exclusão da manutenção?')">Excluir</button>
</form>
</td>
</tr>
</c:forEach>
<c:if test="${empty manutencoes}">
<tr><td colspan="6" class="text-center py-8 text-slate-500">Nenhuma manutenção registrada.</td></tr>
</c:if>
</tbody>
</table>
<div class="pagination">
<c:forEach begin="1" end="${totalPaginas}" var="p">
<c:choose>
<c:when test="${p == pagina}"><span class="active">${p}</span></c:when>
<c:otherwise><a href="${pageContext.request.contextPath}/manutencoes?pagina=${p}">${p}</a></c:otherwise>
</c:choose>
</c:forEach>
</div>
</main>
<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>