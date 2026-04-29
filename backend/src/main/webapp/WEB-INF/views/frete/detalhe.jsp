<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Frete ${frete.numero}" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <div class="flex items-center justify-between mb-6">
        <div>
            <h1 class="text-2xl font-semibold text-slate-900">${frete.numero}</h1>
            <c:set var="badgeCls" value="badge-${fn:toLowerCase(fn:replace(frete.status, '_', '-'))}"/>
            <span class="badge ${badgeCls} mt-2">${frete.status}</span>
        </div>
        <a href="${pageContext.request.contextPath}/fretes" class="btn btn-secondary">Voltar</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-error"><c:out value="${erro}"/></div>
    </c:if>

    <div class="grid grid-cols-2 gap-4 mb-6">
        <div class="card">
            <h2 class="font-semibold mb-3 text-slate-700">Partes</h2>
            <p><span class="text-slate-500 text-sm">Remetente:</span> <c:out value="${frete.remetenteRazaoSocial}"/></p>
            <p><span class="text-slate-500 text-sm">Destinatário:</span> <c:out value="${frete.destinatarioRazaoSocial}"/></p>
            <p><span class="text-slate-500 text-sm">Motorista:</span> <c:out value="${frete.motoristaNome}"/></p>
            <p><span class="text-slate-500 text-sm">Veículo:</span> ${frete.veiculoPlaca}</p>
        </div>
        <div class="card">
            <h2 class="font-semibold mb-3 text-slate-700">Trajeto</h2>
            <p>${frete.municipioOrigem}/${frete.ufOrigem} → ${frete.municipioDestino}/${frete.ufDestino}</p>
            <p><span class="text-slate-500 text-sm">Carga:</span> <c:out value="${frete.descricaoCarga}"/></p>
            <p><span class="text-slate-500 text-sm">Peso:</span> ${frete.pesoKg} kg | <span class="text-slate-500 text-sm">Volumes:</span> ${frete.volumes}</p>
        </div>
        <div class="card">
            <h2 class="font-semibold mb-3 text-slate-700">Valores</h2>
            <p><span class="text-slate-500 text-sm">Valor frete:</span> R$ ${frete.valorFrete}</p>
            <p><span class="text-slate-500 text-sm">ICMS:</span> ${frete.aliquotaIcms}% (R$ ${frete.valorIcms})</p>
            <p class="font-semibold"><span class="text-slate-500 text-sm font-normal">Total:</span> R$ ${frete.valorTotal}</p>
        </div>
        <div class="card">
            <h2 class="font-semibold mb-3 text-slate-700">Datas</h2>
            <p><span class="text-slate-500 text-sm">Emissão:</span> ${frete.dataEmissao}</p>
            <p><span class="text-slate-500 text-sm">Previsão entrega:</span> ${frete.dataPrevisaoEntrega}</p>
            <p><span class="text-slate-500 text-sm">Saída:</span> <c:out value="${frete.dataSaida}" default="—"/></p>
            <p><span class="text-slate-500 text-sm">Entrega:</span> <c:out value="${frete.dataEntrega}" default="—"/></p>
        </div>
    </div>

    <div class="card mb-6">
        <h2 class="font-semibold mb-3 text-slate-700">Ações</h2>
        <div class="flex flex-wrap gap-2">
            <c:if test="${frete.status == 'EMITIDO'}">
                <form method="post" action="${pageContext.request.contextPath}/fretes/confirmar-saida/${frete.id}"
                      data-confirm="Confirmar saída do veículo?"><button class="btn btn-primary">Confirmar saída</button></form>
                <form method="post" action="${pageContext.request.contextPath}/fretes/cancelar/${frete.id}"
                      data-confirm="Cancelar frete?"><button class="btn btn-danger">Cancelar frete</button></form>
            </c:if>
            <c:if test="${frete.status == 'SAIDA_CONFIRMADA'}">
                <form method="post" action="${pageContext.request.contextPath}/fretes/marcar-em-transito/${frete.id}"
                      data-confirm="Marcar frete em trânsito?"><button class="btn btn-primary">Marcar em trânsito</button></form>
            </c:if>
            <c:if test="${frete.status == 'EM_TRANSITO'}">
                <form method="post" action="${pageContext.request.contextPath}/fretes/registrar-entrega/${frete.id}"
                      data-confirm="Registrar entrega?"><button class="btn btn-primary">Registrar entrega</button></form>
                <form method="post" action="${pageContext.request.contextPath}/fretes/registrar-nao-entrega/${frete.id}"
                      data-confirm="Registrar como não entregue?"><button class="btn btn-danger">Não entregue</button></form>
            </c:if>
            <c:if test="${frete.status != 'ENTREGUE' && frete.status != 'NAO_ENTREGUE' && frete.status != 'CANCELADO'}">
                <a href="${pageContext.request.contextPath}/ocorrencias/novo/${frete.id}" class="btn btn-secondary">+ Nova ocorrência</a>
            </c:if>
        </div>
    </div>

    <div class="card">
        <h2 class="font-semibold mb-3 text-slate-700">Histórico de ocorrências</h2>
        <table class="table">
            <thead>
            <tr><th>Data/Hora</th><th>Tipo</th><th>Local</th><th>Descrição</th><th>Recebedor</th></tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${ocorrencias}">
                <tr>
                    <td>${o.dataHora}</td>
                    <td>${o.tipo}</td>
                    <td>${o.municipio}/${o.uf}</td>
                    <td><c:out value="${o.descricao}" default="—"/></td>
                    <td><c:out value="${o.nomeRecebedor}" default="—"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty ocorrencias}">
                <tr><td colspan="5" class="text-center py-6 text-slate-500">Sem ocorrências registradas.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>