package br.com.nextlog.cadastro.veiculo;

import br.com.nextlog.enums.StatusVeiculo;
import br.com.nextlog.enums.TipoVeiculo;
import br.com.nextlog.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public Long inserir(Veiculo v) throws SQLException {
        String sql = "INSERT INTO veiculo (placa, rntrc, ano_fabricacao, tipo, tara_kg, capacidade_kg, volume_m3, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getRntrc());
            ps.setInt(3, v.getAnoFabricacao());
            ps.setString(4, v.getTipo().name());
            ps.setBigDecimal(5, v.getTaraKg());
            ps.setBigDecimal(6, v.getCapacidadeKg());
            ps.setBigDecimal(7, v.getVolumeM3());
            ps.setString(8, v.getStatus().name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    public void atualizar(Veiculo v) throws SQLException {
        String sql = "UPDATE veiculo SET placa=?, rntrc=?, ano_fabricacao=?, tipo=?, tara_kg=?, " +
                     "capacidade_kg=?, volume_m3=?, status=? WHERE id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getRntrc());
            ps.setInt(3, v.getAnoFabricacao());
            ps.setString(4, v.getTipo().name());
            ps.setBigDecimal(5, v.getTaraKg());
            ps.setBigDecimal(6, v.getCapacidadeKg());
            ps.setBigDecimal(7, v.getVolumeM3());
            ps.setString(8, v.getStatus().name());
            ps.setLong(9, v.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM veiculo WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Veiculo buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM veiculo WHERE id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<Veiculo> listar(String filtro, int pagina, int tamanhoPagina) throws SQLException {
        String where = (filtro != null && !filtro.trim().isEmpty()) ? " WHERE UPPER(placa) LIKE UPPER(?)" : "";
        String sql = "SELECT * FROM veiculo" + where + " ORDER BY placa LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (!where.isEmpty()) ps.setString(idx++, "%" + filtro.trim() + "%");
            ps.setInt(idx++, tamanhoPagina);
            ps.setInt(idx, (pagina - 1) * tamanhoPagina);
            try (ResultSet rs = ps.executeQuery()) {
                List<Veiculo> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public int contar(String filtro) throws SQLException {
        String where = (filtro != null && !filtro.trim().isEmpty()) ? " WHERE UPPER(placa) LIKE UPPER(?)" : "";
        String sql = "SELECT COUNT(*) FROM veiculo" + where;
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!where.isEmpty()) ps.setString(1, "%" + filtro.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int contarPorStatus(StatusVeiculo status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM veiculo WHERE status = ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public boolean existePorPlaca(String placa, Long idIgnorar) throws SQLException {
        String sql = "SELECT COUNT(*) FROM veiculo WHERE UPPER(placa)=UPPER(?)" +
                     (idIgnorar != null ? " AND id <> ?" : "");
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, placa);
            if (idIgnorar != null) ps.setLong(2, idIgnorar);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean estaEmFreteAtivo(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM frete WHERE id_veiculo=? " +
                     "AND status IN ('EMITIDO','SAIDA_CONFIRMADA','EM_TRANSITO')";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public List<Veiculo> listarDisponiveis() throws SQLException {
        String sql = "SELECT * FROM veiculo WHERE status='DISPONIVEL' ORDER BY placa";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Veiculo> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        }
    }

    public List<Veiculo> listarTodos() throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM veiculo ORDER BY placa");
             ResultSet rs = ps.executeQuery()) {
            List<Veiculo> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        }
    }

    public void atualizarStatus(Long id, StatusVeiculo status, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE veiculo SET status=? WHERE id=?")) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    private Veiculo mapear(ResultSet rs) throws SQLException {
        Veiculo v = new Veiculo();
        v.setId(rs.getLong("id"));
        v.setPlaca(rs.getString("placa"));
        v.setRntrc(rs.getString("rntrc"));
        v.setAnoFabricacao(rs.getInt("ano_fabricacao"));
        v.setTipo(TipoVeiculo.valueOf(rs.getString("tipo")));
        v.setTaraKg(rs.getBigDecimal("tara_kg"));
        v.setCapacidadeKg(rs.getBigDecimal("capacidade_kg"));
        v.setVolumeM3(rs.getBigDecimal("volume_m3"));
        v.setStatus(StatusVeiculo.valueOf(rs.getString("status")));
        return v;
    }
}