<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Relatórios" scope="request"/>
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
  
  .nl-page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; width: 100%; }
  .nl-page-header h1 { margin: 0; font-family: 'Avenir Next', sans-serif; font-size: 2rem; color: #0f172a; }
  
  .nl-chart-section { 
    background: white; 
    border-radius: 8px; 
    padding: 1.5rem; 
    box-shadow: 0 1px 3px rgba(0,0,0,0.1); 
    border: 1px solid #e2e8f0;
    margin-bottom: 2rem; 
    width: 100%;
  }
  
  .nl-chart-section-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem; }
  .nl-chart-section-title { font-size: 1rem; font-weight: 700; color: #0f172a; display: flex; align-items: center; gap: 0.5rem; }
  .nl-chart-section-title i { color: #3b82f6; }
  .nl-chart-sub { font-size: 0.8rem; color: #94a3b8; margin-top: 0.25rem; }
  
  #report-bar-chart { min-height: 300px; }
  
  .nl-report-grid { 
    display: grid; 
    grid-template-columns: repeat(2, 1fr); 
    gap: 1.5rem; 
    margin-bottom: 2rem; 
    width: 100%;
  }
  
  .nl-report-card {
    background: white; 
    border-radius: 8px; 
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    border: 1px solid #e2e8f0;
    overflow: hidden;
    display: flex; 
    flex-direction: column; 
    transition: box-shadow 0.2s, transform 0.2s;
  }
  
  .nl-report-card:hover { 
    box-shadow: 0 8px 28px rgba(0,0,0,0.12); 
    transform: translateY(-2px); 
  }
  
  .nl-report-card-header { 
    display: flex; 
    align-items: center; 
    gap: 1rem; 
    padding: 1.5rem; 
  }
  
  .nl-report-icon { 
    width: 50px; 
    height: 50px; 
    border-radius: 8px; 
    display: flex; 
    align-items: center; 
    justify-content: center; 
    font-size: 1.5rem; 
    flex-shrink: 0; 
  }
  
  .nl-report-icon.blue { background: #dbeafe; color: #3b82f6; }
  .nl-report-icon.green { background: #dcfce7; color: #16a34a; }
  
  .nl-report-card-title { font-size: 1rem; font-weight: 700; color: #0f172a; margin-bottom: 0.25rem; }
  .nl-report-card-desc { font-size: 0.85rem; color: #64748b; line-height: 1.4; }
  
  .nl-report-card-body { 
    padding: 0 1.5rem 1.5rem; 
    flex: 1; 
    display: flex; 
    flex-direction: column; 
    gap: 1rem; 
  }
  
  .nl-report-divider { height: 1px; background: #e2e8f0; margin: 0 1.5rem; }
  
  .nl-report-form { 
    display: flex; 
    flex-direction: column; 
    gap: 1rem; 
  }
  
  .nl-form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .nl-form-label {
    font-size: 0.85rem;
    font-weight: 600;
    color: #0f172a;
  }
  
  .nl-input-field {
    padding: 0.6rem 1rem; 
    border: 1px solid #e2e8f0; 
    border-radius: 6px;
    font-size: 0.9rem; 
    color: #0f172a; 
    background: #f8fafc;
    font-family: 'Avenir Next', sans-serif;
    outline: none; 
    transition: border-color 0.2s, box-shadow 0.2s;
    width: 100%;
  }
  
  .nl-input-field:focus { 
    border-color: #3b82f6; 
    box-shadow: 0 0 0 3px rgba(59,130,246,0.12); 
    background: white; 
  }
  
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
  
  .btn-primary {
    background: #3b82f6;
    color: white;
  }
  
  .btn-primary:hover {
    background: #2563eb;
    transform: translateY(-1px);
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  }
  
  @media (max-width: 1024px) {
    .nl-main { padding: 1.5rem; }
    .nl-container { max-width: 100%; }
    .nl-report-grid { grid-template-columns: 1fr; }
  }
  
  @media (max-width: 768px) {
    .nl-main { padding: 1rem; }
    .nl-page-header { flex-direction: column; gap: 1rem; align-items: flex-start; }
  }
</style>

<main class="nl-main">
  <div class="nl-container">
    <div class="nl-page-header">
      <h1><i class="bi bi-file-earmark-bar-graph-fill" style="color: #3b82f6; margin-right: 0.75rem;"></i>Relatórios</h1>
    </div>

    <div class="nl-chart-section">
      <div class="nl-chart-section-header">
        <div>
          <div class="nl-chart-section-title">
            <i class="bi bi-graph-up-arrow"></i> Volume de Fretes — Últimos 7 meses
          </div>
          <div class="nl-chart-sub">Fretes concluídos vs. cancelados</div>
        </div>
      </div>
      <div id="report-bar-chart"
           data-resumo="${not empty resumoMensal ? resumoMensal : '[&quot;Nov&quot;,&quot;Dez&quot;,&quot;Jan&quot;,&quot;Fev&quot;,&quot;Mar&quot;,&quot;Abr&quot;,&quot;Mai&quot;]|[42,55,38,61,49,70,58]|[5,8,4,7,3,6,4]'}">
      </div>
    </div>

    <div class="nl-report-grid">
      <div class="nl-report-card">
        <div class="nl-report-card-header">
          <div class="nl-report-icon blue"><i class="bi bi-box-seam-fill"></i></div>
          <div>
            <div class="nl-report-card-title">Fretes em Aberto</div>
            <div class="nl-report-card-desc">Lista completa de fretes não finalizados com dias em atraso.</div>
          </div>
        </div>
        <div class="nl-report-divider"></div>
        <div class="nl-report-card-body">
          <p style="font-size: 0.85rem; color: #64748b; line-height: 1.5;">
            Gera um PDF com todos os fretes pendentes, agrupados por status e ordenados por data de criação.
          </p>
          <a href="${pageContext.request.contextPath}/relatorios/fretes-em-aberto"
             class="btn btn-primary" target="_blank" style="align-self: flex-start;">
            <i class="bi bi-file-earmark-pdf-fill"></i> Gerar PDF
          </a>
        </div>
      </div>

      <div class="nl-report-card">
        <div class="nl-report-card-header">
          <div class="nl-report-icon green"><i class="bi bi-clipboard2-check-fill"></i></div>
          <div>
            <div class="nl-report-card-title">Romaneio de Carga</div>
            <div class="nl-report-card-desc">Fretes de um motorista em uma data específica.</div>
          </div>
        </div>
        <div class="nl-report-divider"></div>
        <div class="nl-report-card-body">
          <form method="get" action="${pageContext.request.contextPath}/relatorios/romaneio"
                target="_blank" class="nl-report-form">
            <div class="nl-form-group">
              <label class="nl-form-label" for="sel-motorista">Motorista</label>
              <select name="idMotorista" id="sel-motorista" class="nl-input-field" required>
                <option value="">Selecione um motorista…</option>
                <c:forEach var="m" items="${motoristas}">
                  <option value="${m.id}"><c:out value="${m.nome}"/></option>
                </c:forEach>
              </select>
            </div>
            <div class="nl-form-group">
              <label class="nl-form-label" for="input-data">Data</label>
              <input type="date" name="data" id="input-data" class="nl-input-field" required>
            </div>
            <button type="submit" class="btn btn-primary" style="align-self: flex-start;">
              <i class="bi bi-file-earmark-pdf-fill"></i> Gerar PDF
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/apexcharts@3.54.0/dist/apexcharts.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/relatorio-chart.js"></script>