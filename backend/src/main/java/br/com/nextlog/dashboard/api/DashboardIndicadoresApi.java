package br.com.nextlog.dashboard.api;

import br.com.nextlog.dashboard.DashboardBO;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/dashboard/indicadores")
public class DashboardIndicadoresApi extends HttpServlet {

  private final DashboardBO dashboardBO = new DashboardBO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      Map<String, Object> data = dashboardBO.indicadores();

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("success", true);
      response.put("data", data);

      JsonResponse.ok(resp, response);
    } catch (NegocioException e) {
      JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao carregar indicadores.");
    }
  }
}