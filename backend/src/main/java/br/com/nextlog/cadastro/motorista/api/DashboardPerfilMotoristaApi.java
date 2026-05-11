package br.com.nextlog.cadastro.motorista.api;

import br.com.nextlog.cadastro.motorista.Motorista;
import br.com.nextlog.cadastro.motorista.MotoristaDAO;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.ConnectionPool;
import br.com.nextlog.util.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/api/motorista/dashboard/*")
public class DashboardPerfilMotoristaApi extends HttpServlet {

    private final MotoristaDAO motoristaDAO = new MotoristaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();
        HttpSession session = req.getSession(false);
        Long usuarioId = session != null ? (Long) session.getAttribute("usuarioId") : null;

        try {
            if (path != null && path.startsWith("/perfil/")) {
                Long idMotorista = Long.valueOf(path.substring("/perfil/".length()));
                validarAcesso(idMotorista, usuarioId);
                JsonResponse.ok(resp, getPerfil(idMotorista));

            } else if (path != null && path.startsWith("/performance/")) {
                Long idMotorista = Long.valueOf(path.substring("/performance/".length()));
                validarAcesso(idMotorista, usuarioId);
                JsonResponse.ok(resp, getPerformance(idMotorista));

            } else {
                JsonResponse.erro(resp, HttpServletResponse.SC_BAD_REQUEST, "Rota inválida.");
            }
        } catch (NegocioException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (SQLException e) {
            JsonResponse.erro(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno.");
        }
    }

    private void validarAcesso(Long idMotorista, Long usuarioId) throws SQLException {
        String sql = "SELECT 1 FROM motorista WHERE id=? AND id_usuario=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idMotorista);
            ps.setLong(2, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new NegocioException("Acesso negado.");
            }
        }
    }

    private String getPerfil(Long idMotorista) throws SQLException {
        Motorista m = motoristaDAO.buscarPorId(idMotorista);
        if (m == null) throw new NegocioException("Motorista não encontrado.");

        String sqlDocs = "SELECT tipo, caminho_arquivo FROM motorista_documento WHERE id_motorista=?";
        StringBuilder docs = new StringBuilder("[");
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlDocs)) {
            ps.setLong(1, idMotorista);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) docs.append(",");
                    docs.append("{\"tipo\":\"").append(rs.getString("tipo"))
                        .append("\",\"caminho\":\"").append(rs.getString("caminho_arquivo")).append("\"}");
                    first = false;
                }
            }
        }
        docs.append("]");

        return "{" +
                "\"id\":" + m.getId() + "," +
                "\"nome\":\"" + m.getNome() + "\"," +
                "\"cpf\":\"" + m.getCpf() + "\"," +
                "\"cnhNumero\":\"" + m.getCnhNumero() + "\"," +
                "\"cnhCategoria\":\"" + m.getCnhCategoria() + "\"," +
                "\"cnhValidade\":\"" + m.getCnhValidade() + "\"," +
                "\"status\":\"" + m.getStatus() + "\"," +
                "\"disponivel\":" + m.isDisponivel() + "," +
                "\"documentos\":" + docs +
                "}";
    }

    private String getPerformance(Long idMotorista) throws SQLException {
        String sql = "SELECT " +
                "COUNT(*) AS total_fretes, " +
                "COUNT(*) FILTER (WHERE status='ENTREGUE') AS entregas_realizadas, " +
                "COUNT(*) FILTER (WHERE status='NAO_ENTREGUE') AS nao_entregues, " +
                "COUNT(*) FILTER (WHERE status IN ('SAIDA_CONFIRMADA','EM_TRANSITO')) AS em_andamento " +
                "FROM frete WHERE id_motorista=?";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idMotorista);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int total        = rs.getInt("total_fretes");
                int realizadas   = rs.getInt("entregas_realizadas");
                int naoEntregues = rs.getInt("nao_entregues");
                int emAndamento  = rs.getInt("em_andamento");
                double taxaSucesso = total > 0 ? (realizadas * 100.0 / total) : 0;

                String ocorrencias = getUltimasOcorrencias(idMotorista, conn);

                return "{" +
                        "\"totalFretes\":" + total + "," +
                        "\"entregasRealizadas\":" + realizadas + "," +
                        "\"naoEntregues\":" + naoEntregues + "," +
                        "\"emAndamento\":" + emAndamento + "," +
                        "\"taxaSucesso\":" + String.format("%.2f", taxaSucesso) + "," +
                        "\"ultimasOcorrencias\":" + ocorrencias +
                        "}";
            }
        }
    }

    private String getUltimasOcorrencias(Long idMotorista, Connection conn) throws SQLException {
        String sql = "SELECT o.tipo, o.data_hora, o.municipio, o.uf, o.descricao " +
                "FROM ocorrencia_frete o " +
                "JOIN frete f ON f.id = o.id_frete " +
                "WHERE f.id_motorista=? " +
                "ORDER BY o.data_hora DESC LIMIT 5";
        StringBuilder sb = new StringBuilder("[");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idMotorista);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) sb.append(",");
                    sb.append("{")
                      .append("\"tipo\":\"").append(rs.getString("tipo")).append("\",")
                      .append("\"dataHora\":\"").append(rs.getTimestamp("data_hora")).append("\",")
                      .append("\"municipio\":\"").append(rs.getString("municipio")).append("\",")
                      .append("\"uf\":\"").append(rs.getString("uf")).append("\",")
                      .append("\"descricao\":").append(rs.getString("descricao") == null ? "null" : "\"" + rs.getString("descricao") + "\"")
                      .append("}");
                    first = false;
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }
}