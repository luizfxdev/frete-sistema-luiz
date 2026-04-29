package br.com.nextlog.cadastro.veiculo;

import br.com.nextlog.enums.StatusVeiculo;
import br.com.nextlog.enums.TipoVeiculo;
import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public Long inserir(Veiculo v) throws SQLException {
        String sql = "INSERT INTO veiculo (placa, rntrc, ano_fabricacao, tipo, tara_kg, capacidade_kg, volume_m3, status) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, v);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public void atualizar(Veiculo v) throws SQLException {
        String sql = "UPDATE veiculo SET placa=?, rntrc=?, ano_fabricacao=?, tipo=?, tara_kg=?, capacidade_kg=?, " +
                "volume_m3=?, status=? WHERE id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, v);
            ps.setLong(9, v.getId());
            ps.executeUpdate();
        }
    }

    public void atualizarStatus(Connection conn, Long id, StatusVeiculo status) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE veiculo SET status=? WHERE id=?")) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public Veiculo buscarPorId(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM veiculo WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public boolean existePorPlaca(String placa, Long idIgnorar) throws SQLException {
        String sql = idIgnorar == null
                ? "SELECT 1 FROM veiculo WHERE placa=?"
                : "SELECT 1 FROM veiculo WHERE placa=? AND id<>?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, placa);
            if (idIgnorar != null) ps.setLong(2, idIgnorar);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean estaEmFreteAtivo(Long idVeiculo) throws SQLException {
        String sql = "SELECT 1 FROM frete WHERE id_veiculo=? AND status IN ('SAIDA_CONFIRMADA','EM_TRANSITO') LIMIT 1";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idVeiculo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Veiculo> listar(String filtro, int pagina, int tamanhoPagina) throws SQLException {
        String where = (filtro == null || filtro.trim().isEmpty())
                ? ""
                : " WHERE UPPER(placa) LIKE ? ";
        String sql = "SELECT * FROM veiculo" + where + " ORDER BY placa LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (!where.isEmpty()) {
                ps.setString(idx++, "%" + filtro.toUpperCase() + "%");
            }
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
        String where = (filtro == null || filtro.trim().isEmpty())
                ? ""
                : " WHERE UPPER(placa) LIKE ? ";
        String sql = "SELECT COUNT(*) FROM veiculo" + where;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!where.isEmpty()) {
                ps.setString(1, "%" + filtro.toUpperCase() + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public List<Veiculo> listarDisponiveis() throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM veiculo WHERE status='DISPONIVEL' ORDER BY placa");
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

    private void preencher(PreparedStatement ps, Veiculo v) throws SQLException {
        ps.setString(1, v.getPlaca());
        ps.setString(2, v.getRntrc());
        ps.setInt(3, v.getAnoFabricacao());
        ps.setString(4, v.getTipo().name());
        ps.setBigDecimal(5, v.getTaraKg());
        ps.setBigDecimal(6, v.getCapacidadeKg());
        ps.setBigDecimal(7, v.getVolumeM3());
        ps.setString(8, v.getStatus().name());
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