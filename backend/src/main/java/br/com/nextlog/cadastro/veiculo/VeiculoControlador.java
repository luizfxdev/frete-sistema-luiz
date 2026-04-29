package br.com.nextlog.cadastro.veiculo;

import br.com.nextlog.enums.StatusVeiculo;
import br.com.nextlog.enums.TipoVeiculo;
import br.com.nextlog.exception.NegocioException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/veiculos/*")
public class VeiculoControlador extends HttpServlet {

    private static final int PAGE_SIZE = 10;
    private final VeiculoBO veiculoBO = new VeiculoBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getPathInfo();
        if (acao == null) acao = "/";

        try {
            if ("/novo".equals(acao)) {
                req.setAttribute("veiculo", new Veiculo());
                preencherEnums(req);
                req.getRequestDispatcher("/WEB-INF/views/cadastro/veiculo/form.jsp").forward(req, resp);
                return;
            }
            if (acao.startsWith("/editar/")) {
                Long id = Long.valueOf(acao.substring("/editar/".length()));
                Veiculo v = veiculoBO.buscarPorId(id);
                if (v == null) { resp.sendRedirect(req.getContextPath() + "/veiculos"); return; }
                req.setAttribute("veiculo", v);
                preencherEnums(req);
                req.getRequestDispatcher("/WEB-INF/views/cadastro/veiculo/form.jsp").forward(req, resp);
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
            Veiculo v = bind(req);
            veiculoBO.salvar(v);
            resp.sendRedirect(req.getContextPath() + "/veiculos");
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("veiculo", bind(req));
            preencherEnums(req);
            req.getRequestDispatcher("/WEB-INF/views/cadastro/veiculo/form.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filtro = req.getParameter("filtro");
        int pagina = parseIntOu(req.getParameter("pagina"), 1);

        List<Veiculo> lista = veiculoBO.listar(filtro, pagina, PAGE_SIZE);
        int total = veiculoBO.contar(filtro);
        int totalPaginas = (int) Math.ceil((double) total / PAGE_SIZE);

        req.setAttribute("veiculos", lista);
        req.setAttribute("filtro", filtro);
        req.setAttribute("pagina", pagina);
        req.setAttribute("totalPaginas", Math.max(totalPaginas, 1));
        req.getRequestDispatcher("/WEB-INF/views/cadastro/veiculo/lista.jsp").forward(req, resp);
    }

    private void preencherEnums(HttpServletRequest req) {
        req.setAttribute("tipos", TipoVeiculo.values());
        req.setAttribute("statusVeiculo", StatusVeiculo.values());
    }

    private Veiculo bind(HttpServletRequest req) {
        Veiculo v = new Veiculo();
        String id = req.getParameter("id");
        if (id != null && !id.isEmpty()) v.setId(Long.valueOf(id));
        v.setPlaca(req.getParameter("placa"));
        v.setRntrc(req.getParameter("rntrc"));
        String ano = req.getParameter("anoFabricacao");
        if (ano != null && !ano.isEmpty()) v.setAnoFabricacao(Integer.valueOf(ano));
        String tipo = req.getParameter("tipo");
        if (tipo != null && !tipo.isEmpty()) v.setTipo(TipoVeiculo.valueOf(tipo));
        v.setTaraKg(parseDecimal(req.getParameter("taraKg")));
        v.setCapacidadeKg(parseDecimal(req.getParameter("capacidadeKg")));
        v.setVolumeM3(parseDecimal(req.getParameter("volumeM3")));
        String status = req.getParameter("status");
        v.setStatus(status == null || status.isEmpty() ? StatusVeiculo.DISPONIVEL : StatusVeiculo.valueOf(status));
        return v;
    }

    private BigDecimal parseDecimal(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return new BigDecimal(s.replace(",", "."));
    }

    private int parseIntOu(String v, int padrao) {
        try { return v == null ? padrao : Integer.parseInt(v); }
        catch (NumberFormatException e) { return padrao; }
    }
}