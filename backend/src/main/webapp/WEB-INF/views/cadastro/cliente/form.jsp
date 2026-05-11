<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Cliente" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main style="padding: 24px; background-color: #ffffff; margin-left: 200px; min-height: 100vh;">
    <h1 style="font-size: 28px; font-weight: 600; color: #1f2937; margin: 0 0 24px 0; font-family: 'Avenir Next', sans-serif;">
        ${cliente.id == null ? 'Novo cliente' : 'Editar cliente'}
    </h1>

    <c:if test="${not empty erro}">
        <div style="background-color: #fee2e2; color: #991b1b; padding: 12px 16px; border-radius: 6px; margin-bottom: 24px; border-left: 4px solid #991b1b; font-family: 'Avenir Next', sans-serif;">
            <c:out value="${erro}"/>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/clientes" style="background-color: #f9fafb; border: 1px solid #e5e7eb; border-radius: 8px; padding: 24px;">
        <input type="hidden" name="id" value="<c:out value='${cliente.id}'/>"/>

        <!-- Seção: Informações básicas -->
        <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 24px;">
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Razão social <span style="color: #dc2626;">*</span></label>
                <input type="text" name="razaoSocial" required value="<c:out value='${cliente.razaoSocial}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Nome fantasia</label>
                <input type="text" name="nomeFantasia" value="<c:out value='${cliente.nomeFantasia}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Tipo de documento <span style="color: #dc2626;">*</span></label>
                <select name="tipoDocumento" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
                    <option value="">Selecione</option>
                    <option value="CPF"  ${cliente.tipoDocumento == 'CPF'  ? 'selected' : ''}>CPF</option>
                    <option value="CNPJ" ${cliente.tipoDocumento == 'CNPJ' ? 'selected' : ''}>CNPJ</option>
                </select>
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Documento <span style="color: #dc2626;">*</span></label>
                <input type="text" name="documento" required data-mask="documento" value="<c:out value='${cliente.documento}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Inscrição estadual</label>
                <input type="text" name="inscricaoEstadual" value="<c:out value='${cliente.inscricaoEstadual}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Tipo <span style="color: #dc2626;">*</span></label>
                <select name="tipo" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
                    <option value="">Selecione</option>
                    <c:forEach var="t" items="${tiposCliente}">
                        <option value="${t}" ${cliente.tipo == t ? 'selected' : ''}>${t}</option>
                    </c:forEach>
                </select>
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Status</label>
                <select name="status" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
                    <option value="ATIVO"   ${cliente.status == 'ATIVO'   ? 'selected' : ''}>ATIVO</option>
                    <option value="INATIVO" ${cliente.status == 'INATIVO' ? 'selected' : ''}>INATIVO</option>
                </select>
            </div>
        </div>

        <!-- Seção: Endereço -->
        <div style="border-top: 1px solid #d1d5db; padding-top: 24px; margin-bottom: 24px;">
            <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Endereço</h3>
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">CEP <span style="color: #dc2626;">*</span></label>
                    <input type="text" name="cep" required data-mask="cep" value="<c:out value='${cliente.cep}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column; grid-column: span 3;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Logradouro <span style="color: #dc2626;">*</span></label>
                    <input type="text" name="logradouro" required value="<c:out value='${cliente.logradouro}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
            </div>
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Número <span style="color: #dc2626;">*</span></label>
                    <input type="text" name="numero" required value="<c:out value='${cliente.numero}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column; grid-column: span 3;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Complemento</label>
                    <input type="text" name="complemento" value="<c:out value='${cliente.complemento}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
            </div>
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Bairro <span style="color: #dc2626;">*</span></label>
                    <input type="text" name="bairro" required value="<c:out value='${cliente.bairro}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column; grid-column: span 2;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Município <span style="color: #dc2626;">*</span></label>
                    <input type="text" name="municipio" required value="<c:out value='${cliente.municipio}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">UF <span style="color: #dc2626;">*</span></label>
                    <input type="text" name="uf" required maxlength="2" value="<c:out value='${cliente.uf}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; text-transform: uppercase;">
                </div>
            </div>
        </div>

        <!-- Seção: Contato -->
        <div style="border-top: 1px solid #d1d5db; padding-top: 24px; margin-bottom: 24px;">
            <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Contato</h3>
            <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Telefone</label>
                    <input type="text" name="telefone" data-mask="telefone" value="<c:out value='${cliente.telefone}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">E-mail</label>
                    <input type="email" name="email" value="<c:out value='${cliente.email}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
            </div>
        </div>

        <div style="display: flex; gap: 12px; justify-content: flex-end; border-top: 1px solid #d1d5db; padding-top: 24px;">
            <a href="${pageContext.request.contextPath}/clientes" style="background-color: #d3d3d3; color: #333; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; text-decoration: none; font-family: 'Avenir Next', sans-serif; display: inline-block;">Cancelar</a>
            <button type="submit" style="background-color: #2563eb; color: white; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; font-family: 'Avenir Next', sans-serif;">Salvar</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>