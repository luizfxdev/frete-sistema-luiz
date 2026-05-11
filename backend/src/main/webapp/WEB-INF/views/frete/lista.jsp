<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Fretes" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main style="padding: 24px; background-color: #ffffff; margin-left: 200px; min-height: 100vh;">
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
    <h1 style="font-size: 28px; font-weight: 600; color: #1f2937; margin: 0;">Fretes</h1>
    <a href="${pageContext.request.contextPath}/fretes/novo" style="background-color: #2563eb; color: white; padding: 10px 16px; border-radius: 6px; text-decoration: none; font-weight: 500; display: inline-block; font-family: 'Avenir Next', sans-serif;">+ Novo frete</a>
  </div>

  <c:if test="${not empty erro}">
    <div style="background-color: #fee2e2; color: #991b1b; padding: 12px; border-radius: 6px; margin-bottom: 16px; font-family: 'Avenir Next', sans-serif;">
      <c:out value="${erro}"/>
    </div>
  </c:if>

  <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 16px; margin-bottom: 24px;">
    <div style="background-color: #f0f9ff; border: 1px solid #bfdbfe; border-radius: 8px; padding: 16px;">
      <div style="display: flex; align-items: center; gap: 12px;">
        <div style="width: 40px; height: 40px; background-color: #3b82f6; border-radius: 6px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px;">📦</div>
        <div>
          <div style="font-size: 12px; color: #64748b; font-weight: 500; font-family: 'Avenir Next', sans-serif;">Total</div>
          <div style="font-size: 24px; font-weight: 700; color: #1f2937; font-family: 'Avenir Next', sans-serif;">${totalFretes != null ? totalFretes : 0}</div>
          <div style="font-size: 11px; color: #94a3b8; font-family: 'Avenir Next', sans-serif;">fretes cadastrados</div>
        </div>
      </div>
    </div>

    <div style="background-color: #eff6ff; border: 1px solid #bfdbfe; border-radius: 8px; padding: 16px;">
      <div style="display: flex; align-items: center; gap: 12px;">
        <div style="width: 40px; height: 40px; background-color: #0369a1; border-radius: 6px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px;">📝</div>
        <div>
          <div style="font-size: 12px; color: #64748b; font-weight: 500; font-family: 'Avenir Next', sans-serif;">Emitido</div>
          <div style="font-size: 24px; font-weight: 700; color: #1f2937; font-family: 'Avenir Next', sans-serif;">${totalEmitido != null ? totalEmitido : 0}</div>
          <div style="font-size: 11px; color: #94a3b8; font-family: 'Avenir Next', sans-serif;">aguardando saída</div>
        </div>
      </div>
    </div>

    <div style="background-color: #eff6ff; border: 1px solid #bfdbfe; border-radius: 8px; padding: 16px;">
      <div style="display: flex; align-items: center; gap: 12px;">
        <div style="width: 40px; height: 40px; background-color: #1e40af; border-radius: 6px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px;">🚚</div>
        <div>
          <div style="font-size: 12px; color: #64748b; font-weight: 500; font-family: 'Avenir Next', sans-serif;">Em Trânsito</div>
          <div style="font-size: 24px; font-weight: 700; color: #1f2937; font-family: 'Avenir Next', sans-serif;">${totalEmTransito != null ? totalEmTransito : 0}</div>
          <div style="font-size: 11px; color: #94a3b8; font-family: 'Avenir Next', sans-serif;">em andamento</div>
        </div>
      </div>
    </div>

    <div style="background-color: #f0fdf4; border: 1px solid #bbf7d0; border-radius: 8px; padding: 16px;">
      <div style="display: flex; align-items: center; gap: 12px;">
        <div style="width: 40px; height: 40px; background-color: #16a34a; border-radius: 6px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px;">✓</div>
        <div>
          <div style="font-size: 12px; color: #64748b; font-weight: 500; font-family: 'Avenir Next', sans-serif;">Entregue</div>
          <div style="font-size: 24px; font-weight: 700; color: #1f2937; font-family: 'Avenir Next', sans-serif;">${totalEntregue != null ? totalEntregue : 0}</div>
          <div style="font-size: 11px; color: #94a3b8; font-family: 'Avenir Next', sans-serif;">concluídos</div>
        </div>
      </div>
    </div>

    <div style="background-color: #fef2f2; border: 1px solid #fecaca; border-radius: 8px; padding: 16px;">
      <div style="display: flex; align-items: center; gap: 12px;">
        <div style="width: 40px; height: 40px; background-color: #dc2626; border-radius: 6px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px;">✕</div>
        <div>
          <div style="font-size: 12px; color: #64748b; font-weight: 500; font-family: 'Avenir Next', sans-serif;">Cancelado</div>
          <div style="font-size: 24px; font-weight: 700; color: #1f2937; font-family: 'Avenir Next', sans-serif;">${totalCancelado != null ? totalCancelado : 0}</div>
          <div style="font-size: 11px; color: #94a3b8; font-family: 'Avenir Next', sans-serif;">não entregues</div>
        </div>
      </div>
    </div>
  </div>

  <form method="get" action="${pageContext.request.contextPath}/fretes" style="margin-bottom: 24px;">
    <div style="display: flex; gap: 12px; flex-wrap: wrap;">
      <input type="text" name="filtro" value="<c:out value='${filtro}'/>" placeholder="Número, remetente ou destinatário" style="flex: 1; min-width: 250px; padding: 8px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
      
      <select name="statusFiltro" style="padding: 8px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
        <option value="">Todos os status</option>
        <option value="EMITIDO" ${statusFiltro == 'EMITIDO' ? 'selected' : ''}>Emitido</option>
        <option value="SAIDA_CONFIRMADA" ${statusFiltro == 'SAIDA_CONFIRMADA' ? 'selected' : ''}>Saída Confirmada</option>
        <option value="EM_TRANSITO" ${statusFiltro == 'EM_TRANSITO' ? 'selected' : ''}>Em Trânsito</option>
        <option value="ENTREGUE" ${statusFiltro == 'ENTREGUE' ? 'selected' : ''}>Entregue</option>
        <option value="CANCELADO" ${statusFiltro == 'CANCELADO' ? 'selected' : ''}>Cancelado</option>
      </select>
      
      <button type="submit" style="background-color: #d3d3d3; color: #333; padding: 8px 20px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; font-family: 'Avenir Next', sans-serif;">Filtrar</button>
    </div>
  </form>

  <div style="overflow-x: auto; border: 1px solid #e5e7eb; border-radius: 6px; background-color: #ffffff;">
    <table style="width: 100%; border-collapse: collapse; font-family: 'Avenir Next', sans-serif; font-size: 14px;">
      <thead>
        <tr style="background-color: #1f2937; border-bottom: 2px solid #d1d5db;">
          <th style="padding: 12px; text-align: left; font-weight: 600; color: white;">Número</th>
          <th style="padding: 12px; text-align: left; font-weight: 600; color: white;">Remetente</th>
          <th style="padding: 12px; text-align: left; font-weight: 600; color: white;">Destinatário</th>
          <th style="padding: 12px; text-align: left; font-weight: 600; color: white;">Motorista</th>
          <th style="padding: 12px; text-align: left; font-weight: 600; color: white;">Rota</th>
          <th style="padding: 12px; text-align: left; font-weight: 600; color: white;">Emissão</th>
          <th style="padding: 12px; text-align: center; font-weight: 600; color: white;">Status</th>
          <th style="padding: 12px; text-align: right; font-weight: 600; color: white;">Ação</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="f" items="${fretes}">
          <tr style="border-bottom: 1px solid #e5e7eb; background-color: #ffffff;">
            <td style="padding: 12px; color: #374151; font-family: monospace;">${f.numero}</td>
            <td style="padding: 12px; color: #374151;"><c:out value="${f.remetenteRazaoSocial}"/></td>
            <td style="padding: 12px; color: #374151;"><c:out value="${f.destinatarioRazaoSocial}"/></td>
            <td style="padding: 12px; color: #374151;"><c:out value="${f.motoristaNome}"/></td>
            <td style="padding: 12px; color: #374151; white-space: nowrap;">${f.municipioOrigem}/${f.ufOrigem} → ${f.municipioDestino}/${f.ufDestino}</td>
            <td style="padding: 12px; color: #374151;"><c:out value="${f.dataEmissao.dayOfMonth}"/>/<c:out value="${f.dataEmissao.monthValue < 10 ? '0' : ''}${f.dataEmissao.monthValue}"/>/<c:out value="${f.dataEmissao.year}"/></td>
            <td style="padding: 12px; text-align: center;">
              <c:choose>
                <c:when test="${f.status == 'EMITIDO'}">
                  <span style="background-color: #e0f2fe; color: #0369a1; padding: 4px 8px; border-radius: 4px; font-weight: 500; font-size: 12px;">Emitido</span>
                </c:when>
                <c:when test="${f.status == 'SAIDA_CONFIRMADA'}">
                  <span style="background-color: #fef3c7; color: #92400e; padding: 4px 8px; border-radius: 4px; font-weight: 500; font-size: 12px;">Saída Confirmada</span>
                </c:when>
                <c:when test="${f.status == 'EM_TRANSITO'}">
                  <span style="background-color: #dbeafe; color: #1e40af; padding: 4px 8px; border-radius: 4px; font-weight: 500; font-size: 12px;">Em Trânsito</span>
                </c:when>
                <c:when test="${f.status == 'ENTREGUE'}">
                  <span style="background-color: #dcfce7; color: #166534; padding: 4px 8px; border-radius: 4px; font-weight: 500; font-size: 12px;">Entregue</span>
                </c:when>
                <c:when test="${f.status == 'CANCELADO'}">
                  <span style="background-color: #fee2e2; color: #991b1b; padding: 4px 8px; border-radius: 4px; font-weight: 500; font-size: 12px;">Cancelado</span>
                </c:when>
                <c:otherwise>
                  <span style="background-color: #f3f4f6; color: #6b7280; padding: 4px 8px; border-radius: 4px; font-weight: 500; font-size: 12px;">${f.status}</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td style="padding: 12px; text-align: right;">
              <a href="${pageContext.request.contextPath}/fretes/detalhe/${f.id}" style="background-color: #d3d3d3; color: #333; padding: 6px 12px; border-radius: 4px; text-decoration: none; font-weight: 500; font-size: 12px; display: inline-block; font-family: 'Avenir Next', sans-serif;">Detalhe</a>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty fretes}">
          <tr style="background-color: #f9fafb;">
            <td colspan="8" style="padding: 32px; text-align: center; color: #6b7280; font-family: 'Avenir Next', sans-serif;">Nenhum frete encontrado.</td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>

  <c:if test="${totalPaginas > 1}">
    <div style="display: flex; justify-content: center; gap: 8px; margin-top: 24px;">
      <c:forEach begin="1" end="${totalPaginas}" var="p">
        <c:choose>
          <c:when test="${p == pagina}">
            <span style="background-color: #1f2937; color: white; padding: 6px 10px; border-radius: 4px; font-size: 12px; min-width: 32px; text-align: center; font-family: 'Avenir Next', sans-serif;">${p}</span>
          </c:when>
          <c:otherwise>
            <a href="${pageContext.request.contextPath}/fretes?pagina=${p}<c:if test='${not empty filtro}'>&filtro=<c:out value='${filtro}'/></c:if><c:if test='${not empty statusFiltro}'>&statusFiltro=<c:out value='${statusFiltro}'/></c:if>" style="background-color: #f3f4f6; color: #333; padding: 6px 10px; border-radius: 4px; text-decoration: none; font-size: 12px; min-width: 32px; text-align: center; border: 1px solid #d1d5db; display: inline-block; font-family: 'Avenir Next', sans-serif;">${p}</a>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </div>
  </c:if>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>