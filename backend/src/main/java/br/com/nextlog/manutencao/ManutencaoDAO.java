package br.com.nextlog.manutencao;

import br.com.nextlog.enums.StatusManutencao;
import br.com.nextlog.enums.TipoManutencao;
import br.com.nextlog.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoDAO {

    public Long inserir(Connection conn, ManutencaoVeiculo m) throws SQLException {
        String sql = "INSERT INTO manutencao_veiculo (id_veiculo, tipo, placa, data_inicio, data_fim, km_atual, custo, descricao, status_manutencao) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, m);
            ps.setString(9, m.getStatusManutencao().name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public void atualizar(Connection conn, ManutencaoVeiculo m) throws SQLException {
        String sql = "UPDATE manutencao_veiculo SET tipo=?, placa=?, data_inicio=?, data_fim=?, km_atual=?, custo=?, descricao=?, status_manutencao=?, data_atualizacao=CURRENT_TIMESTAMP WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getTipo().name());
            ps.setString(2, m.getPlaca());
            ps.setDate(3, Date.valueOf(m.getDataInicio()));
            ps.setDate(4, m.getDataFim() == null ? null : Date.valueOf(m.getDataFim()));
            ps.setBigDecimal(5, m.getKmAtual());
            ps.setBigDecimal(6, m.getCusto());
            ps.setString(7, m.getDescricao());
            ps.setString(8, m.getStatusManutencao().name());
            ps.setLong(9, m.getId());
            ps.executeUpdate();
        }
    }

    public void cancelar(Connection conn, Long id) throws SQLException {
        String sql = "UPDATE manutencao_veiculo SET status_manutencao=?, data_atualizacao=CURRENT_TIMESTAMP WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, StatusManutencao.CANCELADA.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public ManutencaoVeiculo buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM manutencao_veiculo WHERE id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public ManutencaoVeiculo buscarEmAndamento(Long idVeiculo) throws SQLException {
        String sql = "SELECT * FROM manutencao_veiculo WHERE id_veiculo=? AND status_manutencao='EM_ANDAMENTO' ORDER BY data_inicio DESC LIMIT 1";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idVeiculo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<ManutencaoVeiculo> listar(String filtro, int pagina, int tamanhoPagina) throws SQLException {
        String where = (filtro == null || filtro.trim().isEmpty())
                ? ""
                : " WHERE UPPER(placa) LIKE UPPER(?) ";
        String sql = "SELECT * FROM manutencao_veiculo " + where +
                " ORDER BY data_inicio DESC LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (!where.isEmpty()) ps.setString(idx++, "%" + filtro.trim() + "%");
            ps.setInt(idx++, tamanhoPagina);
            ps.setInt(idx, (pagina - 1) * tamanhoPagina);

            try (ResultSet rs = ps.executeQuery()) {
                List<ManutencaoVeiculo> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public List<ManutencaoVeiculo> listarPorStatus(StatusManutencao status, int pagina, int tamanhoPagina) throws SQLException {
        String sql = "SELECT * FROM manutencao_veiculo WHERE status_manutencao=? ORDER BY data_inicio DESC LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, tamanhoPagina);
            ps.setInt(3, (pagina - 1) * tamanhoPagina);
            try (ResultSet rs = ps.executeQuery()) {
                List<ManutencaoVeiculo> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public List<ManutencaoVeiculo> listarPorTipo(TipoManutencao tipo, int pagina, int tamanhoPagina) throws SQLException {
        String sql = "SELECT * FROM manutencao_veiculo WHERE tipo=? ORDER BY data_inicio DESC LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            ps.setInt(2, tamanhoPagina);
            ps.setInt(3, (pagina - 1) * tamanhoPagina);
            try (ResultSet rs = ps.executeQuery()) {
                List<ManutencaoVeiculo> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public int contar(String filtro) throws SQLException {
        String where = (filtro == null || filtro.trim().isEmpty())
                ? ""
                : " WHERE UPPER(placa) LIKE UPPER(?) ";
        String sql = "SELECT COUNT(*) FROM manutencao_veiculo " + where;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!where.isEmpty()) ps.setString(1, "%" + filtro.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int contarPorStatus(StatusManutencao status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM manutencao_veiculo WHERE status_manutencao=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int contarPorTipo(TipoManutencao tipo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM manutencao_veiculo WHERE tipo=? AND status_manutencao='EM_ANDAMENTO'";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int contarLiberadasNoMes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM manutencao_veiculo WHERE status_manutencao='CONCLUIDA' AND EXTRACT(MONTH FROM data_fim)=EXTRACT(MONTH FROM CURRENT_DATE) AND EXTRACT(YEAR FROM data_fim)=EXTRACT(YEAR FROM CURRENT_DATE)";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private void preencher(PreparedStatement ps, ManutencaoVeiculo m) throws SQLException {
        ps.setLong(1, m.getIdVeiculo());
        ps.setString(2, m.getTipo().name());
        ps.setString(3, m.getPlaca());
        ps.setDate(4, Date.valueOf(m.getDataInicio()));
        ps.setDate(5, m.getDataFim() == null ? null : Date.valueOf(m.getDataFim()));
        ps.setBigDecimal(6, m.getKmAtual());
        ps.setBigDecimal(7, m.getCusto());
        ps.setString(8, m.getDescricao());
    }

    public void validarManutencao(ManutencaoVeiculo m) throws SQLException {
        
        
    }

    private ManutencaoVeiculo mapear(ResultSet rs) throws SQLException {
        ManutencaoVeiculo m = new ManutencaoVeiculo();
        m.setId(rs.getLong("id"));
        m.setIdVeiculo(rs.getLong("id_veiculo"));
        m.setTipo(TipoManutencao.valueOf(rs.getString("tipo")));
        m.setPlaca(rs.getString("placa"));
        m.setDataInicio(rs.getDate("data_inicio").toLocalDate());
        Date df = rs.getDate("data_fim");
        if (df != null) m.setDataFim(df.toLocalDate());
        m.setKmAtual(rs.getBigDecimal("km_atual"));
        m.setCusto(rs.getBigDecimal("custo"));
        m.setDescricao(rs.getString("descricao"));
        m.setStatusManutencao(StatusManutencao.valueOf(rs.getString("status_manutencao")));
        Timestamp dc = rs.getTimestamp("data_criacao");
        if (dc != null) m.setDataCriacao(dc.toLocalDateTime());
        Timestamp da = rs.getTimestamp("data_atualizacao");
        if (da != null) m.setDataAtualizacao(da.toLocalDateTime());
        m.setVeiculoPlaca(rs.getString("placa"));
        return m;
    }
}