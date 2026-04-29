package br.com.nextlog.frete.ocorrencia;

import br.com.nextlog.enums.TipoOcorrencia;
import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OcorrenciaDAO {

    public Long inserir(Connection conn, OcorrenciaFrete o) throws SQLException {
        String sql = "INSERT INTO ocorrencia_frete (id_frete, tipo, data_hora, municipio, uf, descricao, " +
                "nome_recebedor, documento_recebedor) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, o.getIdFrete());
            ps.setString(2, o.getTipo().name());
            ps.setTimestamp(3, Timestamp.valueOf(o.getDataHora()));
            ps.setString(4, o.getMunicipio());
            ps.setString(5, o.getUf());
            ps.setString(6, o.getDescricao());
            ps.setString(7, o.getNomeRecebedor());
            ps.setString(8, o.getDocumentoRecebedor());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public LocalDateTime buscarDataHoraUltimaOcorrencia(Connection conn, Long idFrete) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT MAX(data_hora) FROM ocorrencia_frete WHERE id_frete=?")) {
            ps.setLong(1, idFrete);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp t = rs.getTimestamp(1);
                    return t == null ? null : t.toLocalDateTime();
                }
            }
        }
        return null;
    }

    public List<OcorrenciaFrete> listarPorFrete(Long idFrete) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM ocorrencia_frete WHERE id_frete=? ORDER BY data_hora")) {
            ps.setLong(1, idFrete);
            try (ResultSet rs = ps.executeQuery()) {
                List<OcorrenciaFrete> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    private OcorrenciaFrete mapear(ResultSet rs) throws SQLException {
        OcorrenciaFrete o = new OcorrenciaFrete();
        o.setId(rs.getLong("id"));
        o.setIdFrete(rs.getLong("id_frete"));
        o.setTipo(TipoOcorrencia.valueOf(rs.getString("tipo")));
        o.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        o.setMunicipio(rs.getString("municipio"));
        o.setUf(rs.getString("uf"));
        o.setDescricao(rs.getString("descricao"));
        o.setNomeRecebedor(rs.getString("nome_recebedor"));
        o.setDocumentoRecebedor(rs.getString("documento_recebedor"));
        return o;
    }
}