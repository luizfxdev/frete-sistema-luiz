package br.com.nextlog.manutencao.orcamento;

import br.com.nextlog.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoDAO {

    public List<OrcamentoManutencao> buscarPorManutencao(Long idManutencao) throws SQLException {
        String sql = "SELECT * FROM orcamento_manutencao WHERE id_manutencao = ? ORDER BY criado_em DESC";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idManutencao);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrcamentoManutencao> lista = new ArrayList<>();
                while (rs.next()) {
                    OrcamentoManutencao o = mapearOrcamento(rs);
                    o.setItens(buscarItens(o.getId()));
                    lista.add(o);
                }
                return lista;
            }
        }
    }

    public List<OrcamentoManutencaoItem> buscarItens(Long idManutencao) throws SQLException {
        String sql = "SELECT omi.* FROM orcamento_manutencao_item omi " +
                "JOIN orcamento_manutencao om ON omi.id_orcamento = om.id " +
                "WHERE om.id_manutencao = ? ORDER BY omi.criado_em";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idManutencao);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrcamentoManutencaoItem> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapearItem(rs));
                }
                return lista;
            }
        }
    }

    public List<OrcamentoManutencaoItem> buscarItensPorOrcamento(Long idOrcamento) throws SQLException {
        String sql = "SELECT * FROM orcamento_manutencao_item WHERE id_orcamento = ? ORDER BY criado_em";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idOrcamento);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrcamentoManutencaoItem> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapearItem(rs));
                }
                return lista;
            }
        }
    }

    public Long salvarOrcamento(Connection conn, OrcamentoManutencao o) throws SQLException {
        if (o.getId() != null) {
            atualizarOrcamento(conn, o);
            return o.getId();
        }
        String sql = "INSERT INTO orcamento_manutencao (id_manutencao, numero, observacao, valor_total) " +
                "VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, o.getIdManutencao());
            ps.setString(2, o.getNumero());
            ps.setString(3, o.getObservacao());
            ps.setBigDecimal(4, o.getValorTotal());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public void atualizarOrcamento(Connection conn, OrcamentoManutencao o) throws SQLException {
        String sql = "UPDATE orcamento_manutencao SET observacao = ?, valor_total = ?, atualizado_em = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getObservacao());
            ps.setBigDecimal(2, o.getValorTotal());
            ps.setLong(3, o.getId());
            ps.executeUpdate();
        }
    }

    public Long salvarItem(Connection conn, OrcamentoManutencaoItem item) throws SQLException {
        if (item.getId() != null) {
            atualizarItem(conn, item);
            return item.getId();
        }
        String sql = "INSERT INTO orcamento_manutencao_item (id_orcamento, descricao, quantidade, preco_unitario) " +
                "VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, item.getIdOrcamento());
            ps.setString(2, item.getDescricao());
            ps.setBigDecimal(3, item.getQuantidade());
            ps.setBigDecimal(4, item.getPrecoUnitario());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public void atualizarItem(Connection conn, OrcamentoManutencaoItem item) throws SQLException {
        String sql = "UPDATE orcamento_manutencao_item SET descricao = ?, quantidade = ?, preco_unitario = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getDescricao());
            ps.setBigDecimal(2, item.getQuantidade());
            ps.setBigDecimal(3, item.getPrecoUnitario());
            ps.setLong(4, item.getId());
            ps.executeUpdate();
        }
    }

    public void deletarItem(Connection conn, Long id) throws SQLException {
        String sql = "DELETE FROM orcamento_manutencao_item WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public void deletarOrcamento(Connection conn, Long id) throws SQLException {
        String sql = "DELETE FROM orcamento_manutencao WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private OrcamentoManutencao mapearOrcamento(ResultSet rs) throws SQLException {
        OrcamentoManutencao o = new OrcamentoManutencao();
        o.setId(rs.getLong("id"));
        o.setIdManutencao(rs.getLong("id_manutencao"));
        o.setNumero(rs.getString("numero"));
        o.setObservacao(rs.getString("observacao"));
        o.setValorTotal(rs.getBigDecimal("valor_total"));
        Timestamp ct = rs.getTimestamp("criado_em");
        if (ct != null) o.setCriadoEm(ct.toLocalDateTime());
        Timestamp at = rs.getTimestamp("atualizado_em");
        if (at != null) o.setAtualizadoEm(at.toLocalDateTime());
        return o;
    }

    private OrcamentoManutencaoItem mapearItem(ResultSet rs) throws SQLException {
        OrcamentoManutencaoItem item = new OrcamentoManutencaoItem();
        item.setId(rs.getLong("id"));
        item.setIdOrcamento(rs.getLong("id_orcamento"));
        item.setDescricao(rs.getString("descricao"));
        item.setQuantidade(rs.getBigDecimal("quantidade"));
        item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
        item.setValorTotal(rs.getBigDecimal("valor_total"));
        Timestamp ct = rs.getTimestamp("criado_em");
        if (ct != null) item.setCriadoEm(ct.toLocalDateTime());
        return item;
    }
}