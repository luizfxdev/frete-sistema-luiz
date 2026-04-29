package br.com.nextlog.dashboard.api;

import br.com.nextlog.dashboard.DashboardBO;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/dashboard/motoristas")
public class DashboardMotoristasApi extends HttpServlet {

    private final DashboardBO dashboardBO = new DashboardBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int limite = parseInt(req.getParameter("limite"), 10);
            JsonResponse.ok(resp, dashboardBO.rankingMotoristas(limite));
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao carregar ranking.");
        }
    }

    private int parseInt(String v, int padrao) {
        try { return v == null ? padrao : Integer.parseInt(v); }
        catch (NumberFormatException e) { return padrao; }
    }
}