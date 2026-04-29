package br.com.nextlog.cadastro.motorista;

import br.com.nextlog.enums.CnhCategoria;
import br.com.nextlog.enums.StatusFrete;
import br.com.nextlog.enums.StatusMotorista;
import br.com.nextlog.enums.TipoVinculo;
import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MotoristaDAO {

    public Long inserir(Motorista m) throws SQLException {
        String sql = "INSERT INTO motorista (nome, cpf, data_nascimento, telefone, cnh_numero, cnh_categoria, " +
                "cnh_validade, tipo_vinculo, status) VALUES (?,?,?,?,?,?,?,?,?)";
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

    public void atualizar(Motorista m) throws SQLException {
        String sql = "UPDATE motorista SET nome=?, cpf=?, data_nascimento=?, telefone=?, cnh_numero=?, " +
                "cnh_categoria=?, cnh_validade=?, tipo_vinculo=?, status=? WHERE id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, m);
            ps.setLong(10, m.getId());
            ps.executeUpdate();
        }
    }

    public Motorista buscarPorId(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM motorista WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public boolean existePorCpf(String cpf, Long idIgnorar) throws SQLException {
        String sql = idIgnorar == null
                ? "SELECT 1 FROM motorista WHERE cpf=?"
                : "SELECT 1 FROM motorista WHERE cpf=? AND id<>?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            if (idIgnorar != null) ps.setLong(2, idIgnorar);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean possuiFreteAtivo(Long idMotorista) throws SQLException {
        String sql = "SELECT 1 FROM frete WHERE id_motorista=? AND status IN ('EMITIDO','SAIDA_CONFIRMADA','EM_TRANSITO') LIMIT 1";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idMotorista);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean possuiFreteEmAndamento(Long idMotorista) throws SQLException {
        String sql = "SELECT 1 FROM frete WHERE id_motorista=? AND status IN ('SAIDA_CONFIRMADA','EM_TRANSITO') LIMIT 1";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idMotorista);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Motorista> listar(String filtro, int pagina, int tamanhoPagina) throws SQLException {
        String where = (filtro == null || filtro.trim().isEmpty())
                ? ""
                : " WHERE LOWER(nome) LIKE ? OR cpf LIKE ? ";
        String sql = "SELECT * FROM motorista" + where + " ORDER BY nome LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (!where.isEmpty()) {
                String like = "%" + filtro.toLowerCase() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, "%" + filtro.replaceAll("\\D", "") + "%");
            }
            ps.setInt(idx++, tamanhoPagina);
            ps.setInt(idx, (pagina - 1) * tamanhoPagina);

            try (ResultSet rs = ps.executeQuery()) {
                List<Motorista> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public int contar(String filtro) throws SQLException {
        String where = (filtro == null || filtro.trim().isEmpty())
                ? ""
                : " WHERE LOWER(nome) LIKE ? OR cpf LIKE ? ";
        String sql = "SELECT COUNT(*) FROM motorista" + where;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!where.isEmpty()) {
                String like = "%" + filtro.toLowerCase() + "%";
                ps.setString(1, like);
                ps.setString(2, "%" + filtro.replaceAll("\\D", "") + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public List<Motorista> listarAtivos() throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM motorista WHERE status='ATIVO' ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            List<Motorista> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        }
    }

    private void preencher(PreparedStatement ps, Motorista m) throws SQLException {
        ps.setString(1, m.getNome());
        ps.setString(2, m.getCpf());
        ps.setDate(3, Date.valueOf(m.getDataNascimento()));
        ps.setString(4, m.getTelefone());
        ps.setString(5, m.getCnhNumero());
        ps.setString(6, m.getCnhCategoria().name());
        ps.setDate(7, Date.valueOf(m.getCnhValidade()));
        ps.setString(8, m.getTipoVinculo().name());
        ps.setString(9, m.getStatus().name());
    }

    private Motorista mapear(ResultSet rs) throws SQLException {
        Motorista m = new Motorista();
        m.setId(rs.getLong("id"));
        m.setNome(rs.getString("nome"));
        m.setCpf(rs.getString("cpf"));
        m.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        m.setTelefone(rs.getString("telefone"));
        m.setCnhNumero(rs.getString("cnh_numero"));
        m.setCnhCategoria(CnhCategoria.valueOf(rs.getString("cnh_categoria")));
        m.setCnhValidade(rs.getDate("cnh_validade").toLocalDate());
        m.setTipoVinculo(TipoVinculo.valueOf(rs.getString("tipo_vinculo")));
        m.setStatus(StatusMotorista.valueOf(rs.getString("status")));
        return m;
    }
}