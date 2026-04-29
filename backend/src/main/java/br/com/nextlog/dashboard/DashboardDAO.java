package br.com.nextlog.dashboard;

import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAO {

    public Map<String, Object> kpis() throws SQLException {
        Map<String, Object> map = new LinkedHashMap<>();
        try (Connection conn = ConnectionPool.getConnection()) {
            map.put("totalFretes",          contar(conn, "SELECT COUNT(*) FROM frete"));
            map.put("emAberto",             contar(conn, "SELECT COUNT(*) FROM frete WHERE status IN ('EMITIDO','SAIDA_CONFIRMADA','EM_TRANSITO')"));
            map.put("entregues",            contar(conn, "SELECT COUNT(*) FROM frete WHERE status='ENTREGUE'"));
            map.put("naoEntregues",         contar(conn, "SELECT COUNT(*) FROM frete WHERE status='NAO_ENTREGUE'"));
            map.put("cancelados",           contar(conn, "SELECT COUNT(*) FROM frete WHERE status='CANCELADO'"));
            map.put("motoristasAtivos",     contar(conn, "SELECT COUNT(*) FROM motorista WHERE status='ATIVO'"));
            map.put("veiculosDisponiveis",  contar(conn, "SELECT COUNT(*) FROM veiculo WHERE status='DISPONIVEL'"));
            map.put("veiculosEmViagem",     contar(conn, "SELECT COUNT(*) FROM veiculo WHERE status='EM_VIAGEM'"));
        }
        return map;
    }

    public List<Map<String, Object>> entregasPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        String sql = "SELECT data_emissao::date AS dia, " +
                "  SUM(CASE WHEN status='ENTREGUE'    THEN 1 ELSE 0 END) AS entregues, " +
                "  SUM(CASE WHEN status='NAO_ENTREGUE' THEN 1 ELSE 0 END) AS nao_entregues, " +
                "  SUM(CASE WHEN status='CANCELADO'    THEN 1 ELSE 0 END) AS cancelados, " +
                "  COUNT(*) AS total " +
                "FROM frete WHERE data_emissao BETWEEN ? AND ? " +
                "GROUP BY data_emissao::date ORDER BY dia";

        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> linha = new LinkedHashMap<>();
                    linha.put("dia",          rs.getDate("dia").toLocalDate());
                    linha.put("entregues",    rs.getInt("entregues"));
                    linha.put("naoEntregues", rs.getInt("nao_entregues"));
                    linha.put("cancelados",   rs.getInt("cancelados"));
                    linha.put("total",        rs.getInt("total"));
                    lista.add(linha);
                }
            }
        }
        return lista;
    }

    public List<Map<String, Object>> rankingMotoristas(int limite) throws SQLException {
        String sql = "SELECT m.id, m.nome, " +
                "  COUNT(f.id) AS total, " +
                "  SUM(CASE WHEN f.status='ENTREGUE' THEN 1 ELSE 0 END) AS entregues, " +
                "  CASE WHEN COUNT(f.id)=0 THEN 0 " +
                "       ELSE ROUND(100.0 * SUM(CASE WHEN f.status='ENTREGUE' THEN 1 ELSE 0 END) / COUNT(f.id), 2) " +
                "  END AS taxa_sucesso " +
                "FROM motorista m LEFT JOIN frete f ON f.id_motorista=m.id " +
                "GROUP BY m.id, m.nome ORDER BY taxa_sucesso DESC, total DESC LIMIT ?";

        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> linha = new LinkedHashMap<>();
                    linha.put("id",           rs.getLong("id"));
                    linha.put("nome",         rs.getString("nome"));
                    linha.put("total",        rs.getInt("total"));
                    linha.put("entregues",    rs.getInt("entregues"));
                    linha.put("taxaSucesso",  rs.getBigDecimal("taxa_sucesso"));
                    lista.add(linha);
                }
            }
        }
        return lista;
    }

    public List<Map<String, Object>> rotasMaisMovimentadas(int limite) throws SQLException {
        String sql = "SELECT municipio_origem, uf_origem, municipio_destino, uf_destino, COUNT(*) AS total " +
                "FROM frete GROUP BY municipio_origem, uf_origem, municipio_destino, uf_destino " +
                "ORDER BY total DESC LIMIT ?";

        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> linha = new LinkedHashMap<>();
                    linha.put("origem",   rs.getString("municipio_origem") + "/" + rs.getString("uf_origem"));
                    linha.put("destino",  rs.getString("municipio_destino") + "/" + rs.getString("uf_destino"));
                    linha.put("total",    rs.getInt("total"));
                    lista.add(linha);
                }
            }
        }
        return lista;
    }

    private int contar(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}