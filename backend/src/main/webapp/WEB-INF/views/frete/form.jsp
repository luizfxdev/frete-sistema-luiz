<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Novo frete" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <h1 class="text-2xl font-semibold text-slate-900 mb-6">Novo frete</h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/fretes" class="card">
        <div class="form-grid">
            <div class="field">
                <label>Remetente *</label>
                <select name="idRemetente" required>
                    <option value="">Selecione</option>
                    <c:forEach var="c" items="${clientes}">
                        <option value="${c.id}" ${frete.idRemetente == c.id ? 'selected' : ''}>${c.razaoSocial}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label>Destinatário *</label>
                <select name="idDestinatario" required>
                    <option value="">Selecione</option>
                    <c:forEach var="c" items="${clientes}">
                        <option value="${c.id}" ${frete.idDestinatario == c.id ? 'selected' : ''}>${c.razaoSocial}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label>Motorista *</label>
                <select name="idMotorista" required>
                    <option value="">Selecione</option>
                    <c:forEach var="m" items="${motoristas}">
                        <option value="${m.id}" ${frete.idMotorista == m.id ? 'selected' : ''}>${m.nome}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label>Veículo *</label>
                <select name="idVeiculo" required>
                    <option value="">Selecione</option>
                    <c:forEach var="v" items="${veiculos}">
                        <option value="${v.id}" ${frete.idVeiculo == v.id ? 'selected' : ''}>${v.placa} - ${v.tipo}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="field"><label>Município origem *</label><input type="text" name="municipioOrigem" required value="<c:out value='${frete.municipioOrigem}'/>"></div>
            <div class="field"><label>UF origem *</label><input type="text" name="ufOrigem" required maxlength="2" value="<c:out value='${frete.ufOrigem}'/>"></div>
            <div class="field"><label>Município destino *</label><input type="text" name="municipioDestino" required value="<c:out value='${frete.municipioDestino}'/>"></div>
            <div class="field"><label>UF destino *</label><input type="text" name="ufDestino" required maxlength="2" value="<c:out value='${frete.ufDestino}'/>"></div>

            <div class="field full"><label>Descrição da carga *</label><input type="text" name="descricaoCarga" required value="<c:out value='${frete.descricaoCarga}'/>"></div>
            <div class="field"><label>Peso (kg) *</label><input type="number" step="0.01" name="pesoKg" required value="<c:out value='${frete.pesoKg}'/>"></div>
            <div class="field"><label>Volumes *</label><input type="number" name="volumes" required value="<c:out value='${frete.volumes}'/>"></div>
            <div class="field"><label>Valor do frete (R$) *</label><input type="number" step="0.01" name="valorFrete" required value="<c:out value='${frete.valorFrete}'/>"></div>
            <div class="field"><label>Alíquota ICMS (%) *</label><input type="number" step="0.01" name="aliquotaIcms" required value="<c:out value='${frete.aliquotaIcms}'/>"></div>
            <div class="field"><label>Data de emissão *</label><input type="date" name="dataEmissao" required data-default-today value="<c:out value='${frete.dataEmissao}'/>"></div>
            <div class="field"><label>Previsão de entrega *</label><input type="date" name="dataPrevisaoEntrega" required value="<c:out value='${frete.dataPrevisaoEntrega}'/>"></div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/fretes" class="btn btn-secondary">Cancelar</a>
            <button type="submit" class="btn btn-primary">Emitir frete</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>