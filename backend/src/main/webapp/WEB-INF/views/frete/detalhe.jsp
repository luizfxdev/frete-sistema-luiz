<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Frete ${frete.numero}" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<style>
  .nl-main { 
    padding: 2rem; 
    min-height: 100vh; 
    background: #f8fafc; 
    display: flex; 
    justify-content: center;
    width: 100%;
  }
  
  .nl-container { 
    width: 100%; 
    max-width: 1100px; 
  }
  
  .nl-page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 2rem; width: 100%; }
  .nl-page-header h1 { margin: 0; font-family: 'Avenir Next', sans-serif; font-size: 2rem; color: #0f172a; }
  .nl-page-header-info { display: flex; flex-direction: column; gap: 0.5rem; }
  
  .nl-badge { display: inline-block; padding: 0.4rem 0.9rem; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
  .badge-emitido { background: #dbeafe; color: #0c4a6e; }
  .badge-saida-confirmada { background: #bfdbfe; color: #1e40af; }
  .badge-em-transito { background: #fef3c7; color: #b45309; }
  .badge-entregue { background: #dcfce7; color: #166534; }
  .badge-nao-entregue { background: #fee2e2; color: #991b1b; }
  .badge-cancelado { background: #fecaca; color: #991b1b; }
  
  .nl-alert { padding: 1rem; border-radius: 6px; margin-bottom: 1.5rem; display: flex; align-items: flex-start; gap: 0.75rem; width: 100%; }
  .nl-alert-error { background: #fee2e2; color: #991b1b; }
  
  .nl-grid-2 { display: grid; grid-template-columns: repeat(2, 1fr); gap: 1.5rem; margin-bottom: 1.5rem; width: 100%; }
  
  .nl-card { 
    background: white; 
    border-radius: 8px; 
    padding: 1.5rem; 
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
  }
  
  .nl-card-title { font-size: 0.95rem; font-weight: 600; color: #0f172a; margin-bottom: 0.5rem; }
  .nl-card-row { display: flex; justify-content: space-between; gap: 1rem; }
  .nl-card-label { font-size: 0.75rem; color: #94a3b8; font-weight: 600; text-transform: uppercase; }
  .nl-card-value { font-size: 0.95rem; color: #1e293b; font-weight: 500; }
  .nl-card-value.mono { font-family: 'Courier New', monospace; color: #3b82f6; font-weight: 600; }
  .nl-card-value.highlight { font-size: 1.25rem; font-weight: 700; color: #0f172a; }
  
  .nl-actions-card { background: white; border-radius: 8px; padding: 1.5rem; box-shadow: 0 1px 3px rgba(0,0,0,0.1); margin-bottom: 1.5rem; width: 100%; }
  .nl-actions-title { font-size: 0.95rem; font-weight: 600; color: #0f172a; margin-bottom: 1rem; }
  .nl-actions-group { display: flex; flex-wrap: wrap; gap: 0.75rem; }
  
  .btn { 
    padding: 0.6rem 1.2rem; 
    border-radius: 6px; 
    border: none; 
    cursor: pointer; 
    font-weight: 500; 
    text-decoration: none; 
    display: inline-flex; 
    align-items: center; 
    gap: 0.4rem; 
    font-size: 0.9rem; 
    font-family: 'Avenir Next', sans-serif;
    transition: all 0.2s ease;
  }
  
  .btn-primary { background: #3b82f6; color: white; }
  .btn-primary:hover { background: #2563eb; transform: translateY(-1px); box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
  
  .btn-secondary { background: #d3d3d3; color: #333; }
  .btn-secondary:hover { background: #c0c0c0; transform: translateY(-1px); box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
  
  .btn-danger { background: #fca5a5; color: #7f1d1d; }
  .btn-danger:hover { background: #f87171; transform: translateY(-1px); box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
  
  .nl-table-card { background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 1px 3px rgba(0,0,0,0.1); width: 100%; }
  .nl-table { width: 100%; border-collapse: collapse; }
  .nl-table thead { background: #292929; color: white; }
  .nl-table th { padding: 1rem; text-align: left; font-size: 0.85rem; font-weight: 600; letter-spacing: 0.5px; }
  .nl-table td { padding: 1rem; border-bottom: 1px solid #e2e8f0; }
  .nl-table tbody tr:hover { background: #f8fafc; }
  
  .nl-empty { text-align: center; padding: 2rem; color: #94a3b8; font-size: 0.9rem; }
  
  .nl-chart-card { 
    background: white; 
    border-radius: 8px; 
    padding: 1.5rem; 
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    margin-bottom: 1.5rem;
    width: 100%;
  }
  
  .nl-chart-title { font-size: 0.95rem; font-weight: 600; color: #0f172a; margin-bottom: 1rem; }
  #status-timeline { width: 100%; height: 280px; }
  
  .modal-overlay { 
    display: none; 
    position: fixed; 
    top: 0; 
    left: 0; 
    width: 100%; 
    height: 100%; 
    background: rgba(0,0,0,0.5); 
    z-index: 1000; 
    align-items: center; 
    justify-content: center;
  }
  
  .modal-overlay.active { display: flex; }
  
  .modal-content { 
    background: white; 
    border-radius: 8px; 
    padding: 2rem; 
    box-shadow: 0 10px 40px rgba(0,0,0,0.2); 
    max-width: 500px; 
    width: 100%;
  }
  
  .modal-title { 
    font-size: 1.25rem; 
    font-weight: 600; 
    color: #0f172a; 
    margin-bottom: 1rem; 
  }
  
  .modal-field { 
    display: flex; 
    flex-direction: column; 
    gap: 0.5rem; 
    margin-bottom: 1.5rem;
  }
  
  .modal-field label { 
    font-size: 0.875rem; 
    font-weight: 600; 
    color: #1f2937;
  }
  
  .modal-field textarea { 
    padding: 0.75rem; 
    border: 1px solid #d1d5db; 
    border-radius: 6px; 
    font-size: 0.875rem; 
    font-family: 'Avenir Next', sans-serif;
    resize: vertical;
    min-height: 80px;
  }
  
  .modal-actions { 
    display: flex; 
    gap: 0.75rem; 
    justify-content: flex-end;
  }
  
  @media (max-width: 1024px) {
    .nl-main { padding: 1.5rem; }
    .nl-container { max-width: 100%; }
    .nl-grid-2 { grid-template-columns: 1fr; }
    .nl-page-header { flex-direction: column; gap: 1rem; }
  }
  
  @media (max-width: 768px) {
    .nl-main { padding: 1rem; }
    .nl-card { padding: 1rem; }
    .nl-actions-group { gap: 0.5rem; }
    .btn { padding: 0.5rem 1rem; font-size: 0.85rem; }
  }
</style>

<main class="nl-main">
  <div class="nl-container">
    <div class="nl-page-header">
      <div class="nl-page-header-info">
        <h1 style="margin: 0;">Frete <span style="color: #3b82f6;"><c:out value="${frete.numero}"/></span></h1>
        <c:set var="statusClass" value="badge-${fn:toLowerCase(fn:replace(frete.status, '_', '-'))}"/>
        <span class="nl-badge ${statusClass}"><c:out value="${frete.status}"/></span>
      </div>
      <a href="${pageContext.request.contextPath}/fretes" class="btn btn-secondary">
        <i class="bi bi-arrow-left"></i> Voltar
      </a>
    </div>

    <c:if test="${not empty erro}">
      <div class="nl-alert nl-alert-error">
        <i class="bi bi-exclamation-triangle-fill"></i>
        <div><c:out value="${erro}"/></div>
      </div>
    </c:if>

    <div class="nl-grid-2">
      <div class="nl-card">
        <div class="nl-card-title"><i class="bi bi-people-fill" style="color: #3b82f6; margin-right: 0.5rem;"></i>Partes</div>
        <div class="nl-card-row">
          <div style="flex: 1;">
            <div class="nl-card-label">Remetente</div>
            <div class="nl-card-value"><c:out value="${frete.remetenteRazaoSocial}"/></div>
          </div>
        </div>
        <div class="nl-card-row">
          <div style="flex: 1;">
            <div class="nl-card-label">Destinatário</div>
            <div class="nl-card-value"><c:out value="${frete.destinatarioRazaoSocial}"/></div>
          </div>
        </div>
        <div class="nl-card-row">
          <div style="flex: 1;">
            <div class="nl-card-label">Motorista</div>
            <div class="nl-card-value"><c:out value="${frete.motoristaNome}"/></div>
          </div>
        </div>
        <div class="nl-card-row">
          <div style="flex: 1;">
            <div class="nl-card-label">Veículo</div>
            <div class="nl-card-value mono"><c:out value="${frete.veiculoPlaca}"/></div>
          </div>
        </div>
      </div>

      <div class="nl-card">
        <div class="nl-card-title"><i class="bi bi-map-fill" style="color: #3b82f6; margin-right: 0.5rem;"></i>Trajeto</div>
        <div class="nl-card-row">
          <div style="flex: 1;">
            <div class="nl-card-label">Origem → Destino</div>
            <div class="nl-card-value"><c:out value="${frete.municipioOrigem}"/>/<c:out value="${frete.ufOrigem}"/> → <c:out value="${frete.municipioDestino}"/>/<c:out value="${frete.ufDestino}"/></div>
          </div>
        </div>
        <div class="nl-card-row">
          <div style="flex: 1;">
            <div class="nl-card-label">Descrição Carga</div>
            <div class="nl-card-value"><c:out value="${frete.descricaoCarga}"/></div>
          </div>
        </div>
        <div class="nl-card-row">
          <div>
            <div class="nl-card-label">Peso</div>
            <div class="nl-card-value"><c:out value="${frete.pesoKg}"/> kg</div>
          </div>
          <div>
            <div class="nl-card-label">Volumes</div>
            <div class="nl-card-value"><c:out value="${frete.volumes}"/></div>
          </div>
        </div>
      </div>

      <div class="nl-card">
        <div class="nl-card-title"><i class="bi bi-currency-dollar" style="color: #3b82f6; margin-right: 0.5rem;"></i>Valores</div>
        <div class="nl-card-row">
          <div style="flex: 1;">
            <div class="nl-card-label">Valor Frete</div>
            <div class="nl-card-value">R$ <c:out value="${frete.valorFrete}"/></div>
          </div>
        </div>
        <div class="nl-card-row">
          <div style="flex: 1;">
            <div class="nl-card-label">ICMS</div>
            <div class="nl-card-value"><c:out value="${frete.aliquotaIcms}"/>% (R$ <c:out value="${frete.valorIcms}"/>)</div>
          </div>
        </div>
        <div class="nl-card-row" style="border-top: 1px solid #e2e8f0; padding-top: 0.75rem; margin-top: 0.75rem;">
          <div style="flex: 1;">
            <div class="nl-card-label">Total</div>
            <div class="nl-card-value highlight">R$ <c:out value="${frete.valorTotal}"/></div>
          </div>
        </div>
      </div>

      <div class="nl-card">
        <div class="nl-card-title"><i class="bi bi-calendar-event" style="color: #3b82f6; margin-right: 0.5rem;"></i>Datas</div>
        <div class="nl-card-row">
          <div>
            <div class="nl-card-label">Emissão</div>
            <div class="nl-card-value"><c:out value="${frete.dataEmissao}"/></div>
          </div>
        </div>
        <div class="nl-card-row">
          <div>
            <div class="nl-card-label">Previsão Entrega</div>
            <div class="nl-card-value"><c:out value="${frete.dataPrevisaoEntrega}"/></div>
          </div>
        </div>
        <div class="nl-card-row">
          <div>
            <div class="nl-card-label">Saída</div>
            <div class="nl-card-value"><c:out value="${frete.dataSaida}" default="—"/></div>
          </div>
          <div>
            <div class="nl-card-label">Entrega</div>
            <div class="nl-card-value"><c:out value="${frete.dataEntrega}" default="—"/></div>
          </div>
        </div>
      </div>
    </div>

    <div class="nl-chart-card">
      <div class="nl-chart-title"><i class="bi bi-diagram-3" style="color: #3b82f6; margin-right: 0.5rem;"></i>Status Timeline</div>
      <div id="status-timeline" data-status="${frete.status}"></div>
    </div>

    <div class="nl-actions-card">
      <div class="nl-actions-title"><i class="bi bi-lightning-fill" style="color: #3b82f6; margin-right: 0.5rem;"></i>Ações Disponíveis</div>
      <div class="nl-actions-group">
        <c:if test="${frete.status == 'EMITIDO'}">
          <form method="post" action="${pageContext.request.contextPath}/fretes/confirmar-saida/${frete.id}" style="display: inline;">
            <button type="submit" class="btn btn-primary" onclick="return confirm('Confirmar saída do veículo?');">
              <i class="bi bi-check-circle"></i> Confirmar saída
            </button>
          </form>
          <form method="post" action="${pageContext.request.contextPath}/fretes/cancelar/${frete.id}" style="display: inline;">
            <button type="submit" class="btn btn-danger" onclick="return confirm('Cancelar frete?');">
              <i class="bi bi-x-circle"></i> Cancelar frete
            </button>
          </form>
        </c:if>
        <c:if test="${frete.status == 'SAIDA_CONFIRMADA'}">
          <form method="post" action="${pageContext.request.contextPath}/fretes/marcar-em-transito/${frete.id}" style="display: inline;">
            <button type="submit" class="btn btn-primary" onclick="return confirm('Marcar frete em trânsito?');">
              <i class="bi bi-arrow-right-circle"></i> Marcar em trânsito
            </button>
          </form>
        </c:if>
        <c:if test="${frete.status == 'EM_TRANSITO'}">
          <form method="post" action="${pageContext.request.contextPath}/fretes/registrar-entrega/${frete.id}" style="display: inline;">
            <button type="submit" class="btn btn-primary" onclick="return confirm('Registrar entrega?');">
              <i class="bi bi-check2-circle"></i> Registrar entrega
            </button>
          </form>
          <button type="button" class="btn btn-danger" onclick="abrirModalNaoEntrega();">
            <i class="bi bi-exclamation-circle"></i> Não entregue
          </button>
        </c:if>
        <c:if test="${frete.status != 'ENTREGUE' && frete.status != 'NAO_ENTREGUE' && frete.status != 'CANCELADO'}">
          <a href="${pageContext.request.contextPath}/ocorrencias/novo/${frete.id}" class="btn btn-secondary">
            <i class="bi bi-plus-circle"></i> Nova ocorrência
          </a>
        </c:if>
      </div>
    </div>

    <div class="nl-table-card">
      <div style="padding: 1rem 0 0 0;">
        <h2 style="font-size: 0.95rem; font-weight: 600; color: #0f172a; margin: 0 1rem 1rem 1rem; padding: 0;">
          <i class="bi bi-list-ul" style="color: #3b82f6; margin-right: 0.5rem;"></i>Histórico de Ocorrências
        </h2>
      </div>
      <table class="nl-table">
        <thead>
          <tr>
            <th>Data/Hora</th>
            <th>Tipo</th>
            <th>Local</th>
            <th>Descrição</th>
            <th>Recebedor</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="o" items="${ocorrencias}">
            <tr>
              <td><c:out value="${o.dataHora}"/></td>
              <td><c:out value="${o.tipo}"/></td>
              <td><c:out value="${o.municipio}"/>/<c:out value="${o.uf}"/></td>
              <td><c:out value="${o.descricao}" default="—"/></td>
              <td><c:out value="${o.nomeRecebedor}" default="—"/></td>
            </tr>
          </c:forEach>
          <c:if test="${empty ocorrencias}">
            <tr>
              <td colspan="5">
                <div class="nl-empty">
                  <i class="bi bi-inbox"></i> Sem ocorrências registradas.
                </div>
              </td>
            </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</main>

<div id="modalNaoEntrega" class="modal-overlay">
  <div class="modal-content">
    <div class="modal-title">Registrar Não Entrega</div>
    <form method="post" action="${pageContext.request.contextPath}/fretes/registrar-nao-entrega/${frete.id}">
      <div class="modal-field">
        <label for="motivo">Motivo da não entrega *</label>
        <textarea id="motivo" name="motivo" required placeholder="Descreva o motivo da tentativa frustrada de entrega"></textarea>
      </div>
      <div class="modal-actions">
        <button type="button" class="btn btn-secondary" onclick="fecharModalNaoEntrega();">Cancelar</button>
        <button type="submit" class="btn btn-danger">Confirmar não entrega</button>
      </div>
    </form>
  </div>
</div>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/apexcharts@3.54.0/dist/apexcharts.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/nl-confirm.js"></script>
<script src="${pageContext.request.contextPath}/static/js/frota-timeline.js"></script>
<script>
  function abrirModalNaoEntrega() {
    document.getElementById('modalNaoEntrega').classList.add('active');
  }
  function fecharModalNaoEntrega() {
    document.getElementById('modalNaoEntrega').classList.remove('active');
  }
  document.getElementById('modalNaoEntrega').addEventListener('click', function(e) {
    if (e.target === this) fecharModalNaoEntrega();
  });
</script>