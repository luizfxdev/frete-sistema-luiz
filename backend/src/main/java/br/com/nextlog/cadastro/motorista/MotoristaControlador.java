package br.com.nextlog.cadastro.motorista;

import br.com.nextlog.enums.CnhCategoria;
import br.com.nextlog.enums.StatusMotorista;
import br.com.nextlog.enums.TipoVinculo;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@WebServlet({"/motoristas/*", "/api/motorista/*"})
public class MotoristaControlador extends HttpServlet {

    private static final int PAGE_SIZE = 10;
    private static final DateTimeFormatter BR_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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

        String acao = req.getPathInfo();
        if (acao == null) acao = "/";

        if (acao.matches("/\\d+/disponibilidade")) {
            handleDisponibilidade(req, resp, acao);
            return;
        }

        if (acao.startsWith("/excluir/")) {
            try {
                Long id = Long.valueOf(acao.substring("/excluir/".length()));
                motoristaBO.excluir(id);
                resp.sendRedirect(req.getContextPath() + "/motoristas");
            } catch (NegocioException e) {
                req.setAttribute("erro", e.getMessage());
                listar(req, resp);
            }
            return;
        }

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

    private void handleDisponibilidade(HttpServletRequest req, HttpServletResponse resp, String acao)
            throws IOException {
        try {
            String[] partes = acao.split("/");
            Long id = Long.valueOf(partes[1]);

            String body = req.getReader().lines().reduce("", (acc, l) -> acc + l);
            boolean disponivel = body.contains("\"disponivel\":true")
                    || body.contains("\"disponivel\": true");

            motoristaBO.atualizarDisponibilidade(id, disponivel);
            JsonResponse.ok(resp, "{\"sucesso\":true}");
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filtro = req.getParameter("filtro");
        int pagina = parseIntOu(req.getParameter("pagina"), 1);

        List<Motorista> lista = motoristaBO.listar(filtro, pagina, PAGE_SIZE);
        int total = motoristaBO.contar(filtro);
        int totalPaginas = (int) Math.ceil((double) total / PAGE_SIZE);

        Map<StatusMotorista, Integer> totaisPorStatus = motoristaBO.contarPorStatus();
        int totalCnhVencida = motoristaBO.contarCnhVencida();

        req.setAttribute("motoristas", lista);
        req.setAttribute("filtro", filtro);
        req.setAttribute("pagina", pagina);
        req.setAttribute("totalPaginas", Math.max(totalPaginas, 1));
        req.setAttribute("totalMotoristas", total);
        req.setAttribute("totalAtivos", totaisPorStatus.getOrDefault(StatusMotorista.ATIVO, 0));
        req.setAttribute("totalSuspensos", totaisPorStatus.getOrDefault(StatusMotorista.SUSPENSO, 0));
        req.setAttribute("totalCnhVencida", totalCnhVencida);
        
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
        m.setDataNascimento(parseData(req.getParameter("dataNascimento")));
        m.setTelefone(req.getParameter("telefone"));
        m.setLogradouro(req.getParameter("logradouro"));
        m.setNumero(req.getParameter("numero"));
        m.setComplemento(req.getParameter("complemento"));
        m.setBairro(req.getParameter("bairro"));
        m.setMunicipio(req.getParameter("municipio"));
        m.setUf(req.getParameter("uf"));
        m.setCep(req.getParameter("cep"));
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

    private LocalDate parseData(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        if (valor.contains("/")) return LocalDate.parse(valor, BR_DATE);
        return LocalDate.parse(valor);
    }

    private int parseIntOu(String v, int padrao) {
        try { return v == null ? padrao : Integer.parseInt(v); }
        catch (NumberFormatException e) { return padrao; }
    }
}