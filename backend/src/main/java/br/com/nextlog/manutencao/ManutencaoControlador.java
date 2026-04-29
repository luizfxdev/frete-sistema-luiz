package br.com.nextlog.manutencao;

import br.com.nextlog.cadastro.veiculo.VeiculoBO;
import br.com.nextlog.enums.TipoManutencao;
import br.com.nextlog.exception.NegocioException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/manutencoes/*")
public class ManutencaoControlador extends HttpServlet {

    private static final int PAGE_SIZE = 10;
    private final ManutencaoBO manutencaoBO = new ManutencaoBO();
    private final VeiculoBO veiculoBO = new VeiculoBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getPathInfo();
        if (acao == null) acao = "/";

        try {
            if ("/novo".equals(acao)) {
                req.setAttribute("manutencao", new ManutencaoVeiculo());
                preencherListas(req);
                req.getRequestDispatcher("/WEB-INF/views/manutencao/form.jsp").forward(req, resp);
                return;
            }
            if (acao.startsWith("/editar/")) {
                Long id = Long.valueOf(acao.substring("/editar/".length()));
                ManutencaoVeiculo m = manutencaoBO.buscarPorId(id);
                if (m == null) { resp.sendRedirect(req.getContextPath() + "/manutencoes"); return; }
                req.setAttribute("manutencao", m);
                preencherListas(req);
                req.getRequestDispatcher("/WEB-INF/views/manutencao/form.jsp").forward(req, resp);
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
            ManutencaoVeiculo m = bind(req);
            manutencaoBO.salvar(m);
            resp.sendRedirect(req.getContextPath() + "/manutencoes");
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("manutencao", bind(req));
            preencherListas(req);
            req.getRequestDispatcher("/WEB-INF/views/manutencao/form.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filtro = req.getParameter("filtro");
        int pagina = parseIntOu(req.getParameter("pagina"), 1);

        List<ManutencaoVeiculo> lista = manutencaoBO.listar(filtro, pagina, PAGE_SIZE);
        int total = manutencaoBO.contar(filtro);
        int totalPaginas = (int) Math.ceil((double) total / PAGE_SIZE);

        req.setAttribute("manutencoes", lista);
        req.setAttribute("filtro", filtro);
        req.setAttribute("pagina", pagina);
        req.setAttribute("totalPaginas", Math.max(totalPaginas, 1));
        req.getRequestDispatcher("/WEB-INF/views/manutencao/lista.jsp").forward(req, resp);
    }

    private void preencherListas(HttpServletRequest req) {
        req.setAttribute("veiculos", veiculoBO.listarTodos());
        req.setAttribute("tipos", TipoManutencao.values());
    }

    private ManutencaoVeiculo bind(HttpServletRequest req) {
        ManutencaoVeiculo m = new ManutencaoVeiculo();
        String id = req.getParameter("id");
        if (id != null && !id.isEmpty()) m.setId(Long.valueOf(id));
        String idVeiculo = req.getParameter("idVeiculo");
        if (idVeiculo != null && !idVeiculo.isEmpty()) m.setIdVeiculo(Long.valueOf(idVeiculo));
        String tipo = req.getParameter("tipo");
        if (tipo != null && !tipo.isEmpty()) m.setTipo(TipoManutencao.valueOf(tipo));
        m.setDescricao(req.getParameter("descricao"));
        String inicio = req.getParameter("dataInicio");
        if (inicio != null && !inicio.isEmpty()) m.setDataInicio(LocalDate.parse(inicio));
        String fim = req.getParameter("dataFim");
        if (fim != null && !fim.isEmpty()) m.setDataFim(LocalDate.parse(fim));
        String custo = req.getParameter("custo");
        if (custo != null && !custo.trim().isEmpty()) m.setCusto(new BigDecimal(custo.replace(",", ".")));
        return m;
    }

    private int parseIntOu(String v, int padrao) {
        try { return v == null ? padrao : Integer.parseInt(v); }
        catch (NumberFormatException e) { return padrao; }
    }
}