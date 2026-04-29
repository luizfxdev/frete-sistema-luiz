package br.com.nextlog.rota;

import br.com.nextlog.exception.NegocioException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/rotas/*")
public class TabelaRotaControlador extends HttpServlet {

    private static final int PAGE_SIZE = 10;
    private final TabelaRotaBO rotaBO = new TabelaRotaBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getPathInfo();
        if (acao == null) acao = "/";

        try {
            if ("/novo".equals(acao)) {
                req.setAttribute("rota", new TabelaFreteRota());
                req.getRequestDispatcher("/WEB-INF/views/rota/form.jsp").forward(req, resp);
                return;
            }
            if (acao.startsWith("/editar/")) {
                Long id = Long.valueOf(acao.substring("/editar/".length()));
                TabelaFreteRota r = rotaBO.buscarPorId(id);
                if (r == null) { resp.sendRedirect(req.getContextPath() + "/rotas"); return; }
                req.setAttribute("rota", r);
                req.getRequestDispatcher("/WEB-INF/views/rota/form.jsp").forward(req, resp);
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

        String acao = req.getPathInfo() == null ? "/" : req.getPathInfo();

        try {
            if (acao.startsWith("/excluir/")) {
                Long id = Long.valueOf(acao.substring("/excluir/".length()));
                rotaBO.excluir(id);
                resp.sendRedirect(req.getContextPath() + "/rotas");
                return;
            }
            TabelaFreteRota r = bind(req);
            rotaBO.salvar(r);
            resp.sendRedirect(req.getContextPath() + "/rotas");
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("rota", bind(req));
            req.getRequestDispatcher("/WEB-INF/views/rota/form.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pagina = parseIntOu(req.getParameter("pagina"), 1);
        List<TabelaFreteRota> lista = rotaBO.listar(pagina, PAGE_SIZE);
        int total = rotaBO.contar();
        int totalPaginas = (int) Math.ceil((double) total / PAGE_SIZE);

        req.setAttribute("rotas", lista);
        req.setAttribute("pagina", pagina);
        req.setAttribute("totalPaginas", Math.max(totalPaginas, 1));
        req.getRequestDispatcher("/WEB-INF/views/rota/lista.jsp").forward(req, resp);
    }

    private TabelaFreteRota bind(HttpServletRequest req) {
        TabelaFreteRota r = new TabelaFreteRota();
        String id = req.getParameter("id");
        if (id != null && !id.isEmpty()) r.setId(Long.valueOf(id));
        r.setMunicipioOrigem(req.getParameter("municipioOrigem"));
        r.setUfOrigem(req.getParameter("ufOrigem"));
        r.setMunicipioDestino(req.getParameter("municipioDestino"));
        r.setUfDestino(req.getParameter("ufDestino"));
        String vb = req.getParameter("valorBase");
        if (vb != null && !vb.isEmpty()) r.setValorBase(new BigDecimal(vb.replace(",", ".")));
        String vk = req.getParameter("valorPorKg");
        r.setValorPorKg(vk == null || vk.isEmpty() ? BigDecimal.ZERO : new BigDecimal(vk.replace(",", ".")));
        return r;
    }

    private int parseIntOu(String v, int padrao) {
        try { return v == null ? padrao : Integer.parseInt(v); }
        catch (NumberFormatException e) { return padrao; }
    }
}