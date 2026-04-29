<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="w-64 bg-slate-900 text-slate-200 flex flex-col">
    <div class="px-6 py-5 border-b border-slate-800">
        <img src="${pageContext.request.contextPath}/static/img/logo.svg" alt="NextLog" class="h-8 invert">
    </div>

    <nav class="flex-1 px-3 py-4 space-y-1 text-sm">
        <a href="${pageContext.request.contextPath}/fretes" class="block px-3 py-2 rounded hover:bg-slate-800">Fretes</a>
        <a href="${pageContext.request.contextPath}/clientes" class="block px-3 py-2 rounded hover:bg-slate-800">Clientes</a>
        <a href="${pageContext.request.contextPath}/motoristas" class="block px-3 py-2 rounded hover:bg-slate-800">Motoristas</a>
        <a href="${pageContext.request.contextPath}/veiculos" class="block px-3 py-2 rounded hover:bg-slate-800">Veículos</a>
        <a href="${pageContext.request.contextPath}/manutencoes" class="block px-3 py-2 rounded hover:bg-slate-800">Manutenções</a>
        <a href="${pageContext.request.contextPath}/rotas" class="block px-3 py-2 rounded hover:bg-slate-800">Tabela de Rotas</a>
        <a href="${pageContext.request.contextPath}/relatorios" class="block px-3 py-2 rounded hover:bg-slate-800">Relatórios</a>
    </nav>

    <div class="px-4 py-3 border-t border-slate-800 text-xs">
        <div class="text-slate-400 mb-1">Logado como</div>
        <div class="font-medium"><c:out value="${sessionScope.usuarioNome}" default="Usuário" /></div>
        <a href="${pageContext.request.contextPath}/auth/logout" class="text-teal-400 hover:text-teal-300 mt-2 inline-block">Sair</a>
    </div>
</aside>