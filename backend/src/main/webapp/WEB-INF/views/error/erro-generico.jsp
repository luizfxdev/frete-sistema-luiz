<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Erro - NextLog</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 min-h-screen flex items-center justify-center">
<div class="bg-white border border-slate-200 rounded-lg shadow-sm p-8 max-w-md text-center">
    <div class="text-5xl mb-4">⚠️</div>
    <h1 class="text-xl font-semibold text-slate-900 mb-2">Algo deu errado</h1>
    <p class="text-sm text-slate-600 mb-6">
        Ocorreu um erro inesperado ao processar sua solicitação. Tente novamente em alguns instantes.
    </p>
    <a href="${pageContext.request.contextPath}/fretes" class="inline-block px-4 py-2 bg-teal-700 text-white rounded hover:bg-teal-800 text-sm">
        Voltar
    </a>
</div>
</body>
</html>