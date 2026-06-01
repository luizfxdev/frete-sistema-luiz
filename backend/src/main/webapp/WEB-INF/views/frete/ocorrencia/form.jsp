<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Nova ocorrência" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main style="padding: 24px; background-color: #ffffff; margin-left: 200px; min-height: 100vh;">
  <h1 style="font-size: 28px; font-weight: 600; color: #1f2937; margin: 0 0 24px 0; font-family: 'Avenir Next', sans-serif;">
    Nova ocorrência - <c:out value="${frete.numero}"/>
  </h1>

  <c:if test="${not empty erro}">
    <div style="background-color: #fee2e2; color: #991b1b; padding: 12px 16px; border-radius: 6px; margin-bottom: 24px; border-left: 4px solid #991b1b; font-family: 'Avenir Next', sans-serif;">
      <c:out value="${erro}"/>
    </div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/ocorrencias" style="background-color: #f9fafb; border: 1px solid #e5e7eb; border-radius: 8px; padding: 24px;">
    
    <input type="hidden" name="idFrete" value="${ocorrencia.idFrete}"/>

    <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 24px;">
      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Tipo <span style="color: #dc2626;">*</span></label>
        <select name="tipo" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
          <option value="">Selecione</option>
          <c:forEach var="t" items="${tipos}">
            <option value="${t}" ${ocorrencia.tipo == t ? 'selected' : ''}><c:out value="${t}"/></option>
          </c:forEach>
        </select>
      </div>

      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Data/hora <span style="color: #dc2626;">*</span></label>
        <input type="datetime-local" name="dataHora" required value="<c:out value='${ocorrencia.dataHora}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
      </div>

      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Município <span style="color: #dc2626;">*</span></label>
        <input type="text" name="municipio" required value="<c:out value='${ocorrencia.municipio}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
      </div>

      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">UF <span style="color: #dc2626;">*</span></label>
        <input type="text" name="uf" required maxlength="2" value="<c:out value='${ocorrencia.uf}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; text-transform: uppercase;">
      </div>
    </div>

    <div style="margin-bottom: 24px;">
      <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; display: block; font-family: 'Avenir Next', sans-serif;">Descrição</label>
      <textarea name="descricao" style="width: 100%; padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; min-height: 100px;"><c:out value="${ocorrencia.descricao}"/></textarea>
    </div>

    <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 24px;">
      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Nome do recebedor</label>
        <input type="text" name="nomeRecebedor" value="<c:out value='${ocorrencia.nomeRecebedor}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
      </div>

      <div style="display: flex; flex-direction: column;">
        <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Documento do recebedor</label>
        <input type="text" name="documentoRecebedor" value="<c:out value='${ocorrencia.documentoRecebedor}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
      </div>
    </div>

    <div style="display: flex; gap: 12px; justify-content: flex-end;">
      <a href="${pageContext.request.contextPath}/fretes/detalhe/${ocorrencia.idFrete}" style="background-color: #d3d3d3; color: #333; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; text-decoration: none; font-family: 'Avenir Next', sans-serif; display: inline-block;">Cancelar</a>
      <button type="submit" style="background-color: #2563eb; color: white; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; font-family: 'Avenir Next', sans-serif;">Registrar ocorrência</button>
    </div>
  </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>