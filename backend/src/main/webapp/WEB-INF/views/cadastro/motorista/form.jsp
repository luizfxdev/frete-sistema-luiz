<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Motorista" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <h1 class="text-2xl font-semibold text-slate-900 mb-6">
        ${motorista.id == null ? 'Novo motorista' : 'Editar motorista'}
    </h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/motoristas" class="card">
        <input type="hidden" name="id" value="<c:out value='${motorista.id}'/>"/>

        <div class="form-grid">
            <div class="field full">
                <label>Nome *</label>
                <input type="text" name="nome" required value="<c:out value='${motorista.nome}'/>">
            </div>
            <div class="field">
                <label>CPF *</label>
                <input type="text" name="cpf" required data-mask="cpf" value="<c:out value='${motorista.cpf}'/>">
            </div>
            <div class="field">
                <label>Data de nascimento *</label>
                <input type="date" name="dataNascimento" required value="<c:out value='${motorista.dataNascimento}'/>">
            </div>
            <div class="field">
                <label>Telefone</label>
                <input type="text" name="telefone" data-mask="telefone" value="<c:out value='${motorista.telefone}'/>">
            </div>
            <div class="field">
                <label>Número da CNH *</label>
                <input type="text" name="cnhNumero" required value="<c:out value='${motorista.cnhNumero}'/>">
            </div>
            <div class="field">
                <label>Categoria *</label>
                <select name="cnhCategoria" required>
                    <option value="">Selecione</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat}" ${motorista.cnhCategoria == cat ? 'selected' : ''}>${cat}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label>Validade da CNH *</label>
                <input type="date" name="cnhValidade" required value="<c:out value='${motorista.cnhValidade}'/>">
            </div>
            <div class="field">
                <label>Tipo de vínculo *</label>
                <select name="tipoVinculo" required>
                    <option value="">Selecione</option>
                    <c:forEach var="v" items="${vinculos}">
                        <option value="${v}" ${motorista.tipoVinculo == v ? 'selected' : ''}>${v}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label>Status</label>
                <select name="status">
                    <c:forEach var="s" items="${statusMotorista}">
                        <option value="${s}" ${motorista.status == s ? 'selected' : ''}>${s}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/motoristas" class="btn btn-secondary">Cancelar</a>
            <button type="submit" class="btn btn-primary">Salvar</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>