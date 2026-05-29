package br.com.nextlog.manutencao.orcamento;

import br.com.nextlog.exception.CadastroException;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.manutencao.ManutencaoDAO;
import br.com.nextlog.util.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrcamentoBO {

    private static final Logger LOG = Logger.getLogger(OrcamentoBO.class.getName());
    private final OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
    private final ManutencaoDAO manutencaoDAO = new ManutencaoDAO();

    public Long registrarOrcamento(OrcamentoManutencao o) {
        Connection conn = null;
        try {
            if (manutencaoDAO.buscarPorId(o.getIdManutencao()) == null) {
                throw new CadastroException("Manutenção não encontrada.");
            }
            
            o.setNumero(gerarNumero());
            BigDecimal total = BigDecimal.ZERO;
            if (o.getItens() != null) {
                for (OrcamentoManutencaoItem item : o.getItens()) {
                    total = total.add(item.getQuantidade().multiply(item.getPrecoUnitario()));
                }
            }
            o.setValorTotal(total);
            
            conn = ConnectionPool.getConnection();
            return orcamentoDAO.salvarOrcamento(conn, o);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao registrar orçamento", e);
            throw new NegocioException("Erro ao registrar orçamento.");
        } finally {
            if (conn != null) {
                try { conn.close(); }
                catch (SQLException e) { LOG.log(Level.WARNING, "Erro ao fechar conexão", e); }
            }
        }
    }

    public List<OrcamentoManutencao> buscarPorManutencao(Long idManutencao) {
        try {
            return orcamentoDAO.buscarPorManutencao(idManutencao);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar orçamentos", e);
            throw new NegocioException("Erro ao buscar orçamentos.");
        }
    }

    public List<OrcamentoManutencaoItem> buscarItens(Long idManutencao) {
        try {
            return orcamentoDAO.buscarItens(idManutencao);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar itens de orçamento", e);
            throw new NegocioException("Erro ao buscar itens de orçamento.");
        }
    }

    public Long salvarItem(OrcamentoManutencaoItem item) {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            return orcamentoDAO.salvarItem(conn, item);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao salvar item de orçamento", e);
            throw new NegocioException("Erro ao salvar item de orçamento.");
        } finally {
            if (conn != null) {
                try { conn.close(); }
                catch (SQLException e) { LOG.log(Level.WARNING, "Erro ao fechar conexão", e); }
            }
        }
    }

    public void excluir(Long id) {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            orcamentoDAO.deletarOrcamento(conn, id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao excluir orçamento", e);
            throw new NegocioException("Erro ao excluir orçamento.");
        } finally {
            if (conn != null) {
                try { conn.close(); }
                catch (SQLException e) { LOG.log(Level.WARNING, "Erro ao fechar conexão", e); }
            }
        }
    }

    public void excluirItem(Long id) {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            orcamentoDAO.deletarItem(conn, id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao excluir item de orçamento", e);
            throw new NegocioException("Erro ao excluir item de orçamento.");
        } finally {
            if (conn != null) {
                try { conn.close(); }
                catch (SQLException e) { LOG.log(Level.WARNING, "Erro ao fechar conexão", e); }
            }
        }
    }

    private String gerarNumero() {
        int ano = LocalDate.now().getYear();
        String sequencia = String.format("%05d", (int)(Math.random() * 99999) + 1);
        return "ORC-" + ano + "-" + sequencia;
    }
}