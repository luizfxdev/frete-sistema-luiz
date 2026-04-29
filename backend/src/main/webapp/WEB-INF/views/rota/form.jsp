<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Rota" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <h1 class="text-2xl font-semibold text-slate-900 mb-6">
        ${rota.id == null ? 'Nova rota' : 'Editar rota'}
    </h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/rotas" class="card">
        <input type="hidden" name="id" value="<c:out value='${rota.id}'/>"/>

        <div class="form-grid">
            <div class="field"><label>Município origem *</label><input type="text" name="municipioOrigem" required value="<c:out value='${rota.municipioOrigem}'/>"></div>
            <div class="field"><label>UF origem *</label><input type="text" name="ufOrigem" required maxlength="2" value="<c:out value='${rota.ufOrigem}'/>"></div>
            <div class="field"><label>Município destino *</label><input type="text" name="municipioDestino" required value="<c:out value='${rota.municipioDestino}'/>"></div>
            <div class="field"><label>UF destino *</label><input type="text" name="ufDestino" required maxlength="2" value="<c:out value='${rota.ufDestino}'/>"></div>
            <div class="field"><label>Valor base (R$) *</label><input type="number" step="0.01" name="valorBase" required value="<c:out value='${rota.valorBase}'/>"></div>
            <div class="field"><label>Valor por kg (R$)</label><input type="number" step="0.0001" name="valorPorKg" value="<c:out value='${rota.valorPorKg}'/>"></div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/rotas" class="btn btn-secondary">Cancelar</a>
            <button type="submit" class="btn btn-primary">Salvar</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>