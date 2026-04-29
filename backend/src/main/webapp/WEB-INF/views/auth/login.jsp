<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NextLog - Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
</head>
<body class="bg-slate-50 min-h-screen flex items-center justify-center">

<div class="bg-white border border-slate-200 rounded-lg shadow-sm w-full max-w-md p-8">
    <div class="text-center mb-6">
        <img src="${pageContext.request.contextPath}/static/img/logo.svg" alt="NextLog" class="h-10 mx-auto mb-3">
        <p class="text-sm text-slate-600">Sistema de Gestão de Fretes</p>
    </div>

    <c:if test="${not empty erro}">
        <div class="bg-red-100 border border-red-300 text-red-800 px-3 py-2 rounded mb-4 text-sm">
            <c:out value="${erro}" />
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/auth/login" class="space-y-4">
        <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">E-mail</label>
            <input type="email" name="email" required value="<c:out value='${email}'/>"
                   class="w-full px-3 py-2 border border-slate-300 rounded focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none text-sm">
        </div>
        <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">Senha</label>
            <input type="password" name="senha" required
                   class="w-full px-3 py-2 border border-slate-300 rounded focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none text-sm">
        </div>
        <button type="submit" class="w-full bg-teal-700 hover:bg-teal-800 text-white py-2 rounded text-sm font-medium">
            Entrar
        </button>
    </form>
</div>

</body>
</html>