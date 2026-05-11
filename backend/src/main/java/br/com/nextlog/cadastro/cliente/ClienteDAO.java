package br.com.nextlog.cadastro.cliente;

import br.com.nextlog.enums.TipoCliente;
import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String SQL_LISTAGEM =
        "SELECT id, razao_social, nome_fantasia, tipo_documento, documento, " +
        "tipo, municipio, uf, status FROM cliente";

    private static final String SQL_COMPLETO =
        "SELECT id, razao_social, nome_fantasia, tipo_documento, documento, " +
        "inscricao_estadual, tipo, logradouro, numero, complemento, bairro, " +
        "municipio, uf, cep, telefone, email, status FROM cliente";

    public Long inserir(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (razao_social, nome_fantasia, tipo_documento, documento, inscricao_estadual, tipo, " +
                "logradouro, numero, complemento, bairro, municipio, uf, cep, telefone, email, status) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, c);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public void atualizar(Cliente c) throws SQLException {
        String sql = "UPDATE cliente SET razao_social=?, nome_fantasia=?, tipo_documento=?, documento=?, inscricao_estadual=?, tipo=?, " +
                "logradouro=?, numero=?, complemento=?, bairro=?, municipio=?, uf=?, cep=?, telefone=?, email=?, status=? " +
                "WHERE id=?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, c);
            ps.setLong(17, c.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM cliente WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Cliente buscarPorId(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COMPLETO + " WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public boolean existePorDocumento(String documento, Long idIgnorar) throws SQLException {
        String sql = idIgnorar == null
                ? "SELECT 1 FROM cliente WHERE documento=?"
                : "SELECT 1 FROM cliente WHERE documento=? AND id<>?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documento);
            if (idIgnorar != null) ps.setLong(2, idIgnorar);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean possuiFretes(Long idCliente) throws SQLException {
        String sql = "SELECT 1 FROM frete WHERE id_remetente=? OR id_destinatario=? LIMIT 1";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idCliente);
            ps.setLong(2, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Cliente> listar(String filtro, int pagina, int tamanhoPagina) throws SQLException {
        String where = (filtro == null || filtro.trim().isEmpty())
                ? ""
                : " WHERE LOWER(razao_social) LIKE ? OR LOWER(nome_fantasia) LIKE ? ";
        String sql = SQL_LISTAGEM + where + " ORDER BY razao_social LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (!where.isEmpty()) {
                String like = "%" + filtro.toLowerCase() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            ps.setInt(idx++, tamanhoPagina);
            ps.setInt(idx, (pagina - 1) * tamanhoPagina);

            try (ResultSet rs = ps.executeQuery()) {
                List<Cliente> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapearResumo(rs));
                return lista;
            }
        }
    }

    public int contar(String filtro) throws SQLException {
        String where = (filtro == null || filtro.trim().isEmpty())
                ? ""
                : " WHERE LOWER(razao_social) LIKE ? OR LOWER(nome_fantasia) LIKE ? ";
        String sql = "SELECT COUNT(*) FROM cliente" + where;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!where.isEmpty()) {
                String like = "%" + filtro.toLowerCase() + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public List<Cliente> listarAtivos() throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_LISTAGEM + " WHERE status='ATIVO' ORDER BY razao_social");
             ResultSet rs = ps.executeQuery()) {
            List<Cliente> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapearResumo(rs));
            return lista;
        }
    }

    public int contarPorStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cliente WHERE status = ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int contarPorTipo(TipoCliente tipo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cliente WHERE tipo = ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int contarPorTipoDocumento(String tipoDocumento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cliente WHERE tipo_documento = ?";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipoDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private void preencher(PreparedStatement ps, Cliente c) throws SQLException {
        ps.setString(1, c.getRazaoSocial());
        ps.setString(2, c.getNomeFantasia());
        ps.setString(3, c.getTipoDocumento());
        ps.setString(4, c.getDocumento());
        ps.setString(5, c.getInscricaoEstadual());
        ps.setString(6, c.getTipo().name());
        ps.setString(7, c.getLogradouro());
        ps.setString(8, c.getNumero());
        ps.setString(9, c.getComplemento());
        ps.setString(10, c.getBairro());
        ps.setString(11, c.getMunicipio());
        ps.setString(12, c.getUf());
        ps.setString(13, c.getCep());
        ps.setString(14, c.getTelefone());
        ps.setString(15, c.getEmail());
        ps.setString(16, c.getStatus());
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setRazaoSocial(rs.getString("razao_social"));
        c.setNomeFantasia(rs.getString("nome_fantasia"));
        c.setTipoDocumento(rs.getString("tipo_documento"));
        c.setDocumento(rs.getString("documento"));
        c.setInscricaoEstadual(rs.getString("inscricao_estadual"));
        c.setTipo(TipoCliente.valueOf(rs.getString("tipo")));
        c.setLogradouro(rs.getString("logradouro"));
        c.setNumero(rs.getString("numero"));
        c.setComplemento(rs.getString("complemento"));
        c.setBairro(rs.getString("bairro"));
        c.setMunicipio(rs.getString("municipio"));
        c.setUf(rs.getString("uf"));
        c.setCep(rs.getString("cep"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setStatus(rs.getString("status"));
        return c;
    }

    private Cliente mapearResumo(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setRazaoSocial(rs.getString("razao_social"));
        c.setNomeFantasia(rs.getString("nome_fantasia"));
        c.setTipoDocumento(rs.getString("tipo_documento"));
        c.setDocumento(rs.getString("documento"));
        c.setTipo(TipoCliente.valueOf(rs.getString("tipo")));
        c.setMunicipio(rs.getString("municipio"));
        c.setUf(rs.getString("uf"));
        c.setStatus(rs.getString("status"));
        return c;
    }
}