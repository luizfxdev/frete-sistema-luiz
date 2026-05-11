package br.com.nextlog.rota;

import br.com.nextlog.util.ConnectionPool;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/api/cotacao/*")
public class CotacaoApi extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String municipioOrigem  = req.getParameter("municipioOrigem");
        String ufOrigem         = req.getParameter("ufOrigem");
        String municipioDestino = req.getParameter("municipioDestino");
        String ufDestino        = req.getParameter("ufDestino");
        String pesoKgParam      = req.getParameter("pesoKg");

        if (municipioOrigem == null || ufOrigem == null
                || municipioDestino == null || ufDestino == null) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST,
                    "Parâmetros obrigatórios: municipioOrigem, ufOrigem, municipioDestino, ufDestino.");
            return;
        }

        String sql = "SELECT valor_base, valor_por_kg FROM tabela_frete_rota " +
                     "WHERE LOWER(municipio_origem)=LOWER(?) AND UPPER(uf_origem)=UPPER(?) " +
                     "AND LOWER(municipio_destino)=LOWER(?) AND UPPER(uf_destino)=UPPER(?)";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, municipioOrigem);
            ps.setString(2, ufOrigem);
            ps.setString(3, municipioDestino);
            ps.setString(4, ufDestino);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND, "Rota não cadastrada.");
                    return;
                }

                BigDecimal valorBase  = rs.getBigDecimal("valor_base");
                BigDecimal valorPorKg = rs.getBigDecimal("valor_por_kg");
                BigDecimal valorSugerido = valorBase;

                if (pesoKgParam != null && !pesoKgParam.trim().isEmpty()) {
                    try {
                        BigDecimal pesoKg = new BigDecimal(pesoKgParam);
                        valorSugerido = valorBase.add(valorPorKg.multiply(pesoKg));
                    } catch (NumberFormatException e) {
                        JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST,
                                "Parâmetro 'pesoKg' inválido.");
                        return;
                    }
                }

                String json = "{"
                        + "\"valorBase\":" + valorBase + ","
                        + "\"valorPorKg\":" + valorPorKg + ","
                        + "\"valorSugerido\":" + valorSugerido
                        + "}";
                JsonResponse.ok(resp, json);
            }
        } catch (SQLException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro ao consultar cotação.");
        }
    }
}