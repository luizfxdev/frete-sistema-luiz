package br.com.nextlog.relatorio;

import br.com.nextlog.cadastro.motorista.Motorista;
import br.com.nextlog.cadastro.motorista.MotoristaBO;
import br.com.nextlog.exception.NegocioException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/relatorios/*")
public class RelatorioControlador extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(RelatorioControlador.class.getName());
    private static final String RESUMO_PADRAO = "[]|[]|[]";

    private final RelatorioBO relatorioBO = new RelatorioBO();
    private final MotoristaBO motoristaBO = new MotoristaBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getPathInfo();
        if (acao == null) acao = "/";

        try {
            if ("/fretes-em-aberto".equals(acao)) {
                byte[] pdf = relatorioBO.gerarFretesEmAbertoPdf();
                escreverPdf(resp, "fretes-em-aberto.pdf", pdf);
                return;
            }

            if ("/romaneio".equals(acao)) {
                String idStr   = req.getParameter("idMotorista");
                String dataStr = req.getParameter("data");

                if (idStr == null || idStr.isEmpty() || dataStr == null || dataStr.isEmpty()) {
                    carregarIndex(req);
                    req.getRequestDispatcher("/WEB-INF/views/relatorio/index.jsp").forward(req, resp);
                    return;
                }

                try {
                    byte[] pdf = relatorioBO.gerarRomaneioPdf(Long.valueOf(idStr), LocalDate.parse(dataStr));
                    escreverPdf(resp, "romaneio-" + idStr + "-" + dataStr + ".pdf", pdf);
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Erro ao gerar romaneio", e);
                    req.setAttribute("erro", "Erro ao gerar romaneio. Verifique se há fretes para essa data/motorista.");
                    carregarIndex(req);
                    req.getRequestDispatcher("/WEB-INF/views/relatorio/index.jsp").forward(req, resp);
                }
                return;
            }

            carregarIndex(req);
            req.getRequestDispatcher("/WEB-INF/views/relatorio/index.jsp").forward(req, resp);

        } catch (NegocioException e) {
            LOG.log(Level.WARNING, "Erro de negócio ao acessar relatórios", e);
            req.setAttribute("erro", e.getMessage());
            carregarIndex(req);
            try {
                req.getRequestDispatcher("/WEB-INF/views/relatorio/index.jsp").forward(req, resp);
            } catch (ServletException | IOException ex) {
                LOG.log(Level.SEVERE, "Erro ao fazer forward após exceção", ex);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Erro inesperado ao acessar relatórios", e);
            req.setAttribute("erro", "Erro inesperado. Tente novamente.");
            carregarIndex(req);
            try {
                req.getRequestDispatcher("/WEB-INF/views/relatorio/index.jsp").forward(req, resp);
            } catch (ServletException | IOException ex) {
                LOG.log(Level.SEVERE, "Erro ao fazer forward após exceção", ex);
            }
        }
    }

    /**
     * Carrega dados necessários para a página de índice de relatórios
     */
    private void carregarIndex(HttpServletRequest req) {
        try {
            List<Motorista> motoristas = motoristaBO.listarAtivos();
            req.setAttribute("motoristas", motoristas != null ? motoristas : java.util.Collections.emptyList());
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Erro ao carregar motoristas", e);
            req.setAttribute("motoristas", java.util.Collections.emptyList());
        }

        carregarResumoMensal(req);
    }

    /**
     * Carrega dados de resumo mensal com tratamento seguro de erros
     */
    private void carregarResumoMensal(HttpServletRequest req) {
        try {
            String resumo = relatorioBO.resumoMensal(7);

            if (resumo == null || resumo.trim().isEmpty()) {
                LOG.log(Level.WARNING, "resumoMensal retornou vazio, usando padrão");
                req.setAttribute("resumoMensal", RESUMO_PADRAO);
                return;
            }

            LOG.log(Level.INFO, "resumoMensal carregado com sucesso: " + resumo);
            req.setAttribute("resumoMensal", resumo);

        } catch (Exception e) {
            LOG.log(Level.WARNING, "Erro ao carregar resumo mensal, usando padrão: " + e.getMessage(), e);
            req.setAttribute("resumoMensal", RESUMO_PADRAO);
        }
    }

    /**
     * Escreve PDF na resposta HTTP
     */
    private void escreverPdf(HttpServletResponse resp, String fileName, byte[] pdf) throws IOException {
        if (pdf == null || pdf.length == 0) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao gerar relatório");
            return;
        }

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
        resp.setContentLength(pdf.length);

        try (OutputStream out = resp.getOutputStream()) {
            out.write(pdf);
            out.flush();
        }
    }
}