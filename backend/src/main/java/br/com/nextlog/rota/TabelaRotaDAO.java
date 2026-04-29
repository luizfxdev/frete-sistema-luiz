package br.com.nextlog.rota;

import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TabelaRotaDAO {

    public Long inserir(TabelaFreteRota r) throws SQLException {
        String sql = "INSERT INTO tabela_frete_rota (municipio_origem, uf_origem, municipio_destino, uf_destino, valor_base, valor_por_kg) " +
                "VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, r);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public void atualizar(TabelaFreteRota r) throws SQLException {
        String sql = "UPDATE tabela_frete_rota SET municipio_origem=?, uf_origem=?, municipio_destino=?, " +
                "uf_destino=?, valor_base=?, valor_por_kg=? WHERE id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, r);
            ps.setLong(7, r.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM tabela_frete_rota WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public TabelaFreteRota buscarPorId(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM tabela_frete_rota WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public TabelaFreteRota buscarPorRota(String municipioOrigem, String ufOrigem,
                                         String municipioDestino, String ufDestino) throws SQLException {
        String sql = "SELECT * FROM tabela_frete_rota WHERE LOWER(municipio_origem)=LOWER(?) AND uf_origem=? " +
                "AND LOWER(municipio_destino)=LOWER(?) AND uf_destino=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, municipioOrigem);
            ps.setString(2, ufOrigem);
            ps.setString(3, municipioDestino);
            ps.setString(4, ufDestino);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<TabelaFreteRota> listar(int pagina, int tamanhoPagina) throws SQLException {
        String sql = "SELECT * FROM tabela_frete_rota ORDER BY municipio_origem, municipio_destino LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tamanhoPagina);
            ps.setInt(2, (pagina - 1) * tamanhoPagina);
            try (ResultSet rs = ps.executeQuery()) {
                List<TabelaFreteRota> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public int contar() throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tabela_frete_rota");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private void preencher(PreparedStatement ps, TabelaFreteRota r) throws SQLException {
        ps.setString(1, r.getMunicipioOrigem());
        ps.setString(2, r.getUfOrigem());
        ps.setString(3, r.getMunicipioDestino());
        ps.setString(4, r.getUfDestino());
        ps.setBigDecimal(5, r.getValorBase());
        ps.setBigDecimal(6, r.getValorPorKg());
    }

    private TabelaFreteRota mapear(ResultSet rs) throws SQLException {
        TabelaFreteRota r = new TabelaFreteRota();
        r.setId(rs.getLong("id"));
        r.setMunicipioOrigem(rs.getString("municipio_origem"));
        r.setUfOrigem(rs.getString("uf_origem"));
        r.setMunicipioDestino(rs.getString("municipio_destino"));
        r.setUfDestino(rs.getString("uf_destino"));
        r.setValorBase(rs.getBigDecimal("valor_base"));
        r.setValorPorKg(rs.getBigDecimal("valor_por_kg"));
        return r;
    }
}