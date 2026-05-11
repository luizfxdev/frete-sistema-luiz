package br.com.nextlog.dashboard.api;

import br.com.nextlog.dashboard.DashboardBO;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.JsonResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/api/dashboard/performance")
public class DashboardPerformanceApi extends HttpServlet {
  private final DashboardBO dashboardBO = new DashboardBO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      String dataInicioStr = req.getParameter("dataInicio");
      String dataFimStr = req.getParameter("dataFim");

      LocalDate dataInicio = (dataInicioStr == null || dataInicioStr.isEmpty())
        ? LocalDate.now().minusDays(30)
        : LocalDate.parse(dataInicioStr);

      LocalDate dataFim = (dataFimStr == null || dataFimStr.isEmpty())
        ? LocalDate.now()
        : LocalDate.parse(dataFimStr);

      JsonResponse.ok(resp, dashboardBO.performanceCompleta(dataInicio, dataFim));
    } catch (NegocioException e) {
      JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao carregar performance.");
    }
  }
}