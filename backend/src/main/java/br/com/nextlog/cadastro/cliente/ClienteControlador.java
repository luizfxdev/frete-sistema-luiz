package br.com.nextlog.cadastro.cliente;

import br.com.nextlog.enums.TipoCliente;
import br.com.nextlog.exception.NegocioException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/clientes/*")
public class ClienteControlador extends HttpServlet {

    private static final int PAGE_SIZE = 10;
    private final ClienteBO clienteBO = new ClienteBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getPathInfo();
        if (acao == null) acao = "/";

        try {
            if ("/novo".equals(acao))          { editar(req, resp); return; }
            if (acao.startsWith("/carregar/")) { carregar(req, resp); return; }
            if (acao.startsWith("/editar/"))   { carregar(req, resp); return; }
            listar(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getPathInfo() == null ? "/" : req.getPathInfo();

        try {
            if (acao.startsWith("/excluir/")) {
                Long id = Long.valueOf(acao.substring("/excluir/".length()));
                clienteBO.excluir(id);
                resp.sendRedirect(req.getContextPath() + "/clientes");
                return;
            }
            Cliente c = bind(req);
            clienteBO.salvar(c);
            resp.sendRedirect(req.getContextPath() + "/clientes");
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("cliente", bind(req));
            req.setAttribute("tiposCliente", TipoCliente.values());
            req.getRequestDispatcher("/WEB-INF/views/cadastro/cliente/form.jsp").forward(req, resp);
        }
    }

    private void carregar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        String prefixo = pathInfo.startsWith("/carregar/") ? "/carregar/" : "/editar/";
        Long id = Long.valueOf(pathInfo.substring(prefixo.length()));

        Cliente c = clienteBO.buscarPorId(id);
        if (c == null) { resp.sendRedirect(req.getContextPath() + "/clientes"); return; }

        req.setAttribute("cliente", c);
        req.setAttribute("tiposCliente", TipoCliente.values());
        req.getRequestDispatcher("/WEB-INF/views/cadastro/cliente/form.jsp").forward(req, resp);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("cliente", new Cliente());
        req.setAttribute("tiposCliente", TipoCliente.values());
        req.getRequestDispatcher("/WEB-INF/views/cadastro/cliente/form.jsp").forward(req, resp);
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filtro = req.getParameter("filtro");
        int pagina = parseIntOu(req.getParameter("pagina"), 1);

        List<Cliente> lista = clienteBO.listar(filtro, pagina, PAGE_SIZE);
        int total = clienteBO.contar(filtro);
        int totalPaginas = (int) Math.ceil((double) total / PAGE_SIZE);

        Map<String, Integer> totaisPorStatus = clienteBO.contarPorStatus();
        Map<String, Integer> totaisPorTipo = clienteBO.contarPorTipo();

        req.setAttribute("clientes", lista);
        req.setAttribute("filtro", filtro);
        req.setAttribute("pagina", pagina);
        req.setAttribute("totalPaginas", Math.max(totalPaginas, 1));
        req.setAttribute("totalClientes", total);
        req.setAttribute("totalAtivos", totaisPorStatus.getOrDefault("ATIVO", 0));
        req.setAttribute("totalPF", totaisPorTipo.getOrDefault("PF", 0));
        req.setAttribute("totalPJ", totaisPorTipo.getOrDefault("PJ", 0));
        
        req.getRequestDispatcher("/WEB-INF/views/cadastro/cliente/lista.jsp").forward(req, resp);
    }

    private Cliente bind(HttpServletRequest req) {
        Cliente c = new Cliente();
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) c.setId(Long.valueOf(idStr));
        c.setRazaoSocial(req.getParameter("razaoSocial"));
        c.setNomeFantasia(req.getParameter("nomeFantasia"));
        c.setTipoDocumento(req.getParameter("tipoDocumento"));
        String documento = req.getParameter("documento");
        c.setDocumento(documento == null ? null : documento.replaceAll("\\D", ""));
        c.setInscricaoEstadual(req.getParameter("inscricaoEstadual"));
        String tipo = req.getParameter("tipo");
        if (tipo != null && !tipo.isEmpty()) c.setTipo(TipoCliente.valueOf(tipo));
        c.setLogradouro(req.getParameter("logradouro"));
        c.setNumero(req.getParameter("numero"));
        c.setComplemento(req.getParameter("complemento"));
        c.setBairro(req.getParameter("bairro"));
        c.setMunicipio(req.getParameter("municipio"));
        c.setUf(req.getParameter("uf"));
        c.setCep(req.getParameter("cep"));
        c.setTelefone(req.getParameter("telefone"));
        c.setEmail(req.getParameter("email"));
        String status = req.getParameter("status");
        c.setStatus(status == null || status.isEmpty() ? "ATIVO" : status);
        return c;
    }

    private int parseIntOu(String v, int padrao) {
        try { return v == null ? padrao : Integer.parseInt(v); }
        catch (NumberFormatException e) { return padrao; }
    }
}