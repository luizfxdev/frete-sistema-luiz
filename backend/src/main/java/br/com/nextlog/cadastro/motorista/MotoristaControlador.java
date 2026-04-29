package br.com.nextlog.cadastro.motorista;

import br.com.nextlog.enums.CnhCategoria;
import br.com.nextlog.enums.StatusMotorista;
import br.com.nextlog.enums.TipoVinculo;
import br.com.nextlog.exception.NegocioException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/motoristas/*")
public class MotoristaControlador extends HttpServlet {

    private static final int PAGE_SIZE = 10;
    private final MotoristaBO motoristaBO = new MotoristaBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getPathInfo();
        if (acao == null) acao = "/";

        try {
            if ("/novo".equals(acao)) {
                req.setAttribute("motorista", new Motorista());
                preencherEnums(req);
                req.getRequestDispatcher("/WEB-INF/views/cadastro/motorista/form.jsp").forward(req, resp);
                return;
            }
            if (acao.startsWith("/editar/")) {
                Long id = Long.valueOf(acao.substring("/editar/".length()));
                Motorista m = motoristaBO.buscarPorId(id);
                if (m == null) { resp.sendRedirect(req.getContextPath() + "/motoristas"); return; }
                req.setAttribute("motorista", m);
                preencherEnums(req);
                req.getRequestDispatcher("/WEB-INF/views/cadastro/motorista/form.jsp").forward(req, resp);
                return;
            }
            listar(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Motorista m = bind(req);
            motoristaBO.salvar(m);
            resp.sendRedirect(req.getContextPath() + "/motoristas");
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("motorista", bind(req));
            preencherEnums(req);
            req.getRequestDispatcher("/WEB-INF/views/cadastro/motorista/form.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filtro = req.getParameter("filtro");
        int pagina = parseIntOu(req.getParameter("pagina"), 1);

        List<Motorista> lista = motoristaBO.listar(filtro, pagina, PAGE_SIZE);
        int total = motoristaBO.contar(filtro);
        int totalPaginas = (int) Math.ceil((double) total / PAGE_SIZE);

        req.setAttribute("motoristas", lista);
        req.setAttribute("filtro", filtro);
        req.setAttribute("pagina", pagina);
        req.setAttribute("totalPaginas", Math.max(totalPaginas, 1));
        req.getRequestDispatcher("/WEB-INF/views/cadastro/motorista/lista.jsp").forward(req, resp);
    }

    private void preencherEnums(HttpServletRequest req) {
        req.setAttribute("categorias", CnhCategoria.values());
        req.setAttribute("vinculos", TipoVinculo.values());
        req.setAttribute("statusMotorista", StatusMotorista.values());
    }

    private Motorista bind(HttpServletRequest req) {
        Motorista m = new Motorista();
        String id = req.getParameter("id");
        if (id != null && !id.isEmpty()) m.setId(Long.valueOf(id));
        m.setNome(req.getParameter("nome"));
        m.setCpf(req.getParameter("cpf"));
        String dn = req.getParameter("dataNascimento");
        if (dn != null && !dn.isEmpty()) m.setDataNascimento(LocalDate.parse(dn));
        m.setTelefone(req.getParameter("telefone"));
        m.setCnhNumero(req.getParameter("cnhNumero"));
        String cat = req.getParameter("cnhCategoria");
        if (cat != null && !cat.isEmpty()) m.setCnhCategoria(CnhCategoria.valueOf(cat));
        String validade = req.getParameter("cnhValidade");
        if (validade != null && !validade.isEmpty()) m.setCnhValidade(LocalDate.parse(validade));
        String vinculo = req.getParameter("tipoVinculo");
        if (vinculo != null && !vinculo.isEmpty()) m.setTipoVinculo(TipoVinculo.valueOf(vinculo));
        String status = req.getParameter("status");
        m.setStatus(status == null || status.isEmpty() ? StatusMotorista.ATIVO : StatusMotorista.valueOf(status));
        return m;
    }

    private int parseIntOu(String v, int padrao) {
        try { return v == null ? padrao : Integer.parseInt(v); }
        catch (NumberFormatException e) { return padrao; }
    }
}