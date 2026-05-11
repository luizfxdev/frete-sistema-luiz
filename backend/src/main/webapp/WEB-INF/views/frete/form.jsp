<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Novo frete" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main style="padding: 24px; background-color: #ffffff; margin-left: 200px; min-height: 100vh;">
  <h1 style="font-size: 28px; font-weight: 600; color: #1f2937; margin: 0 0 24px 0; font-family: 'Avenir Next', sans-serif;">Novo frete</h1>

  <c:if test="${not empty erro}">
    <div style="background-color: #fee2e2; color: #991b1b; padding: 12px 16px; border-radius: 6px; margin-bottom: 24px; border-left: 4px solid #991b1b; font-family: 'Avenir Next', sans-serif;">
      <c:out value="${erro}"/>
    </div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/fretes" style="background-color: #f9fafb; border: 1px solid #e5e7eb; border-radius: 8px; padding: 24px;">

    <!-- Seção: Seleções (Remetente, Destinatário, Motorista, Veículo) -->
    <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 24px;">
      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Remetente <span style="color: #dc2626;">*</span></label>
        <select name="idRemetente" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
          <option value="">Selecione</option>
          <c:forEach var="c" items="${clientes}">
            <option value="${c.id}" ${frete.idRemetente == c.id ? 'selected' : ''}>${c.razaoSocial}</option>
          </c:forEach>
        </select>
      </div>

      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Destinatário <span style="color: #dc2626;">*</span></label>
        <select name="idDestinatario" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
          <option value="">Selecione</option>
          <c:forEach var="c" items="${clientes}">
            <option value="${c.id}" ${frete.idDestinatario == c.id ? 'selected' : ''}>${c.razaoSocial}</option>
          </c:forEach>
        </select>
      </div>

      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Motorista <span style="color: #dc2626;">*</span></label>
        <select name="idMotorista" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
          <option value="">Selecione</option>
          <c:forEach var="m" items="${motoristas}">
            <option value="${m.id}" ${frete.idMotorista == m.id ? 'selected' : ''}>${m.nome}</option>
          </c:forEach>
        </select>
      </div>

      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Veículo <span style="color: #dc2626;">*</span></label>
        <select name="idVeiculo" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
          <option value="">Selecione</option>
          <c:forEach var="v" items="${veiculos}">
            <option value="${v.id}" ${frete.idVeiculo == v.id ? 'selected' : ''}>${v.placa} - ${v.tipo}</option>
          </c:forEach>
        </select>
      </div>
    </div>

    <!-- Seção: Localização -->
    <div style="border-top: 1px solid #d1d5db; padding-top: 24px; margin-bottom: 24px;">
      <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Localização</h3>
      <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px;">
        <div style="display: flex; flex-direction: column;">
          <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Município origem <span style="color: #dc2626;">*</span></label>
          <input type="text" name="municipioOrigem" required value="<c:out value='${frete.municipioOrigem}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
        </div>
        <div style="display: flex; flex-direction: column;">
          <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">UF origem <span style="color: #dc2626;">*</span></label>
          <input type="text" name="ufOrigem" required maxlength="2" value="<c:out value='${frete.ufOrigem}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; text-transform: uppercase;">
        </div>
        <div style="display: flex; flex-direction: column;">
          <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Município destino <span style="color: #dc2626;">*</span></label>
          <input type="text" name="municipioDestino" required value="<c:out value='${frete.municipioDestino}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
        </div>
        <div style="display: flex; flex-direction: column;">
          <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">UF destino <span style="color: #dc2626;">*</span></label>
          <input type="text" name="ufDestino" required maxlength="2" value="<c:out value='${frete.ufDestino}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; text-transform: uppercase;">
        </div>
      </div>
    </div>

    <!-- Seção: Carga -->
    <div style="border-top: 1px solid #d1d5db; padding-top: 24px; margin-bottom: 24px;">
      <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Carga</h3>
      <div style="display: flex; flex-direction: column; margin-bottom: 20px;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Descrição da carga <span style="color: #dc2626;">*</span></label>
        <input type="text" name="descricaoCarga" required value="<c:out value='${frete.descricaoCarga}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
      </div>
      <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px;">
        <div style="display: flex; flex-direction: column;">
          <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Peso (kg) <span style="color: #dc2626;">*</span></label>
          <input type="number" step="0.01" name="pesoKg" required value="<c:out value='${frete.pesoKg}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
        </div>
        <div style="display: flex; flex-direction: column;">
          <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Volumes <span style="color: #dc2626;">*</span></label>
          <input type="number" name="volumes" required value="<c:out value='${frete.volumes}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
        </div>
      </div>
    </div>

    <!-- Seção: Valores e Datas lado a lado -->
    <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 40px;">

      <!-- Valores -->
      <div style="border-top: 1px solid #d1d5db; padding-top: 24px;">
        <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Valores</h3>
        <div style="display: flex; flex-direction: column; gap: 20px;">
          <div style="display: flex; flex-direction: column;">
            <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Valor do frete (R$) <span style="color: #dc2626;">*</span></label>
            <input type="number" step="0.01" name="valorFrete" required value="<c:out value='${frete.valorFrete}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
          </div>
          <div style="display: flex; flex-direction: column;">
            <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Alíquota ICMS (%) <span style="color: #dc2626;">*</span></label>
            <input type="number" step="0.01" name="aliquotaIcms" required value="<c:out value='${frete.aliquotaIcms}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
          </div>
        </div>
      </div>

      <!-- Datas -->
      <div style="border-top: 1px solid #d1d5db; padding-top: 24px;">
        <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Datas</h3>
        <div style="display: flex; flex-direction: column; gap: 20px;">
          <div style="display: flex; flex-direction: column;">
            <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Data de emissão <span style="color: #dc2626;">*</span></label>
            <input type="date" name="dataEmissao" required value="<c:out value='${frete.dataEmissao}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
          </div>
          <div style="display: flex; flex-direction: column;">
            <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Previsão de entrega <span style="color: #dc2626;">*</span></label>
            <input type="date" name="dataPrevisaoEntrega" required value="<c:out value='${frete.dataPrevisaoEntrega}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
          </div>
        </div>
      </div>

    </div>

    <div style="display: flex; gap: 12px; justify-content: flex-end; border-top: 1px solid #d1d5db; padding-top: 24px; margin-top: 24px;">
      <a href="${pageContext.request.contextPath}/fretes" style="background-color: #d3d3d3; color: #333; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; text-decoration: none; font-family: 'Avenir Next', sans-serif; display: inline-block;">Cancelar</a>
      <button type="submit" style="background-color: #2563eb; color: white; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; font-family: 'Avenir Next', sans-serif;">Emitir frete</button>
    </div>
  </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/frete.js"></script>