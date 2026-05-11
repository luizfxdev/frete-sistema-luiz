package br.com.nextlog.transporte;

import br.com.nextlog.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoDAO {

    public Long inserir(SolicitacaoTransporte s) throws SQLException {
        String sql = "INSERT INTO solicitacao_transporte (id_motorista, municipio_coleta, uf_coleta, logradouro_coleta, " +
                "municipio_destino, uf_destino, logradouro_destino, descricao_carga, status) " +
                "VALUES (?,?,?,?,?,?,?,?,'PENDENTE')";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, s.getIdMotorista());
            ps.setString(2, s.getMunicipioColeta());
            ps.setString(3, s.getUfColeta());
            ps.setString(4, s.getLogradouroColeta());
            ps.setString(5, s.getMunicipioDestino());
            ps.setString(6, s.getUfDestino());
            ps.setString(7, s.getLogradouroDestino());
            ps.setString(8, s.getDescricaoCarga());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public List<SolicitacaoTransporte> listarPorMotorista(Long idMotorista) throws SQLException {
        String sql = "SELECT * FROM solicitacao_transporte WHERE id_motorista=? ORDER BY criado_em DESC";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idMotorista);
            try (ResultSet rs = ps.executeQuery()) {
                List<SolicitacaoTransporte> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public SolicitacaoTransporte buscarPorId(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM solicitacao_transporte WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public void atualizarStatus(Long id, String status, String resposta) throws SQLException {
        String sql = "UPDATE solicitacao_transporte SET status=?, resposta_motorista=?, respondido_em=NOW() WHERE id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, resposta);
            ps.setLong(3, id);
            ps.executeUpdate();
        }
    }

    private SolicitacaoTransporte mapear(ResultSet rs) throws SQLException {
        SolicitacaoTransporte s = new SolicitacaoTransporte();
        s.setId(rs.getLong("id"));
        s.setIdMotorista(rs.getLong("id_motorista"));
        s.setMunicipioColeta(rs.getString("municipio_coleta"));
        s.setUfColeta(rs.getString("uf_coleta"));
        s.setLogradouroColeta(rs.getString("logradouro_coleta"));
        s.setMunicipioDestino(rs.getString("municipio_destino"));
        s.setUfDestino(rs.getString("uf_destino"));
        s.setLogradouroDestino(rs.getString("logradouro_destino"));
        s.setDescricaoCarga(rs.getString("descricao_carga"));
        s.setStatus(rs.getString("status"));
        s.setRespostaMotorista(rs.getString("resposta_motorista"));
        Timestamp criadoEm = rs.getTimestamp("criado_em");
        if (criadoEm != null) s.setCriadoEm(criadoEm.toLocalDateTime());
        Timestamp respondidoEm = rs.getTimestamp("respondido_em");
        if (respondidoEm != null) s.setRespondidoEm(respondidoEm.toLocalDateTime());
        return s;
    }
}