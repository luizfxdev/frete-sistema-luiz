<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Veículo" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <h1 class="text-2xl font-semibold text-slate-900 mb-6">
        ${veiculo.id == null ? 'Novo veículo' : 'Editar veículo'}
    </h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/veiculos" class="card">
        <input type="hidden" name="id" value="<c:out value='${veiculo.id}'/>"/>

        <div class="form-grid">
            <div class="field">
                <label>Placa *</label>
                <input type="text" name="placa" required maxlength="7" data-mask="placa" value="<c:out value='${veiculo.placa}'/>">
            </div>
            <div class="field">
                <label>RNTRC</label>
                <input type="text" name="rntrc" value="<c:out value='${veiculo.rntrc}'/>">
            </div>
            <div class="field">
                <label>Ano de fabricação *</label>
                <input type="number" name="anoFabricacao" required min="1980" max="2100" value="<c:out value='${veiculo.anoFabricacao}'/>">
            </div>
            <div class="field">
                <label>Tipo *</label>
                <select name="tipo" required>
                    <option value="">Selecione</option>
                    <c:forEach var="t" items="${tipos}">
                        <option value="${t}" ${veiculo.tipo == t ? 'selected' : ''}>${t}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field"><label>Tara (kg) *</label><input type="number" step="0.01" name="taraKg" required value="<c:out value='${veiculo.taraKg}'/>"></div>
            <div class="field"><label>Capacidade (kg) *</label><input type="number" step="0.01" name="capacidadeKg" required value="<c:out value='${veiculo.capacidadeKg}'/>"></div>
            <div class="field"><label>Volume (m³)</label><input type="number" step="0.01" name="volumeM3" value="<c:out value='${veiculo.volumeM3}'/>"></div>
            <div class="field">
                <label>Status</label>
                <select name="status">
                    <c:forEach var="s" items="${statusVeiculo}">
                        <option value="${s}" ${veiculo.status == s ? 'selected' : ''}>${s}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/veiculos" class="btn btn-secondary">Cancelar</a>
            <button type="submit" class="btn btn-primary">Salvar</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>