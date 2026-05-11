package br.com.nextlog.candidatura;

import br.com.nextlog.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidaturaDAO {

    public Long inserir(CandidaturaMotorista c) throws SQLException {
        String sql = "INSERT INTO candidatura_motorista (nome, cpf, data_nascimento, telefone, email, " +
                "cnh_numero, cnh_categoria, cnh_validade, tipo_vinculo, municipio, uf, mensagem, status) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,'PENDENTE')";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setDate(3, Date.valueOf(c.getDataNascimento()));
            ps.setString(4, c.getTelefone());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getCnhNumero());
            ps.setString(7, c.getCnhCategoria());
            ps.setDate(8, Date.valueOf(c.getCnhValidade()));
            ps.setString(9, c.getTipoVinculo());
            ps.setString(10, c.getMunicipio());
            ps.setString(11, c.getUf());
            ps.setString(12, c.getMensagem());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public List<CandidaturaMotorista> listar() throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM candidatura_motorista ORDER BY criado_em DESC");
             ResultSet rs = ps.executeQuery()) {
            List<CandidaturaMotorista> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        }
    }

    public CandidaturaMotorista buscarPorId(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM candidatura_motorista WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public void atualizarStatus(Long id, String status) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE candidatura_motorista SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    private CandidaturaMotorista mapear(ResultSet rs) throws SQLException {
        CandidaturaMotorista c = new CandidaturaMotorista();
        c.setId(rs.getLong("id"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setCnhNumero(rs.getString("cnh_numero"));
        c.setCnhCategoria(rs.getString("cnh_categoria"));
        c.setCnhValidade(rs.getDate("cnh_validade").toLocalDate());
        c.setTipoVinculo(rs.getString("tipo_vinculo"));
        c.setMunicipio(rs.getString("municipio"));
        c.setUf(rs.getString("uf"));
        c.setMensagem(rs.getString("mensagem"));
        c.setStatus(rs.getString("status"));
        Timestamp criadoEm = rs.getTimestamp("criado_em");
        if (criadoEm != null) c.setCriadoEm(criadoEm.toLocalDateTime());
        return c;
    }
}