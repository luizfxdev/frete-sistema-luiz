<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Romaneio de Carga" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="nl-main">
  <div class="nl-container">
    <div class="nl-page-header">
      <h1><i class="bi bi-clipboard2-check-fill" style="color: #16a34a; margin-right: 0.75rem;"></i>Romaneio de Carga</h1>
    </div>

    <div class="nl-report-card" style="max-width: 600px; margin: 0 auto;">
      <div class="nl-report-card-header">
        <div class="nl-report-icon green"><i class="bi bi-clipboard2-check-fill"></i></div>
        <div>
          <div class="nl-report-card-title">Gerar Romaneio</div>
          <div class="nl-report-card-desc">Fretes de um motorista em uma data específica, formatado para impressão.</div>
        </div>
      </div>
      <div class="nl-report-divider"></div>
      
      <c:if test="${not empty erro}">
        <div style="padding: 1.5rem; background: #fee2e2; border-left: 4px solid #ef4444; color: #991b1b; margin-bottom: 1rem;">
          <strong>Erro:</strong> ${erro}
        </div>
      </c:if>

      <div class="nl-report-card-body">
        <form method="get" action="${pageContext.request.contextPath}/relatorios/romaneio"
              target="_blank" class="nl-report-form">
          
          <div class="nl-form-group">
            <label class="nl-form-label" for="sel-motorista">Motorista <span style="color: #ef4444;">*</span></label>
            <select name="idMotorista" id="sel-motorista" class="nl-input-field" required>
              <option value="">Selecione um motorista…</option>
              <c:forEach var="m" items="${motoristas}">
                <option value="${m.id}"><c:out value="${m.nome}"/></option>
              </c:forEach>
            </select>
          </div>

          <div class="nl-form-group">
            <label class="nl-form-label" for="input-data">Data <span style="color: #ef4444;">*</span></label>
            <input type="date" name="data" id="input-data" class="nl-input-field" required>
          </div>

          <button type="submit" class="btn btn-primary" style="width: 100%;">
            <i class="bi bi-file-earmark-pdf-fill"></i> Gerar PDF
          </button>
        </form>

        <div style="margin-top: 1.5rem; padding: 1rem; background: #f0fdf4; border-radius: 6px; font-size: 0.85rem; color: #166534;">
          <strong>ℹ️ Dica:</strong> Selecione um motorista e a data de emissão dos fretes para gerar o romaneio. 
          O relatório incluirá todos os fretes do motorista naquela data com cabeçalho contendo dados do motorista e veículo, 
          além de totais de peso e volumes.
        </div>
      </div>
    </div>

    <div style="margin-top: 2rem; text-align: center;">
      <a href="${pageContext.request.contextPath}/relatorios" class="btn" style="background: #94a3b8; color: white; text-decoration: none;">
        <i class="bi bi-arrow-left"></i> Voltar
      </a>
    </div>
  </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>