<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Nova ocorrência" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <h1 class="text-2xl font-semibold text-slate-900 mb-6">
        Nova ocorrência - ${frete.numero}
    </h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/ocorrencias" class="card">
        <input type="hidden" name="idFrete" value="${ocorrencia.idFrete}"/>

        <div class="form-grid">
            <div class="field">
                <label>Tipo *</label>
                <select name="tipo" required>
                    <option value="">Selecione</option>
                    <c:forEach var="t" items="${tipos}">
                        <option value="${t}" ${ocorrencia.tipo == t ? 'selected' : ''}>${t}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label>Data/hora *</label>
                <input type="datetime-local" name="dataHora" required value="<c:out value='${ocorrencia.dataHora}'/>">
            </div>
            <div class="field"><label>Município *</label><input type="text" name="municipio" required value="<c:out value='${ocorrencia.municipio}'/>"></div>
            <div class="field"><label>UF *</label><input type="text" name="uf" required maxlength="2" value="<c:out value='${ocorrencia.uf}'/>"></div>
            <div class="field full"><label>Descrição</label><textarea name="descricao" rows="2"><c:out value="${ocorrencia.descricao}"/></textarea></div>
            <div class="field"><label>Nome do recebedor</label><input type="text" name="nomeRecebedor" value="<c:out value='${ocorrencia.nomeRecebedor}'/>"></div>
            <div class="field"><label>Documento do recebedor</label><input type="text" name="documentoRecebedor" value="<c:out value='${ocorrencia.documentoRecebedor}'/>"></div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/fretes/detalhe/${ocorrencia.idFrete}" class="btn btn-secondary">Cancelar</a>
            <button type="submit" class="btn btn-primary">Registrar</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>