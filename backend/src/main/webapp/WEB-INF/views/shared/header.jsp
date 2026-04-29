<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NextLog - ${pageTitle != null ? pageTitle : 'Sistema de Gestão de Fretes'}</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/forms.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/status.css">
</head>
<body>
<div class="app-shell">