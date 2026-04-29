<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Relatórios" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main class="app-content">
    <h1 class="text-2xl font-semibold text-slate-900 mb-6">Relatórios</h1>

    <div class="grid grid-cols-2 gap-4">
        <div class="card">
            <h2 class="font-semibold mb-2 text-slate-700">Fretes em aberto</h2>
            <p class="text-sm text-slate-600 mb-4">Lista de todos os fretes ainda não finalizados, com dias em atraso.</p>
            <a href="${pageContext.request.contextPath}/relatorios/fretes-em-aberto" class="btn btn-primary" target="_blank">Gerar PDF</a>
        </div>

        <div class="card">
            <h2 class="font-semibold mb-2 text-slate-700">Romaneio de carga</h2>
            <p class="text-sm text-slate-600 mb-4">Fretes de um motorista em uma data específica.</p>
            <form method="get" action="${pageContext.request.contextPath}/relatorios/romaneio" target="_blank" class="space-y-3">
                <div class="field">
                    <label>Motorista</label>
                    <select name="idMotorista" required>
                        <option value="">Selecione</option>
                        <c:forEach var="m" items="${motoristas}">
                            <option value="${m.id}">${m.nome}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="field">
                    <label>Data</label>
                    <input type="date" name="data" required>
                </div>
                <button type="submit" class="btn btn-primary">Gerar PDF</button>
            </form>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>