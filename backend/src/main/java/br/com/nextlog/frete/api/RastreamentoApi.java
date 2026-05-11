package br.com.nextlog.frete.api;

import br.com.nextlog.util.ConnectionPool;
import br.com.nextlog.util.JsonResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/api/rastreamento/*")
public class RastreamentoApi extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String documento = req.getParameter("documento");

        if (documento == null || documento.trim().isEmpty()) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST,
                    "Documento é obrigatório.");
            return;
        }

        String documentoLimpo = documento.replaceAll("\\D", "");

        if (documentoLimpo.length() < 11 || documentoLimpo.length() > 14) {
            JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST,
                    "CPF ou CNPJ inválido.");
            return;
        }

        String sql =
                "SELECT DISTINCT f.numero, f.status, " +
                "       f.municipio_origem, f.uf_origem, " +
                "       f.municipio_destino, f.uf_destino, " +
                "       f.data_emissao, f.data_previsao_entrega, f.data_entrega " +
                "FROM frete f " +
                "LEFT JOIN cliente cr ON cr.id = f.id_remetente " +
                "LEFT JOIN cliente cd ON cd.id = f.id_destinatario " +
                "WHERE cr.documento = ? OR cd.documento = ? " +
                "ORDER BY f.data_emissao DESC";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, documentoLimpo);
            ps.setString(2, documentoLimpo);

            try (ResultSet rs = ps.executeQuery()) {
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;

                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                            .append("\"numero\":\"").append(rs.getString("numero")).append("\",")
                            .append("\"status\":\"").append(rs.getString("status")).append("\",")
                            .append("\"municipioOrigem\":\"").append(rs.getString("municipio_origem")).append("\",")
                            .append("\"ufOrigem\":\"").append(rs.getString("uf_origem")).append("\",")
                            .append("\"municipioDestino\":\"").append(rs.getString("municipio_destino")).append("\",")
                            .append("\"ufDestino\":\"").append(rs.getString("uf_destino")).append("\",")
                            .append("\"dataEmissao\":").append(formatDate(rs.getDate("data_emissao"))).append(",")
                            .append("\"dataPrevisaoEntrega\":").append(formatDate(rs.getDate("data_previsao_entrega"))).append(",")
                            .append("\"dataEntrega\":").append(formatDate(rs.getDate("data_entrega")))
                            .append("}");
                    first = false;
                }
                sb.append("]");

                if (first) {
                    JsonResponse.erro(resp, HttpServletResponse.SC_NOT_FOUND,
                            "Nenhuma encomenda encontrada para este CPF/CNPJ.");
                    return;
                }

                JsonResponse.ok(resp, sb.toString());
            }
        } catch (SQLException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro ao consultar rastreamento. Tente novamente.");
        }
    }

    private String formatDate(java.sql.Date d) {
        return d == null ? "null" : "\"" + d.toString() + "\"";
    }
}