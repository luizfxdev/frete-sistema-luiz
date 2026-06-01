package br.com.nextlog.rota;

import br.com.nextlog.util.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TabelaFreteRotaDAO {

    public TabelaFreteRota buscarRota(String municipioOrigem, String ufOrigem,
                                       String municipioDestino, String ufDestino) throws SQLException {
        String sql = "SELECT * FROM tabela_frete_rota " +
                "WHERE LOWER(municipio_origem) = LOWER(?) AND UPPER(uf_origem) = UPPER(?) " +
                "AND LOWER(municipio_destino) = LOWER(?) AND UPPER(uf_destino) = UPPER(?)";

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

    private TabelaFreteRota mapear(ResultSet rs) throws SQLException {
        TabelaFreteRota rota = new TabelaFreteRota();
        rota.setId(rs.getLong("id"));
        rota.setMunicipioOrigem(rs.getString("municipio_origem"));
        rota.setUfOrigem(rs.getString("uf_origem"));
        rota.setMunicipioDestino(rs.getString("municipio_destino"));
        rota.setUfDestino(rs.getString("uf_destino"));
        rota.setValorBase(rs.getBigDecimal("valor_base"));
        rota.setValorPorKg(rs.getBigDecimal("valor_por_kg"));
        return rota;
    }
}