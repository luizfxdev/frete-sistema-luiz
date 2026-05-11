<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="uri" value="${pageContext.request.requestURI}"/>
<style>
  .nl-sidebar {
    width: 240px;
    min-width: 240px;
    background: #292929;
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    position: sticky;
    top: 0;
    border-right: 1px solid rgba(255,255,255,.06);
  }
  .nl-sidebar-logo {
    display: flex;
    align-items: center;
    gap: .75rem;
    padding: 1.375rem 1.25rem 1.25rem;
    border-bottom: 1px solid rgba(255,255,255,.07);
    text-decoration: none;
  }
  .nl-sidebar-logo-icon {
    width: 34px;
    height: 34px;
    background: #3b82f6;
    border-radius: .5rem;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    box-shadow: 0 2px 8px rgba(59,130,246,.4);
  }
  .nl-sidebar-logo-icon svg { width: 18px; height: 18px; }
  .nl-sidebar-brand { display: flex; flex-direction: column; line-height: 1.1; }
  .nl-sidebar-brand-name { font-size: .95rem; font-weight: 700; color: #fff; letter-spacing: -.01em; }
  .nl-sidebar-brand-sub { font-size: .65rem; font-weight: 500; color: rgba(255,255,255,.35); text-transform: uppercase; letter-spacing: .1em; }
  .nl-sidebar-nav { flex: 1; padding: 1rem .75rem; display: flex; flex-direction: column; gap: .125rem; }
  .nl-nav-section-label {
    font-size: .65rem; font-weight: 700; text-transform: uppercase;
    letter-spacing: .12em; color: rgba(255,255,255,.2);
    padding: .75rem .75rem .25rem; margin-top: .375rem;
  }
  .nl-nav-section-label:first-child { margin-top: 0; }
  .nl-nav-link {
    display: flex; align-items: center; gap: .75rem;
    padding: .6rem .75rem; border-radius: .625rem;
    font-size: .82rem; font-weight: 500; color: rgba(255,255,255,.55);
    text-decoration: none; transition: background .15s, color .15s;
  }
  .nl-nav-link i { font-size: 1rem; width: 18px; text-align: center; flex-shrink: 0; }
  .nl-nav-link:hover { background: rgba(255,255,255,.07); color: rgba(255,255,255,.9); }
  .nl-nav-link.active { background: #3b82f6; color: #fff; font-weight: 600; box-shadow: 0 2px 8px rgba(59,130,246,.35); }
  .nl-nav-link.active i { color: #fff; }
  .nl-sidebar-divider { height: 1px; background: rgba(255,255,255,.07); margin: .5rem .75rem; }
  .nl-sidebar-footer { padding: 1rem 1.25rem; border-top: 1px solid rgba(255,255,255,.07); }
  .nl-sidebar-user { display: flex; align-items: center; gap: .75rem; margin-bottom: .875rem; }
  .nl-sidebar-avatar {
    width: 34px; height: 34px; border-radius: 50%;
    background: rgba(255,255,255,.1); display: flex; align-items: center; justify-content: center;
    flex-shrink: 0; font-size: .875rem; color: rgba(255,255,255,.7); font-weight: 700;
    border: 1.5px solid rgba(255,255,255,.12);
  }
  .nl-sidebar-user-info { min-width: 0; }
  .nl-sidebar-user-label { font-size: .65rem; color: rgba(255,255,255,.3); text-transform: uppercase; letter-spacing: .08em; font-weight: 600; }
  .nl-sidebar-user-name { font-size: .82rem; font-weight: 600; color: rgba(255,255,255,.85); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
  .nl-sidebar-logout {
    display: flex; align-items: center; gap: .5rem; width: 100%;
    padding: .5rem .75rem; border-radius: .5rem; font-size: .8rem; font-weight: 600;
    color: rgba(255,255,255,.4); background: transparent;
    border: 1px solid rgba(255,255,255,.1); text-decoration: none; transition: all .15s; cursor: pointer;
  }
  .nl-sidebar-logout:hover { background: rgba(239,68,68,.15); color: #fca5a5; border-color: rgba(239,68,68,.3); }
</style>

<c:set var="nomeUsuario"><c:out value="${sessionScope.usuarioNome}" default="Usuário"/></c:set>

<aside class="nl-sidebar">
  <a href="${pageContext.request.contextPath}/dashboard" class="nl-sidebar-logo">
    <div class="nl-sidebar-logo-icon">
      <svg viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg">
        <line x1="3" y1="15" x2="3" y2="3" stroke="white" stroke-width="2" stroke-linecap="round"/>
        <line x1="3" y1="3" x2="15" y2="15" stroke="white" stroke-width="2" stroke-linecap="round"/>
        <line x1="15" y1="15" x2="15" y2="3" stroke="white" stroke-width="2" stroke-linecap="round"/>
        <circle cx="3"  cy="3"  r="1.5" fill="white"/>
        <circle cx="15" cy="15" r="1.5" fill="white"/>
        <circle cx="15" cy="3"  r="1.5" fill="white"/>
      </svg>
    </div>
    <div class="nl-sidebar-brand">
      <span class="nl-sidebar-brand-name">NextLog</span>
      <span class="nl-sidebar-brand-sub">Gestão de Fretes</span>
    </div>
  </a>

  <nav class="nl-sidebar-nav">
    <span class="nl-nav-section-label">Principal</span>
    <a href="http://localhost:3000/dashboard"
       class="nl-nav-link">
      <i class="bi bi-graph-up"></i> Visão Geral
    </a>

    <div class="nl-sidebar-divider"></div>
    <span class="nl-nav-section-label">Operação</span>
    <a href="${pageContext.request.contextPath}/fretes"
       class="nl-nav-link ${fn:contains(uri, '/fretes') ? 'active' : ''}">
      <i class="bi bi-box-seam-fill"></i> Fretes
    </a>
    <a href="${pageContext.request.contextPath}/rotas"
       class="nl-nav-link ${fn:contains(uri, '/rotas') ? 'active' : ''}">
      <i class="bi bi-signpost-split-fill"></i> Tabela de Rotas
    </a>

    <div class="nl-sidebar-divider"></div>
    <span class="nl-nav-section-label">Cadastros</span>
    <a href="${pageContext.request.contextPath}/clientes"
       class="nl-nav-link ${fn:contains(uri, '/clientes') ? 'active' : ''}">
      <i class="bi bi-people-fill"></i> Clientes
    </a>
    <a href="${pageContext.request.contextPath}/motoristas"
       class="nl-nav-link ${fn:contains(uri, '/motoristas') ? 'active' : ''}">
      <i class="bi bi-person-badge-fill"></i> Motoristas
    </a>
    <a href="${pageContext.request.contextPath}/veiculos"
       class="nl-nav-link ${fn:contains(uri, '/veiculos') ? 'active' : ''}">
      <i class="bi bi-truck"></i> Veículos
    </a>

    <div class="nl-sidebar-divider"></div>
    <span class="nl-nav-section-label">Gestão</span>
    <a href="${pageContext.request.contextPath}/manutencoes"
       class="nl-nav-link ${fn:contains(uri, '/manutencoes') ? 'active' : ''}">
      <i class="bi bi-wrench-adjustable-circle-fill"></i> Manutenções
    </a>
    <a href="${pageContext.request.contextPath}/relatorios"
       class="nl-nav-link ${fn:contains(uri, '/relatorios') ? 'active' : ''}">
      <i class="bi bi-file-earmark-bar-graph-fill"></i> Relatórios
    </a>
  </nav>

  <div class="nl-sidebar-footer">
    <div class="nl-sidebar-user">
      <div class="nl-sidebar-avatar">${fn:toUpperCase(fn:substring(nomeUsuario, 0, 1))}</div>
      <div class="nl-sidebar-user-info">
        <div class="nl-sidebar-user-label">Logado como</div>
        <div class="nl-sidebar-user-name">${nomeUsuario}</div>
      </div>
    </div>
    <a href="${pageContext.request.contextPath}/auth/logout" class="nl-sidebar-logout">
      <i class="bi bi-box-arrow-right"></i> Sair da conta
    </a>
  </div>
</aside>