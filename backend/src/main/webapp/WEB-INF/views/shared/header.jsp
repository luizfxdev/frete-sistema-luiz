<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>NextLog — ${pageTitle != null ? pageTitle : 'Sistema de Gestão de Fretes'}</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
  <style>
    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

    :root {
      --brand:    #0a2540;
      --accent:   #005eff;
      --accent-d: #0049cc;
      --accent-l: #3d7fff;
      --success:  #22c55e;
      --cream:    #f8f6f1;
      --danger:   #ef4444;
      --warn:     #f59e0b;
      --info:     #06b6d4;
      --bg:       #f4f6f9;
      --radius:   0.75rem;
      --shadow:   0 1px 3px rgba(10,37,64,.08), 0 4px 16px rgba(10,37,64,.06);
      --font-primary:   'Avenir Next', 'DM Sans', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
      --font-secondary: 'Libre Baskerville', Georgia, serif;
    }

    html { scroll-behavior: smooth; -webkit-font-smoothing: antialiased; }

    body {
      font-family: var(--font-primary);
      background: var(--bg);
      color: var(--brand);
      line-height: 1.6;
    }

    a { color: inherit; text-decoration: none; }
    button { font-family: inherit; cursor: pointer; }
    img, video { max-width: 100%; display: block; }

    ::selection { background: var(--accent); color: #fff; }
    ::-webkit-scrollbar { width: 6px; }
    ::-webkit-scrollbar-track { background: var(--brand); }
    ::-webkit-scrollbar-thumb { background: var(--accent); border-radius: 3px; }
    ::-webkit-scrollbar-thumb:hover { background: var(--accent-l); }

    .app-shell { display: flex; min-height: 100vh; }
    .app-content { flex: 1; min-width: 0; padding: 2rem 2.5rem; }

    .btn {
      display: inline-flex;
      align-items: center;
      gap: .45rem;
      padding: .55rem 1.2rem;
      border-radius: var(--radius);
      font-size: .875rem;
      font-weight: 600;
      cursor: pointer;
      border: none;
      transition: background .18s, transform .12s, box-shadow .18s;
      text-decoration: none;
      white-space: nowrap;
    }
    .btn:active { transform: scale(.97); }
    .btn-primary   { background: var(--accent); color: #fff; box-shadow: 0 2px 8px rgba(0,94,255,.25); }
    .btn-primary:hover { background: var(--accent-d); }
    .btn-secondary { background: var(--brand); color: #fff; }
    .btn-secondary:hover { background: #0e304f; }
    .btn-sm  { padding: .35rem .8rem; font-size: .8rem; }
    .btn-danger { background: transparent; color: var(--danger); border: 1.5px solid var(--danger); }
    .btn-danger:hover { background: var(--danger); color: #fff; }

    .nl-card {
      background: #fff;
      border-radius: var(--radius);
      box-shadow: var(--shadow);
      border: 1px solid rgba(10,37,64,.07);
      padding: 1.5rem;
    }

    .nl-alert {
      display: flex; align-items: center; gap: .75rem;
      padding: 1rem 1.25rem; border-radius: var(--radius);
      margin-bottom: 1.5rem; font-size: .9rem; font-weight: 500;
    }
    .nl-alert-error   { background: #fef2f2; border: 1.5px solid #fecaca; color: #991b1b; }
    .nl-alert-success { background: #f0fdf4; border: 1.5px solid #bbf7d0; color: #166534; }

    .nl-kpi {
      background: #fff; border-radius: var(--radius); padding: 1.25rem 1.5rem;
      box-shadow: var(--shadow); display: flex; align-items: center; gap: 1rem;
      border: 1px solid rgba(10,37,64,.07); transition: box-shadow .2s;
    }
    .nl-kpi:hover { box-shadow: 0 4px 20px rgba(10,37,64,.12); }
    .nl-kpi-icon {
      width: 44px; height: 44px; border-radius: .625rem;
      display: flex; align-items: center; justify-content: center;
      font-size: 1.2rem; flex-shrink: 0;
    }
    .nl-kpi-icon.blue   { background: rgba(0,94,255,.1);   color: var(--accent); }
    .nl-kpi-icon.green  { background: rgba(34,197,94,.1);  color: var(--success); }
    .nl-kpi-icon.red    { background: rgba(239,68,68,.1);  color: var(--danger); }
    .nl-kpi-icon.amber  { background: rgba(245,158,11,.1); color: var(--warn); }
    .nl-kpi-icon.cyan   { background: rgba(6,182,212,.1);  color: var(--info); }
    .nl-kpi-icon.slate  { background: rgba(10,37,64,.08);  color: var(--brand); }
    .nl-kpi-label { font-size: .72rem; font-weight: 600; text-transform: uppercase; letter-spacing: .07em; color: #64748b; margin-bottom: .15rem; }
    .nl-kpi-value { font-size: 1.5rem; font-weight: 700; color: var(--brand); line-height: 1; }
    .nl-kpi-sub   { font-size: .75rem; color: #94a3b8; margin-top: .2rem; }

    .nl-table-card {
      background: #fff; border-radius: var(--radius); box-shadow: var(--shadow);
      border: 1px solid rgba(10,37,64,.07); overflow: hidden;
    }
    .nl-table { width: 100%; border-collapse: collapse; font-size: .875rem; }
    .nl-table thead { background: var(--brand); }
    .nl-table thead th {
      padding: .875rem 1.25rem; text-align: left;
      font-size: .72rem; font-weight: 700; text-transform: uppercase;
      letter-spacing: .08em; color: rgba(255,255,255,.65); white-space: nowrap;
    }
    .nl-table thead th:last-child { text-align: right; }
    .nl-table tbody tr { border-bottom: 1px solid #f1f5f9; transition: background .15s; }
    .nl-table tbody tr:last-child { border-bottom: none; }
    .nl-table tbody tr:hover { background: #f8fafc; }
    .nl-table td { padding: .9rem 1.25rem; color: #334155; vertical-align: middle; }
    .nl-table td:last-child { text-align: right; }
    .nl-empty { text-align: center; padding: 3rem 1.25rem; color: #94a3b8; font-size: .9rem; }
    .nl-empty i { font-size: 2rem; display: block; margin-bottom: .75rem; opacity: .4; }

    .nl-badge {
      display: inline-flex; align-items: center; gap: .3rem;
      padding: .25rem .65rem; border-radius: 999px;
      font-size: .72rem; font-weight: 700; text-transform: uppercase; letter-spacing: .06em;
    }
    .nl-badge::before { content: ''; width: 6px; height: 6px; border-radius: 50%; background: currentColor; opacity: .7; }
    .badge-ativo         { background: rgba(34,197,94,.12);  color: #15803d; }
    .badge-inativo       { background: rgba(100,116,139,.1); color: #475569; }
    .badge-suspenso      { background: rgba(245,158,11,.12); color: #92400e; }
    .badge-vencida       { background: rgba(239,68,68,.1);   color: #991b1b; margin-left: .35rem; }
    .badge-disponivel    { background: rgba(34,197,94,.12);  color: #15803d; }
    .badge-em-viagem     { background: rgba(0,94,255,.1);    color: #1d4ed8; }
    .badge-em-manutencao { background: rgba(245,158,11,.12); color: #92400e; }

    .nl-filter-card {
      background: #fff; border-radius: var(--radius); padding: 1rem 1.25rem;
      box-shadow: var(--shadow); margin-bottom: 1.25rem;
      border: 1px solid rgba(10,37,64,.07);
    }
    .nl-filter-row { display: flex; gap: .75rem; align-items: center; flex-wrap: wrap; }
    .nl-input {
      flex: 1; min-width: 200px; padding: .55rem 1rem;
      border: 1.5px solid #e2e8f0; border-radius: .625rem;
      font-size: .875rem; color: var(--brand); background: #f8fafc;
      font-family: inherit; transition: border-color .18s, box-shadow .18s; outline: none;
    }
    .nl-input:focus { border-color: var(--accent); box-shadow: 0 0 0 3px rgba(0,94,255,.12); background: #fff; }
    .nl-input::placeholder { color: #94a3b8; }

    .nl-field { display: flex; flex-direction: column; gap: .4rem; }
    .nl-label { font-size: .8rem; font-weight: 600; color: #475569; }
    .nl-select {
      padding: .55rem 1rem; border: 1.5px solid #e2e8f0; border-radius: .625rem;
      font-size: .875rem; color: var(--brand); background: #f8fafc;
      font-family: inherit; outline: none; transition: border-color .18s, box-shadow .18s;
      appearance: none;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='8' viewBox='0 0 12 8'%3E%3Cpath d='M1 1l5 5 5-5' stroke='%230a2540' stroke-width='1.5' fill='none' stroke-linecap='round'/%3E%3C/svg%3E");
      background-repeat: no-repeat;
      background-position: right .9rem center;
      padding-right: 2.5rem;
    }
    .nl-select:focus { border-color: var(--accent); box-shadow: 0 0 0 3px rgba(0,94,255,.12); background-color: #fff; }

    .nl-pagination {
      display: flex; align-items: center; justify-content: flex-end;
      gap: .375rem; padding: 1rem 1.25rem; border-top: 1px solid #f1f5f9;
    }
    .nl-pagination a, .nl-pagination span {
      display: inline-flex; align-items: center; justify-content: center;
      width: 34px; height: 34px; border-radius: .5rem; font-size: .82rem; font-weight: 600; text-decoration: none; transition: all .15s;
    }
    .nl-pagination a { color: var(--brand); border: 1.5px solid #e2e8f0; }
    .nl-pagination a:hover { background: var(--accent); color: #fff; border-color: var(--accent); }
    .nl-pagination span.active { background: var(--accent); color: #fff; border: 1.5px solid var(--accent); }

    .nl-page-header {
      display: flex; align-items: center; justify-content: space-between;
      margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;
    }
    .nl-page-header h1 { font-size: 1.625rem; font-weight: 700; color: var(--brand); letter-spacing: -.02em; }
    .nl-actions { display: flex; align-items: center; justify-content: flex-end; gap: .5rem; }

    .nl-td-name { font-weight: 600; color: var(--brand) !important; }
    .nl-td-mono { font-family: 'Courier New', monospace; font-size: .82rem; color: #64748b !important; }

    @media (max-width: 768px) {
      .app-content { padding: 1.25rem 1rem; }
    }
  </style>
</head>
<body>
<div class="app-shell">