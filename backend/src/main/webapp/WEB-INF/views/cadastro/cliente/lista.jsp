<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Clientes" scope="request"/>
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
  
  .nl-kpi-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 1.5rem; margin-bottom: 2rem; width: 100%; }
  
  .nl-kpi { background: white; padding: 1.5rem; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); display: flex; align-items: flex-start; gap: 1rem; }
  .nl-kpi-icon { width: 50px; height: 50px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 1.5rem; flex-shrink: 0; }
  .nl-kpi-icon.blue { background: #dbeafe; color: #3b82f6; }
  .nl-kpi-icon.green { background: #dcfce7; color: #16a34a; }
  .nl-kpi-icon.slate { background: #f1f5f9; color: #475569; }
  .nl-kpi-icon.amber { background: #fef3c7; color: #d97706; }
  .nl-kpi-label { font-size: 0.75rem; color: #94a3b8; font-weight: 600; text-transform: uppercase; margin-bottom: 0.25rem; }
  .nl-kpi-value { font-size: 1.75rem; font-weight: 700; color: #0f172a; }
  .nl-kpi-sub { font-size: 0.8rem; color: #94a3b8; }
  
  .nl-filter-card { display: flex; align-items: center; gap: 1rem; padding: 1rem; background: white; border-radius: 8px; margin-bottom: 2rem; box-shadow: 0 1px 3px rgba(0,0,0,0.1); width: 100%; }
  .nl-filter-row { display: flex; align-items: center; gap: 1rem; flex: 1; }
  .nl-filter-row i { color: #94a3b8; flex-shrink: 0; }
  .nl-filter-row input { flex: 1; min-width: 200px; padding: 0.75rem; border: 1px solid #e2e8f0; border-radius: 6px; font-family: 'Avenir Next', sans-serif; font-size: 1rem; }
  
  .nl-alert { padding: 1rem; border-radius: 6px; margin-bottom: 1.5rem; display: flex; align-items: flex-start; gap: 0.75rem; width: 100%; }
  .nl-alert-error { background: #fee2e2; color: #991b1b; }
  
  .nl-table-card { background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 1px 3px rgba(0,0,0,0.1); width: 100%; }
  .nl-table { width: 100%; border-collapse: collapse; }
  .nl-table thead { background: #292929; color: white; }
  .nl-table th { padding: 1rem; text-align: left; font-size: 0.85rem; font-weight: 600; letter-spacing: 0.5px; }
  .nl-table td { padding: 1rem; border-bottom: 1px solid #e2e8f0; }
  .nl-table tbody tr:hover { background: #f8fafc; }
  
  .nl-td-name { font-weight: 500; color: #1e293b; }
  .nl-td-mono { font-family: 'Courier New', monospace; color: #64748b; font-size: 0.9rem; }
  
  .nl-badge { display: inline-block; padding: 0.35rem 0.75rem; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
  .badge-ativo { background: #dcfce7; color: #166534; }
  .badge-inativo { background: #fee2e2; color: #991b1b; }
  
  .nl-actions { display: flex; gap: 0.5rem; }
  .btn { padding: 0.75rem 1.5rem; border-radius: 6px; border: none; cursor: pointer; font-weight: 500; text-decoration: none; display: inline-flex; align-items: center; gap: 0.4rem; font-size: 0.9rem; font-family: 'Avenir Next', sans-serif; }
  .btn-primary { background: #3b82f6; color: white; }
  .btn-primary:hover { background: #2563eb; }
  .btn-secondary { background: #d3d3d3; color: #333; }
  .btn-secondary:hover { background: #c0c0c0; }
  .btn-sm { padding: 0.5rem 1rem; font-size: 0.85rem; }
  .btn-danger { background: #fca5a5; color: #7f1d1d; }
  .btn-danger:hover { background: #f87171; }
  
  .nl-empty { text-align: center; padding: 3rem; color: #94a3b8; }
  .nl-empty i { font-size: 2.5rem; margin-bottom: 1rem; display: block; }
  
  .nl-pagination { display: flex; justify-content: center; gap: 0.5rem; margin-top: 1.5rem; padding: 1rem 0; flex-wrap: wrap; }
  .nl-pagination a, .nl-pagination span { padding: 0.5rem 0.75rem; border-radius: 4px; font-size: 0.9rem; }
  .nl-pagination a { background: #f1f5f9; color: #0f172a; cursor: pointer; }
  .nl-pagination a:hover { background: #e2e8f0; }
  .nl-pagination span.active { background: #292929; color: white; }
  
  @media (max-width: 1400px) { 
    .nl-kpi-row { grid-template-columns: repeat(2, 1fr); }
  }
  
  @media (max-width: 1024px) { 
    .nl-main { padding: 1.5rem; }
    .nl-container { max-width: 100%; }
    .nl-kpi-row { grid-template-columns: repeat(2, 1fr); gap: 1rem; }
    .nl-filter-row { flex-direction: column; align-items: stretch; }
    .nl-filter-row input { width: 100%; }
    .nl-table { font-size: 0.9rem; }
    .nl-table th, .nl-table td { padding: 0.75rem; }
  }
  
  @media (max-width: 768px) { 
    .nl-main { padding: 1rem; }
    .nl-page-header { flex-direction: column; gap: 1rem; align-items: flex-start; }
    .nl-kpi-row { grid-template-columns: 1fr; }
    .nl-table { font-size: 0.8rem; }
    .nl-table th, .nl-table td { padding: 0.5rem; }
    .nl-actions { flex-direction: column; }
  }
</style>

<main class="nl-main">
  <div class="nl-container">
    <div class="nl-page-header">
      <h1><i class="bi bi-people-fill" style="color: #3b82f6; margin-right: 0.75rem;"></i>Clientes</h1>
      <a href="${pageContext.request.contextPath}/clientes/novo" class="btn btn-primary">
        <i class="bi bi-plus-lg"></i> Novo cliente
      </a>
    </div>

    <c:if test="${not empty erro}">
      <div class="nl-alert nl-alert-error">
        <i class="bi bi-exclamation-triangle-fill"></i>
        <div><c:out value="${erro}"/></div>
      </div>
    </c:if>

    <div class="nl-kpi-row">
      <div class="nl-kpi">
        <div class="nl-kpi-icon blue"><i class="bi bi-people-fill"></i></div>
        <div>
          <div class="nl-kpi-label">Total</div>
          <div class="nl-kpi-value">${totalClientes != null ? totalClientes : 0}</div>
          <div class="nl-kpi-sub">clientes cadastrados</div>
        </div>
      </div>
      <div class="nl-kpi">
        <div class="nl-kpi-icon green"><i class="bi bi-check-circle-fill"></i></div>
        <div>
          <div class="nl-kpi-label">Ativos</div>
          <div class="nl-kpi-value">${totalAtivos != null ? totalAtivos : 0}</div>
          <div class="nl-kpi-sub">em operação</div>
        </div>
      </div>
      <div class="nl-kpi">
        <div class="nl-kpi-icon slate"><i class="bi bi-building"></i></div>
        <div>
          <div class="nl-kpi-label">PJ</div>
          <div class="nl-kpi-value">${totalPJ != null ? totalPJ : 0}</div>
          <div class="nl-kpi-sub">pessoa jurídica</div>
        </div>
      </div>
      <div class="nl-kpi">
        <div class="nl-kpi-icon amber"><i class="bi bi-person-fill"></i></div>
        <div>
          <div class="nl-kpi-label">PF</div>
          <div class="nl-kpi-value">${totalPF != null ? totalPF : 0}</div>
          <div class="nl-kpi-sub">pessoa física</div>
        </div>
      </div>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/clientes" class="nl-filter-card">
      <div class="nl-filter-row">
        <i class="bi bi-search"></i>
        <input type="text" name="filtro" value="<c:out value='${filtro}'/>" placeholder="Filtrar por razão social…">
        <button type="submit" class="btn btn-secondary">
          <i class="bi bi-funnel-fill"></i> Filtrar
        </button>
        <c:if test="${not empty filtro}">
          <a href="${pageContext.request.contextPath}/clientes" class="btn btn-sm btn-secondary">
            <i class="bi bi-x-lg"></i> Limpar
          </a>
        </c:if>
      </div>
    </form>

    <div class="nl-table-card">
      <table class="nl-table">
        <thead>
          <tr>
            <th>Razão Social</th>
            <th>Documento</th>
            <th>Tipo</th>
            <th>Município / UF</th>
            <th>Status</th>
            <th style="text-align: center;">Ações</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="c" items="${clientes}">
            <tr>
              <td class="nl-td-name"><c:out value="${c.razaoSocial}"/></td>
              <td class="nl-td-mono">
                <c:choose>
                  <c:when test="${c.tipoDocumento == 'CPF'}">
                    <c:out value="${fn:substring(c.documento, 0, 3)}.${fn:substring(c.documento, 3, 6)}.${fn:substring(c.documento, 6, 9)}-${fn:substring(c.documento, 9, 11)}"/>
                  </c:when>
                  <c:otherwise>
                    <c:out value="${fn:substring(c.documento, 0, 2)}.${fn:substring(c.documento, 2, 5)}.${fn:substring(c.documento, 5, 8)}/0001-${fn:substring(c.documento, 8, 12)}"/>
                  </c:otherwise>
                </c:choose>
              </td>
              <td><c:out value="${c.tipoNome}"/></td>
              <td><c:out value="${c.municipio}"/> / <c:out value="${c.uf}"/></td>
              <td>
                <span class="nl-badge ${fn:toLowerCase(c.status) == 'ativo' ? 'badge-ativo' : 'badge-inativo'}">
                  <c:out value="${c.status}"/>
                </span>
              </td>
              <td style="text-align: center;">
                <div class="nl-actions">
                  <a href="${pageContext.request.contextPath}/clientes/editar/${c.id}" class="btn btn-sm btn-secondary">
                    <i class="bi bi-pencil-fill"></i> Editar
                  </a>
                  <form method="post" action="${pageContext.request.contextPath}/clientes/excluir/${c.id}" style="display: inline; margin: 0;">
                    <button type="submit" class="btn btn-sm btn-danger" data-confirm="Confirmar exclusão do cliente?">
                      <i class="bi bi-trash3-fill"></i>
                    </button>
                  </form>
                </div>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty clientes}">
            <tr>
              <td colspan="6">
                <div class="nl-empty">
                  <i class="bi bi-inbox"></i>
                  Nenhum cliente cadastrado.
                </div>
              </td>
            </tr>
          </c:if>
        </tbody>
      </table>
      <c:if test="${totalPaginas > 1}">
        <div class="nl-pagination">
          <c:if test="${pagina > 1}">
            <a href="${pageContext.request.contextPath}/clientes?pagina=1<c:if test='${not empty filtro}'>&amp;filtro=<c:out value='${filtro}'/></c:if>">« Primeira</a>
            <a href="${pageContext.request.contextPath}/clientes?pagina=${pagina - 1}<c:if test='${not empty filtro}'>&amp;filtro=<c:out value='${filtro}'/></c:if>">‹ Anterior</a>
          </c:if>
          
          <c:forEach begin="1" end="${totalPaginas}" var="p">
            <c:choose>
              <c:when test="${p == pagina}"><span class="active">${p}</span></c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/clientes?pagina=${p}<c:if test='${not empty filtro}'>&amp;filtro=<c:out value='${filtro}'/></c:if>">${p}</a>
              </c:otherwise>
            </c:choose>
          </c:forEach>
          
          <c:if test="${pagina < totalPaginas}">
            <a href="${pageContext.request.contextPath}/clientes?pagina=${pagina + 1}<c:if test='${not empty filtro}'>&amp;filtro=<c:out value='${filtro}'/></c:if>">Próxima ›</a>
            <a href="${pageContext.request.contextPath}/clientes?pagina=${totalPaginas}<c:if test='${not empty filtro}'>&amp;filtro=<c:out value='${filtro}'/></c:if>">Última »</a>
          </c:if>
        </div>
      </c:if>
    </div>
  </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/nl-confirm.js"></script>