package br.com.nextlog.manutencao;

import br.com.nextlog.enums.TipoManutencao;
import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoDAO {

    public Long inserir(ManutencaoVeiculo m) throws SQLException {
        String sql = "INSERT INTO manutencao_veiculo (id_veiculo, tipo, descricao, data_inicio, data_fim, custo) " +
                "VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, m);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public void atualizar(ManutencaoVeiculo m) throws SQLException {
        String sql = "UPDATE manutencao_veiculo SET id_veiculo=?, tipo=?, descricao=?, data_inicio=?, data_fim=?, custo=? WHERE id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, m);
            ps.setLong(7, m.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM manutencao_veiculo WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public ManutencaoVeiculo buscarPorId(Long id) throws SQLException {
        String sql = "SELECT mv.*, v.placa AS veiculo_placa FROM manutencao_veiculo mv " +
                "JOIN veiculo v ON v.id = mv.id_veiculo WHERE mv.id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<ManutencaoVeiculo> listar(String filtro, int pagina, int tamanhoPagina) throws SQLException {
        String where = (filtro == null || filtro.trim().isEmpty())
                ? ""
                : " WHERE UPPER(v.placa) LIKE ? ";
        String sql = "SELECT mv.*, v.placa AS veiculo_placa FROM manutencao_veiculo mv " +
                "JOIN veiculo v ON v.id = mv.id_veiculo " + where +
                " ORDER BY mv.data_inicio DESC LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (!where.isEmpty()) ps.setString(idx++, "%" + filtro.toUpperCase() + "%");
            ps.setInt(idx++, tamanhoPagina);
            ps.setInt(idx, (pagina - 1) * tamanhoPagina);

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
                : " WHERE UPPER(v.placa) LIKE ? ";
        String sql = "SELECT COUNT(*) FROM manutencao_veiculo mv JOIN veiculo v ON v.id=mv.id_veiculo " + where;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!where.isEmpty()) ps.setString(1, "%" + filtro.toUpperCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private void preencher(PreparedStatement ps, ManutencaoVeiculo m) throws SQLException {
        ps.setLong(1, m.getIdVeiculo());
        ps.setString(2, m.getTipo().name());
        ps.setString(3, m.getDescricao());
        ps.setDate(4, Date.valueOf(m.getDataInicio()));
        ps.setDate(5, m.getDataFim() == null ? null : Date.valueOf(m.getDataFim()));
        ps.setBigDecimal(6, m.getCusto());
    }

    private ManutencaoVeiculo mapear(ResultSet rs) throws SQLException {
        ManutencaoVeiculo m = new ManutencaoVeiculo();
        m.setId(rs.getLong("id"));
        m.setIdVeiculo(rs.getLong("id_veiculo"));
        m.setTipo(TipoManutencao.valueOf(rs.getString("tipo")));
        m.setDescricao(rs.getString("descricao"));
        m.setDataInicio(rs.getDate("data_inicio").toLocalDate());
        Date df = rs.getDate("data_fim");
        if (df != null) m.setDataFim(df.toLocalDate());
        m.setCusto(rs.getBigDecimal("custo"));
        m.setVeiculoPlaca(rs.getString("veiculo_placa"));
        return m;
    }
}