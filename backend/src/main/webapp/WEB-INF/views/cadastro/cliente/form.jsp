<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Cliente" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <h1 class="text-2xl font-semibold text-slate-900 mb-6">
        ${cliente.id == null ? 'Novo cliente' : 'Editar cliente'}
    </h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/clientes" class="card">
        <input type="hidden" name="id" value="<c:out value='${cliente.id}'/>"/>

        <div class="form-grid">
            <div class="field">
                <label>Razão social *</label>
                <input type="text" name="razaoSocial" required value="<c:out value='${cliente.razaoSocial}'/>">
            </div>
            <div class="field">
                <label>Nome fantasia</label>
                <input type="text" name="nomeFantasia" value="<c:out value='${cliente.nomeFantasia}'/>">
            </div>
            <div class="field">
                <label>CNPJ *</label>
                <input type="text" name="cnpj" required data-mask="cnpj" value="<c:out value='${cliente.cnpj}'/>">
            </div>
            <div class="field">
                <label>Inscrição estadual</label>
                <input type="text" name="inscricaoEstadual" value="<c:out value='${cliente.inscricaoEstadual}'/>">
            </div>
            <div class="field">
                <label>Tipo *</label>
                <select name="tipo" required>
                    <option value="">Selecione</option>
                    <c:forEach var="t" items="${tiposCliente}">
                        <option value="${t}" ${cliente.tipo == t ? 'selected' : ''}>${t}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label>Status</label>
                <select name="status">
                    <option value="ATIVO"   ${cliente.status == 'ATIVO'   ? 'selected' : ''}>ATIVO</option>
                    <option value="INATIVO" ${cliente.status == 'INATIVO' ? 'selected' : ''}>INATIVO</option>
                </select>
            </div>

            <div class="field full"><label>Logradouro *</label><input type="text" name="logradouro" required value="<c:out value='${cliente.logradouro}'/>"></div>
            <div class="field"><label>Número *</label><input type="text" name="numero" required value="<c:out value='${cliente.numero}'/>"></div>
            <div class="field"><label>Complemento</label><input type="text" name="complemento" value="<c:out value='${cliente.complemento}'/>"></div>
            <div class="field"><label>Bairro *</label><input type="text" name="bairro" required value="<c:out value='${cliente.bairro}'/>"></div>
            <div class="field"><label>Município *</label><input type="text" name="municipio" required value="<c:out value='${cliente.municipio}'/>"></div>
            <div class="field"><label>UF *</label><input type="text" name="uf" required maxlength="2" value="<c:out value='${cliente.uf}'/>"></div>
            <div class="field"><label>CEP *</label><input type="text" name="cep" required data-mask="cep" value="<c:out value='${cliente.cep}'/>"></div>
            <div class="field"><label>Telefone</label><input type="text" name="telefone" data-mask="telefone" value="<c:out value='${cliente.telefone}'/>"></div>
            <div class="field"><label>E-mail</label><input type="email" name="email" value="<c:out value='${cliente.email}'/>"></div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/clientes" class="btn btn-secondary">Cancelar</a>
            <button type="submit" class="btn btn-primary">Salvar</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>