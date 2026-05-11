package br.com.nextlog.manutencao.orcamento;

import br.com.nextlog.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoDAO {

    public Long inserir(OrcamentoManutencao o) throws SQLException {
        String sqlCabecalho = "INSERT INTO orcamento_manutencao (id_manutencao, numero, observacao, valor_total) " +
                "VALUES (?,?,?,?)";
        String sqlItem = "INSERT INTO orcamento_manutencao_item (id_orcamento, descricao, quantidade, preco_unitario) " +
                "VALUES (?,?,?,?)";

        try (Connection conn = ConnectionPool.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Long idOrcamento;
                try (PreparedStatement ps = conn.prepareStatement(sqlCabecalho, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, o.getIdManutencao());
                    ps.setString(2, o.getNumero());
                    ps.setString(3, o.getObservacao());
                    ps.setBigDecimal(4, o.getValorTotal());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        idOrcamento = rs.getLong(1);
                    }
                }

                if (o.getItens() != null) {
                    try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                        for (OrcamentoManutencaoItem item : o.getItens()) {
                            ps.setLong(1, idOrcamento);
                            ps.setString(2, item.getDescricao());
                            ps.setBigDecimal(3, item.getQuantidade());
                            ps.setBigDecimal(4, item.getPrecoUnitario());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

                conn.commit();
                return idOrcamento;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<OrcamentoManutencao> buscarPorManutencao(Long idManutencao) throws SQLException {
        String sql = "SELECT * FROM orcamento_manutencao WHERE id_manutencao=? ORDER BY criado_em DESC";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idManutencao);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrcamentoManutencao> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public void excluir(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM orcamento_manutencao WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public List<OrcamentoManutencaoItem> buscarItensPorOrcamento(Long idOrcamento) throws SQLException {
        String sql = "SELECT * FROM orcamento_manutencao_item WHERE id_orcamento=? ORDER BY id";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idOrcamento);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrcamentoManutencaoItem> lista = new ArrayList<>();
                while (rs.next()) {
                    OrcamentoManutencaoItem item = new OrcamentoManutencaoItem();
                    item.setId(rs.getLong("id"));
                    item.setIdOrcamento(rs.getLong("id_orcamento"));
                    item.setDescricao(rs.getString("descricao"));
                    item.setQuantidade(rs.getBigDecimal("quantidade"));
                    item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
                    item.setValorTotal(rs.getBigDecimal("valor_total"));
                    lista.add(item);
                }
                return lista;
            }
        }
    }

    private OrcamentoManutencao mapear(ResultSet rs) throws SQLException {
        OrcamentoManutencao o = new OrcamentoManutencao();
        o.setId(rs.getLong("id"));
        o.setIdManutencao(rs.getLong("id_manutencao"));
        o.setNumero(rs.getString("numero"));
        o.setObservacao(rs.getString("observacao"));
        o.setValorTotal(rs.getBigDecimal("valor_total"));
        Timestamp criadoEm = rs.getTimestamp("criado_em");
        if (criadoEm != null) o.setCriadoEm(criadoEm.toLocalDateTime());
        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
        if (atualizadoEm != null) o.setAtualizadoEm(atualizadoEm.toLocalDateTime());
        return o;
    }
}