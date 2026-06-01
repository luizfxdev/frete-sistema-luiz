package br.com.nextlog.rota;

import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/api/cotacao/*")
public class CotacaoApi extends HttpServlet {

    private final TabelaFreteRotaBO tabelaFreteRotaBO = new TabelaFreteRotaBO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String municipioOrigem = req.getParameter("municipioOrigem");
        String ufOrigem = req.getParameter("ufOrigem");
        String municipioDestino = req.getParameter("municipioDestino");
        String ufDestino = req.getParameter("ufDestino");
        String pesoKgParam = req.getParameter("pesoKg");

        if (municipioOrigem == null || ufOrigem == null ||
            municipioDestino == null || ufDestino == null) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST,
                "Parâmetros obrigatórios: municipioOrigem, ufOrigem, municipioDestino, ufDestino.");
            return;
        }

        TabelaFreteRota rota = tabelaFreteRotaBO.buscarRota(municipioOrigem, ufOrigem, municipioDestino, ufDestino);

        if (rota == null) {
            JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Rota não cadastrada.");
            return;
        }

        BigDecimal valorSugerido = rota.getValorBase();
        if (pesoKgParam != null && !pesoKgParam.trim().isEmpty()) {
            try {
                BigDecimal pesoKg = new BigDecimal(pesoKgParam);
                valorSugerido = tabelaFreteRotaBO.calcularValorSugerido(rota.getValorBase(), rota.getValorPorKg(), pesoKg);
            } catch (NumberFormatException e) {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST,
                    "Parâmetro 'pesoKg' inválido.");
                return;
            }
        }

        String json = "{" +
            "\"valorBase\":" + rota.getValorBase() + "," +
            "\"valorPorKg\":" + rota.getValorPorKg() + "," +
            "\"valorSugerido\":" + valorSugerido +
            "}";
        JsonResponse.ok(resp, json);
    }
}