package br.com.nextlog.frete;

import br.com.nextlog.enums.StatusFrete;
import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FreteDAO {

    private static final String SELECT_BASE =
            "SELECT f.*, " +
            "  cr.razao_social AS remetente_razao, " +
            "  cd.razao_social AS destinatario_razao, " +
            "  m.nome          AS motorista_nome, " +
            "  v.placa         AS veiculo_placa " +
            "FROM frete f " +
            "JOIN cliente cr   ON cr.id = f.id_remetente " +
            "JOIN cliente cd   ON cd.id = f.id_destinatario " +
            "JOIN motorista m  ON m.id  = f.id_motorista " +
            "JOIN veiculo v    ON v.id  = f.id_veiculo ";

    public Long inserir(Connection conn, Frete f) throws SQLException {
        String sql = "INSERT INTO frete (numero, id_remetente, id_destinatario, id_motorista, id_veiculo, " +
                "municipio_origem, uf_origem, municipio_destino, uf_destino, descricao_carga, peso_kg, volumes, " +
                "valor_frete, aliquota_icms, valor_icms, valor_total, status, data_emissao, data_previsao_entrega) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getNumero());
            ps.setLong(2, f.getIdRemetente());
            ps.setLong(3, f.getIdDestinatario());
            ps.setLong(4, f.getIdMotorista());
            ps.setLong(5, f.getIdVeiculo());
            ps.setString(6, f.getMunicipioOrigem());
            ps.setString(7, f.getUfOrigem());
            ps.setString(8, f.getMunicipioDestino());
            ps.setString(9, f.getUfDestino());
            ps.setString(10, f.getDescricaoCarga());
            ps.setBigDecimal(11, f.getPesoKg());
            ps.setInt(12, f.getVolumes());
            ps.setBigDecimal(13, f.getValorFrete());
            ps.setBigDecimal(14, f.getAliquotaIcms());
            ps.setBigDecimal(15, f.getValorIcms());
            ps.setBigDecimal(16, f.getValorTotal());
            ps.setString(17, f.getStatus().name());
            ps.setDate(18, Date.valueOf(f.getDataEmissao()));
            ps.setDate(19, Date.valueOf(f.getDataPrevisaoEntrega()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return null;
    }

    public void atualizarStatus(Connection conn, Long id, StatusFrete status) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE frete SET status=? WHERE id=?")) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public void atualizarDataSaida(Connection conn, Long id, Timestamp data) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE frete SET data_saida=? WHERE id=?")) {
            ps.setTimestamp(1, data);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public void atualizarDataEntrega(Connection conn, Long id, Timestamp data) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE frete SET data_entrega=? WHERE id=?")) {
            ps.setTimestamp(1, data);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public Frete buscarPorId(Long id) throws SQLException {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BASE + " WHERE f.id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public Frete buscarPorIdParaTransacao(Connection conn, Long id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM frete WHERE id=? FOR UPDATE")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearSimples(rs) : null;
            }
        }
    }

    public List<Frete> listar(String filtro, String statusFiltro, int pagina, int tamanhoPagina) throws SQLException {
        StringBuilder where = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (filtro != null && !filtro.trim().isEmpty()) {
            where.append(" WHERE (UPPER(f.numero) LIKE ? OR LOWER(cr.razao_social) LIKE ? OR LOWER(cd.razao_social) LIKE ?) ");
            params.add("%" + filtro.toUpperCase() + "%");
            params.add("%" + filtro.toLowerCase() + "%");
            params.add("%" + filtro.toLowerCase() + "%");
        }
        if (statusFiltro != null && !statusFiltro.trim().isEmpty()) {
            where.append(where.length() == 0 ? " WHERE " : " AND ").append("f.status=? ");
            params.add(statusFiltro);
        }

        String sql = SELECT_BASE + where + " ORDER BY f.data_emissao DESC, f.id DESC LIMIT ? OFFSET ?";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            for (Object p : params) ps.setObject(idx++, p);
            ps.setInt(idx++, tamanhoPagina);
            ps.setInt(idx, (pagina - 1) * tamanhoPagina);

            try (ResultSet rs = ps.executeQuery()) {
                List<Frete> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    public int contar(String filtro, String statusFiltro) throws SQLException {
        StringBuilder where = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (filtro != null && !filtro.trim().isEmpty()) {
            where.append(" WHERE (UPPER(f.numero) LIKE ? OR LOWER(cr.razao_social) LIKE ? OR LOWER(cd.razao_social) LIKE ?) ");
            params.add("%" + filtro.toUpperCase() + "%");
            params.add("%" + filtro.toLowerCase() + "%");
            params.add("%" + filtro.toLowerCase() + "%");
        }
        if (statusFiltro != null && !statusFiltro.trim().isEmpty()) {
            where.append(where.length() == 0 ? " WHERE " : " AND ").append("f.status=? ");
            params.add(statusFiltro);
        }

        String sql = "SELECT COUNT(*) FROM frete f " +
                "JOIN cliente cr ON cr.id=f.id_remetente JOIN cliente cd ON cd.id=f.id_destinatario " + where;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            for (Object p : params) ps.setObject(idx++, p);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public List<Frete> listarEmAberto() throws SQLException {
        String sql = SELECT_BASE +
                " WHERE f.status IN ('EMITIDO','SAIDA_CONFIRMADA','EM_TRANSITO') " +
                " ORDER BY f.data_previsao_entrega";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Frete> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        }
    }

    public List<Frete> listarPorMotoristaEData(Long idMotorista, java.time.LocalDate data) throws SQLException {
        String sql = SELECT_BASE + " WHERE f.id_motorista=? AND f.data_emissao=? ORDER BY f.numero";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idMotorista);
            ps.setDate(2, Date.valueOf(data));
            try (ResultSet rs = ps.executeQuery()) {
                List<Frete> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        }
    }

    private Frete mapear(ResultSet rs) throws SQLException {
        Frete f = mapearSimples(rs);
        f.setRemetenteRazaoSocial(rs.getString("remetente_razao"));
        f.setDestinatarioRazaoSocial(rs.getString("destinatario_razao"));
        f.setMotoristaNome(rs.getString("motorista_nome"));
        f.setVeiculoPlaca(rs.getString("veiculo_placa"));
        return f;
    }

    private Frete mapearSimples(ResultSet rs) throws SQLException {
        Frete f = new Frete();
        f.setId(rs.getLong("id"));
        f.setNumero(rs.getString("numero"));
        f.setIdRemetente(rs.getLong("id_remetente"));
        f.setIdDestinatario(rs.getLong("id_destinatario"));
        f.setIdMotorista(rs.getLong("id_motorista"));
        f.setIdVeiculo(rs.getLong("id_veiculo"));
        f.setMunicipioOrigem(rs.getString("municipio_origem"));
        f.setUfOrigem(rs.getString("uf_origem"));
        f.setMunicipioDestino(rs.getString("municipio_destino"));
        f.setUfDestino(rs.getString("uf_destino"));
        f.setDescricaoCarga(rs.getString("descricao_carga"));
        f.setPesoKg(rs.getBigDecimal("peso_kg"));
        f.setVolumes(rs.getInt("volumes"));
        f.setValorFrete(rs.getBigDecimal("valor_frete"));
        f.setAliquotaIcms(rs.getBigDecimal("aliquota_icms"));
        f.setValorIcms(rs.getBigDecimal("valor_icms"));
        f.setValorTotal(rs.getBigDecimal("valor_total"));
        f.setStatus(StatusFrete.valueOf(rs.getString("status")));
        f.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
        f.setDataPrevisaoEntrega(rs.getDate("data_previsao_entrega").toLocalDate());
        Timestamp ds = rs.getTimestamp("data_saida");
        if (ds != null) f.setDataSaida(ds.toLocalDateTime());
        Timestamp de = rs.getTimestamp("data_entrega");
        if (de != null) f.setDataEntrega(de.toLocalDateTime());
        return f;
    }
}