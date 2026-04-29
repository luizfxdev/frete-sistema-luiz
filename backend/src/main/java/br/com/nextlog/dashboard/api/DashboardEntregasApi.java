package br.com.nextlog.dashboard.api;

import br.com.nextlog.dashboard.DashboardBO;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/api/dashboard/entregas")
public class DashboardEntregasApi extends HttpServlet {

    private final DashboardBO dashboardBO = new DashboardBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String inicioStr = req.getParameter("inicio");
            String fimStr    = req.getParameter("fim");
            LocalDate inicio = (inicioStr == null || inicioStr.isEmpty()) ? null : LocalDate.parse(inicioStr);
            LocalDate fim    = (fimStr    == null || fimStr.isEmpty())    ? null : LocalDate.parse(fimStr);
            JsonResponse.ok(resp, dashboardBO.entregasPorPeriodo(inicio, fim));
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao carregar entregas.");
        }
    }
}