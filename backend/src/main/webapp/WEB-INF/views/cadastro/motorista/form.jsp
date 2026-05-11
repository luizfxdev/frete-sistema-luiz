<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Motorista" scope="request"/>
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
<jsp:include page="/WEB-INF/views/shared/sidebar.jsp"/>

<main style="padding: 24px; background-color: #ffffff; margin-left: 200px; min-height: 100vh;">
    <h1 style="font-size: 28px; font-weight: 600; color: #1f2937; margin: 0 0 24px 0; font-family: 'Avenir Next', sans-serif;">
        ${motorista.id == null ? 'Novo motorista' : 'Editar motorista'}
    </h1>

    <c:if test="${not empty erro}">
        <div style="background-color: #fee2e2; color: #991b1b; padding: 12px 16px; border-radius: 6px; margin-bottom: 24px; border-left: 4px solid #991b1b; font-family: 'Avenir Next', sans-serif;">
            <c:out value="${erro}"/>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/motoristas" style="background-color: #f9fafb; border: 1px solid #e5e7eb; border-radius: 8px; padding: 24px;">
        <input type="hidden" name="id" value="<c:out value='${motorista.id}'/>"/>

        <!-- Seção: Informações pessoais -->
        <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 24px;">
            <div style="display: flex; flex-direction: column; grid-column: span 2;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Nome <span style="color: #dc2626;">*</span></label>
                <input type="text" name="nome" required value="<c:out value='${motorista.nome}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">CPF <span style="color: #dc2626;">*</span></label>
                <input type="text" name="cpf" required data-mask="cpf" value="<c:out value='${motorista.cpf}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Data de nascimento <span style="color: #dc2626;">*</span></label>
                <input type="text" name="dataNascimento" required data-mask="data-nascimento" value="<c:out value='${motorista.dataNascimento}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
            <div style="display: flex; flex-direction: column;">
                <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Telefone</label>
                <input type="text" name="telefone" data-mask="telefone" value="<c:out value='${motorista.telefone}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
            </div>
        </div>

        <!-- Seção: CNH -->
        <div style="border-top: 1px solid #d1d5db; padding-top: 24px; margin-bottom: 24px;">
            <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">CNH</h3>
            <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Número da CNH <span style="color: #dc2626;">*</span></label>
                    <input type="text" name="cnhNumero" required value="<c:out value='${motorista.cnhNumero}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Categoria <span style="color: #dc2626;">*</span></label>
                    <select name="cnhCategoria" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
                        <option value="">Selecione</option>
                        <c:forEach var="cat" items="${categorias}">
                            <option value="${cat}" ${motorista.cnhCategoria == cat ? 'selected' : ''}>${cat}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Validade da CNH <span style="color: #dc2626;">*</span></label>
                    <input type="date" name="cnhValidade" required value="<c:out value='${motorista.cnhValidade}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
            </div>
        </div>

        <!-- Seção: Vínculo e Status -->
        <div style="border-top: 1px solid #d1d5db; padding-top: 24px; margin-bottom: 24px;">
            <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Vínculo</h3>
            <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Tipo de vínculo <span style="color: #dc2626;">*</span></label>
                    <select name="tipoVinculo" required style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
                        <option value="">Selecione</option>
                        <c:forEach var="v" items="${vinculos}">
                            <option value="${v}" ${motorista.tipoVinculo == v ? 'selected' : ''}>${v}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Status</label>
                    <select name="status" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; background-color: white; color: #1f2937;">
                        <c:forEach var="s" items="${statusMotorista}">
                            <option value="${s}" ${motorista.status == s ? 'selected' : ''}>${s}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <!-- Seção: Endereço -->
        <div style="border-top: 1px solid #d1d5db; padding-top: 24px; margin-bottom: 24px;">
            <h3 style="font-size: 16px; font-weight: 600; color: #1f2937; margin: 0 0 16px 0; font-family: 'Avenir Next', sans-serif;">Endereço</h3>
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">CEP</label>
                    <input type="text" name="cep" data-mask="cep" value="<c:out value='${motorista.cep}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column; grid-column: span 3;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Logradouro</label>
                    <input type="text" name="logradouro" value="<c:out value='${motorista.logradouro}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
            </div>
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Número</label>
                    <input type="text" name="numero" value="<c:out value='${motorista.numero}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column; grid-column: span 3;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Complemento</label>
                    <input type="text" name="complemento" value="<c:out value='${motorista.complemento}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
            </div>
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px;">
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Bairro</label>
                    <input type="text" name="bairro" value="<c:out value='${motorista.bairro}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column; grid-column: span 2;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">Município</label>
                    <input type="text" name="municipio" value="<c:out value='${motorista.municipio}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif;">
                </div>
                <div style="display: flex; flex-direction: column;">
                    <label style="font-size: 14px; font-weight: 600; color: #1f2937; margin-bottom: 6px; font-family: 'Avenir Next', sans-serif;">UF</label>
                    <input type="text" name="uf" maxlength="2" value="<c:out value='${motorista.uf}'/>" style="padding: 10px 12px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 14px; font-family: 'Avenir Next', sans-serif; text-transform: uppercase;">
                </div>
            </div>
        </div>

        <div style="display: flex; gap: 12px; justify-content: flex-end; border-top: 1px solid #d1d5db; padding-top: 24px;">
            <a href="${pageContext.request.contextPath}/motoristas" style="background-color: #d3d3d3; color: #333; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; text-decoration: none; font-family: 'Avenir Next', sans-serif; display: inline-block;">Cancelar</a>
            <button type="submit" style="background-color: #2563eb; color: white; padding: 10px 24px; border: none; border-radius: 6px; font-size: 14px; font-weight: 500; cursor: pointer; font-family: 'Avenir Next', sans-serif;">Salvar</button>
        </div>
    </form>
</main>

<jsp:include page="/WEB-INF/views/shared/footer.jsp"/>