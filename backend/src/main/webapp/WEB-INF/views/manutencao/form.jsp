<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Manutenção" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <h1 class="text-2xl font-semibold text-slate-900 mb-6">
        ${manutencao.id == null ? 'Nova manutenção' : 'Editar manutenção'}
    </h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/manutencoes" class="card">
        <input type="hidden" name="id" value="<c:out value='${manutencao.id}'/>"/>

        <div class="form-grid">
            <div class="field">
                <label>Veículo *</label>
                <select name="idVeiculo" required>
                    <option value="">Selecione</option>
                    <c:forEach var="v" items="${veiculos}">
                        <option value="${v.id}" ${manutencao.idVeiculo == v.id ? 'selected' : ''}>${v.placa} - ${v.tipo}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label>Tipo *</label>
                <select name="tipo" required>
                    <option value="">Selecione</option>
                    <c:forEach var="t" items="${tipos}">
                        <option value="${t}" ${manutencao.tipo == t ? 'selected' : ''}>${t}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field full"><label>Descrição *</label><textarea name="descricao" rows="3" required><c:out value="${manutencao.descricao}"/></textarea></div>
            <div class="field"><label>Data de início *</label><input type="date" name="dataInicio" required value="<c:out value='${manutencao.dataInicio}'/>"></div>
            <div class="field"><label>Data de fim</label><input type="date" name="dataFim" value="<c:out value='${manutencao.dataFim}'/>"></div>
            <div class="field"><label>Custo (R$)</label><input type="number" step="0.01" name="custo" value="<c:out value='${manutencao.custo}'/>"></div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/manutencoes" class="btn btn-secondary">Cancelar</a>
            <button type="submit" class="btn btn-primary">Salvar</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>