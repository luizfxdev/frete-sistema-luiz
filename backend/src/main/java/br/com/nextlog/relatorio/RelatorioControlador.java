package br.com.nextlog.relatorio;

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

@WebServlet("/relatorios/*")
public class RelatorioControlador extends HttpServlet {

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
                String idStr = req.getParameter("idMotorista");
                String dataStr = req.getParameter("data");
                if (idStr == null || idStr.isEmpty() || dataStr == null || dataStr.isEmpty()) {
                    req.setAttribute("motoristas", motoristaBO.listarAtivos());
                    req.getRequestDispatcher("/WEB-INF/views/relatorio/romaneio.jsp").forward(req, resp);
                    return;
                }
                byte[] pdf = relatorioBO.gerarRomaneioPdf(Long.valueOf(idStr), LocalDate.parse(dataStr));
                escreverPdf(resp, "romaneio-" + idStr + "-" + dataStr + ".pdf", pdf);
                return;
            }
            req.setAttribute("motoristas", motoristaBO.listarAtivos());
            req.getRequestDispatcher("/WEB-INF/views/relatorio/index.jsp").forward(req, resp);
        } catch (NegocioException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("motoristas", motoristaBO.listarAtivos());
            req.getRequestDispatcher("/WEB-INF/views/relatorio/index.jsp").forward(req, resp);
        }
    }

    private void escreverPdf(HttpServletResponse resp, String fileName, byte[] pdf) throws IOException {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
        resp.setContentLength(pdf.length);
        try (OutputStream out = resp.getOutputStream()) {
            out.write(pdf);
            out.flush();
        }
    }
}