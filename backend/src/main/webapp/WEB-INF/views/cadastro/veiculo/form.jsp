<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Veículo" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main style="padding: 24px; background-color: #ffffff; margin-left: 200px; min-height: 100vh;">
    <h1 style="font-size: 28px; font-weight: 600; color: #1f2937; margin: 0 0 24px 0; font-family: 'Avenir Next', sans-serif;">
        ${veiculo.id == null ? 'Novo veículo' : 'Editar veículo'}
    </h1>

    <c:if test="${not empty erro}">
        <div style="background-color: #fee2e2; color: #991b1b; padding: 12px 16px; border-radius: 6px; margin-bottom: 24px; border-left: 4px solid #991b1b; font-family: 'Avenir Next', sans-serif;">
            <c:out value="${erro}"/>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/veiculos" style="background-color: #f9fafb; border: 1px solid #e5e7eb; border-radius: 8px; padding: 24px;">
        <input type="hidden" name="id" value="<c:out value='${veiculo.id}'/>"/>

        <!-- Seção: Identificação -->
        <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 24px;">
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Placa <span style="color: #dc2626;">*</span></label>
                <input type="text" name="placa" required maxlength="7" data-mask="placa" value="<c:out value='${veiculo.placa}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">RNTRC</label>
                <input type="text" name="rntrc" value="<c:out value='${veiculo.rntrc}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Ano de fabricação <span style="color: #dc2626;">*</span></label>
                <input type="number" name="anoFabricacao" required min="1980" max="2100" value="<c:out value='${veiculo.anoFabricacao}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Tipo <span style="color: #dc2626;">*</span></label>
                <select name="tipo" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
                    <option value="">Selecione</option>
                    <c:forEach var="t" items="${tipos}">
                        <option value="${t}" ${veiculo.tipo == t ? 'selected' : ''}>${t}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <!-- Seção: Especificações -->
        <div style="border-top: 1px solid #d1d5db; padding-top: 24px; margin-bottom: 24px;">
            <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Especificações</h3>
            <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Tara (kg) <span style="color: #dc2626;">*</span></label>
                    <input type="number" step="0.01" name="taraKg" required value="<c:out value='${veiculo.taraKg}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Capacidade (kg) <span style="color: #dc2626;">*</span></label>
                    <input type="number" step="0.01" name="capacidadeKg" required value="<c:out value='${veiculo.capacidadeKg}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Volume (m³)</label>
                    <input type="number" step="0.01" name="volumeM3" value="<c:out value='${veiculo.volumeM3}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
            </div>
        </div>

        <!-- Seção: Status -->
        <div style="border-top: 1px solid #d1d5db; padding-top: 24px; margin-bottom: 24px;">
            <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Status</h3>
            <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Status</label>
                    <select name="status" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
                        <c:forEach var="s" items="${statusVeiculo}">
                            <option value="${s}" ${veiculo.status == s ? 'selected' : ''}>${s}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div style="display: flex; gap: 12px; justify-content: flex-end; border-top: 1px solid #d1d5db; padding-top: 24px;">
            <a href="${pageContext.request.contextPath}/veiculos" style="background-color: #d3d3d3; color: #333; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; text-decoration: none; font-family: 'Avenir Next', sans-serif; display: inline-block;">Cancelar</a>
            <button type="submit" style="background-color: #2563eb; color: white; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; font-family: 'Avenir Next', sans-serif;">Salvar</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>