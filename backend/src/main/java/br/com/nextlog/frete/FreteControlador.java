package br.com.nextlog.frete;

import br.com.nextlog.cadastro.cliente.Cliente;
import br.com.nextlog.cadastro.cliente.ClienteBO;
import br.com.nextlog.cadastro.motorista.Motorista;
import br.com.nextlog.cadastro.motorista.MotoristaBO;
import br.com.nextlog.cadastro.veiculo.Veiculo;
import br.com.nextlog.cadastro.veiculo.VeiculoBO;
import br.com.nextlog.enums.StatusFrete;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.frete.ocorrencia.OcorrenciaBO;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/fretes/*")
public class FreteControlador extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    private final FreteBO freteBO = new FreteBO();
    private final ClienteBO clienteBO = new ClienteBO();
    private final MotoristaBO motoristaBO = new MotoristaBO();
    private final VeiculoBO veiculoBO = new VeiculoBO();
    private final OcorrenciaBO ocorrenciaBO = new OcorrenciaBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getPathInfo();
        if (acao == null) acao = "/";

        try {
            if (acao.startsWith("/api/cliente/")) {
                Long id = Long.valueOf(acao.substring("/api/cliente/".length()));
                Cliente c = clienteBO.buscarPorId(id);
                if (c == null) {
                    JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Cliente não encontrado.");
                    return;
                }
                Map<String, Object> dados = new LinkedHashMap<>();
                dados.put("id", c.getId());
                dados.put("razaoSocial", c.getRazaoSocial());
                dados.put("municipio", c.getMunicipio());
                dados.put("uf", c.getUf());
                JsonResponse.ok(resp, dados);
                return;
            }
            if ("/novo".equals(acao)) {
                req.setAttribute("frete", new Frete());
                carregarListasAuxiliares(req);
                req.getRequestDispatcher("/WEB-INF/views/frete/form.jsp").forward(req, resp);
                return;
            }
            if (acao.startsWith("/detalhe/")) {
                Long id = Long.valueOf(acao.substring("/detalhe/".length()));
                Frete f = freteBO.buscarPorId(id);
                if (f == null) { resp.sendRedirect(req.getContextPath() + "/fretes"); return; }
                req.setAttribute("frete", f);
                req.setAttribute("ocorrencias", ocorrenciaBO.listarPorFrete(id));
                req.getRequestDispatcher("/WEB-INF/views/frete/detalhe.jsp").forward(req, resp);
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
            if (acao.startsWith("/confirmar-saida/")) {
                Long id = Long.valueOf(acao.substring("/confirmar-saida/".length()));
                freteBO.confirmarSaida(id);
                resp.sendRedirect(req.getContextPath() + "/fretes/detalhe/" + id);
                return;
            }
            if (acao.startsWith("/marcar-em-transito/")) {
                Long id = Long.valueOf(acao.substring("/marcar-em-transito/".length()));
                freteBO.marcarEmTransito(id);
                resp.sendRedirect(req.getContextPath() + "/fretes/detalhe/" + id);
                return;
            }
            if (acao.startsWith("/registrar-entrega/")) {
                Long id = Long.valueOf(acao.substring("/registrar-entrega/".length()));
                freteBO.registrarEntrega(id);
                resp.sendRedirect(req.getContextPath() + "/fretes/detalhe/" + id);
                return;
            }
            if (acao.startsWith("/registrar-nao-entrega/")) {
                Long id = Long.valueOf(acao.substring("/registrar-nao-entrega/".length()));
                freteBO.registrarNaoEntrega(id);
                resp.sendRedirect(req.getContextPath() + "/fretes/detalhe/" + id);
                return;
            }
            if (acao.startsWith("/cancelar/")) {
                Long id = Long.valueOf(acao.substring("/cancelar/".length()));
                freteBO.cancelar(id);
                resp.sendRedirect(req.getContextPath() + "/fretes/detalhe/" + id);
                return;
            }
            Frete f = bind(req);
            freteBO.registrarFrete(f);
            resp.sendRedirect(req.getContextPath() + "/fretes");
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && pathInfo.contains("/")) {
                String[] partes = pathInfo.split("/");
                if (partes.length >= 3) {
                    try {
                        Long id = Long.valueOf(partes[2]);
                        Frete f = freteBO.buscarPorId(id);
                        if (f != null) {
                            req.setAttribute("frete", f);
                            req.setAttribute("ocorrencias", ocorrenciaBO.listarPorFrete(id));
                            req.getRequestDispatcher("/WEB-INF/views/frete/detalhe.jsp").forward(req, resp);
                            return;
                        }
                    } catch (NumberFormatException ignored) { }
                }
            }
            req.setAttribute("frete", bind(req));
            carregarListasAuxiliares(req);
            req.getRequestDispatcher("/WEB-INF/views/frete/form.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filtro = req.getParameter("filtro");
        String statusFiltro = req.getParameter("statusFiltro");
        int pagina = parseIntOu(req.getParameter("pagina"), 1);

        List<Frete> lista = freteBO.listar(filtro, statusFiltro, pagina, PAGE_SIZE);
        int total = freteBO.contar(filtro, statusFiltro);
        int totalPaginas = (int) Math.ceil((double) total / PAGE_SIZE);

        int totalEmitido = freteBO.contar(null, "EMITIDO");
        int totalEmTransito = freteBO.contar(null, "EM_TRANSITO");
        int totalEntregue = freteBO.contar(null, "ENTREGUE");
        int totalCancelado = freteBO.contar(null, "CANCELADO");
        int totalFretes = totalEmitido + totalEmTransito + totalEntregue + totalCancelado;

        req.setAttribute("fretes", lista);
        req.setAttribute("filtro", filtro);
        req.setAttribute("statusFiltro", statusFiltro);
        req.setAttribute("statusList", StatusFrete.values());
        req.setAttribute("pagina", pagina);
        req.setAttribute("totalPaginas", Math.max(totalPaginas, 1));
        req.setAttribute("totalFretes", totalFretes);
        req.setAttribute("totalEmitido", totalEmitido);
        req.setAttribute("totalEmTransito", totalEmTransito);
        req.setAttribute("totalEntregue", totalEntregue);
        req.setAttribute("totalCancelado", totalCancelado);
        req.getRequestDispatcher("/WEB-INF/views/frete/lista.jsp").forward(req, resp);
    }

    private void carregarListasAuxiliares(HttpServletRequest req) {
        List<Cliente> clientes = clienteBO.listarAtivos();
        List<Motorista> motoristas = motoristaBO.listarAtivos();
        List<Veiculo> veiculos = veiculoBO.listarDisponiveis();
        req.setAttribute("clientes", clientes);
        req.setAttribute("motoristas", motoristas);
        req.setAttribute("veiculos", veiculos);
    }

    private Frete bind(HttpServletRequest req) {
        Frete f = new Frete();
        String id = req.getParameter("id");
        if (id != null && !id.isEmpty()) f.setId(Long.valueOf(id));
        f.setIdRemetente(parseLong(req.getParameter("idRemetente")));
        f.setIdDestinatario(parseLong(req.getParameter("idDestinatario")));
        f.setIdMotorista(parseLong(req.getParameter("idMotorista")));
        f.setIdVeiculo(parseLong(req.getParameter("idVeiculo")));
        f.setMunicipioOrigem(req.getParameter("municipioOrigem"));
        f.setUfOrigem(upper(req.getParameter("ufOrigem")));
        f.setMunicipioDestino(req.getParameter("municipioDestino"));
        f.setUfDestino(upper(req.getParameter("ufDestino")));
        f.setDescricaoCarga(req.getParameter("descricaoCarga"));
        f.setPesoKg(parseDecimal(req.getParameter("pesoKg")));
        String volumes = req.getParameter("volumes");
        if (volumes != null && !volumes.isEmpty()) f.setVolumes(Integer.valueOf(volumes));
        f.setValorFrete(parseDecimal(req.getParameter("valorFrete")));
        f.setAliquotaIcms(parseDecimal(req.getParameter("aliquotaIcms")));
        String emissao = req.getParameter("dataEmissao");
        if (emissao != null && !emissao.isEmpty()) f.setDataEmissao(LocalDate.parse(emissao));
        else f.setDataEmissao(LocalDate.now());
        String previsao = req.getParameter("dataPrevisaoEntrega");
        if (previsao != null && !previsao.isEmpty()) f.setDataPrevisaoEntrega(LocalDate.parse(previsao));
        return f;
    }

    private Long parseLong(String s) {
        if (s == null || s.isEmpty()) return null;
        return Long.valueOf(s);
    }

    private BigDecimal parseDecimal(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return new BigDecimal(s.replace(",", "."));
    }

    private String upper(String s) {
        return s == null ? null : s.toUpperCase();
    }

    private int parseIntOu(String v, int padrao) {
        try { return v == null ? padrao : Integer.parseInt(v); }
        catch (NumberFormatException e) { return padrao; }
    }
}