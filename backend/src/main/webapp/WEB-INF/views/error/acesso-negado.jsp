<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Acesso negado - NextLog</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 min-h-screen flex items-center justify-center">
<div class="bg-white border border-slate-200 rounded-lg shadow-sm p-8 max-w-md text-center">
    <h1 class="text-xl font-semibold text-slate-900 mb-2">Acesso negado</h1>
    <p class="text-sm text-slate-600 mb-6">Você não tem permissão para acessar esta página.</p>
    <a href="${pageContext.request.contextPath}/auth/login" class="inline-block px-4 py-2 bg-teal-700 text-white rounded hover:bg-teal-800 text-sm">
        Ir para o login
    </a>
</div>
</body>
</html>