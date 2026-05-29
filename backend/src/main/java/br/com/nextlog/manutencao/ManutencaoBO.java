package br.com.nextlog.manutencao;

import br.com.nextlog.cadastro.veiculo.VeiculoDAO;
import br.com.nextlog.enums.StatusManutencao;
import br.com.nextlog.enums.StatusVeiculo;
import br.com.nextlog.enums.TipoManutencao;
import br.com.nextlog.exception.CadastroException;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.manutencao.orcamento.OrcamentoDAO;
import br.com.nextlog.manutencao.orcamento.OrcamentoManutencaoItem;
import br.com.nextlog.util.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManutencaoBO {

    private static final Logger LOG = Logger.getLogger(ManutencaoBO.class.getName());
    private final ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final OrcamentoDAO orcamentoDAO = new OrcamentoDAO();

    public Long registrarManutencao(ManutencaoVeiculo m) {
        validarManutencao(m);
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            try {
                m.setStatusManutencao(StatusManutencao.EM_ANDAMENTO);
                Long idManutencao = manutencaoDAO.inserir(conn, m);
                veiculoDAO.atualizarStatus(m.getIdVeiculo(), StatusVeiculo.EM_MANUTENCAO, conn);
                conn.commit();
                return idManutencao;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao registrar manutenção", e);
            throw new NegocioException("Erro ao registrar manutenção.");
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); }
                catch (SQLException e) { LOG.log(Level.WARNING, "Erro ao fechar conexão", e); }
            }
        }
    }

    public void concluirManutencao(Long idManutencao, ManutencaoVeiculo m) {
        validarConclusao(m);
        Connection conn = null;
        try {
            ManutencaoVeiculo manutencao = manutencaoDAO.buscarPorId(idManutencao);
            if (manutencao == null) {
                throw new CadastroException("Manutenção não encontrada.");
            }
            if (manutencao.getStatusManutencao() != StatusManutencao.EM_ANDAMENTO) {
                throw new CadastroException("Apenas manutenções em andamento podem ser concluídas.");
            }

            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            try {
                m.setId(idManutencao);
                m.setStatusManutencao(StatusManutencao.CONCLUIDA);
                manutencaoDAO.atualizar(conn, m);
                veiculoDAO.atualizarStatus(manutencao.getIdVeiculo(), StatusVeiculo.DISPONIVEL, conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao concluir manutenção", e);
            throw new NegocioException("Erro ao concluir manutenção.");
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); }
                catch (SQLException e) { LOG.log(Level.WARNING, "Erro ao fechar conexão", e); }
            }
        }
    }

    public void cancelarManutencao(Long idManutencao) {
        Connection conn = null;
        try {
            ManutencaoVeiculo m = manutencaoDAO.buscarPorId(idManutencao);
            if (m == null) {
                throw new CadastroException("Manutenção não encontrada.");
            }
            if (m.getStatusManutencao() == StatusManutencao.CONCLUIDA) {
                throw new CadastroException("Manutenção concluída não pode ser cancelada.");
            }

            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            try {
                manutencaoDAO.cancelar(conn, idManutencao);
                veiculoDAO.atualizarStatus(m.getIdVeiculo(), StatusVeiculo.DISPONIVEL, conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao cancelar manutenção", e);
            throw new NegocioException("Erro ao cancelar manutenção.");
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); }
                catch (SQLException e) { LOG.log(Level.WARNING, "Erro ao fechar conexão", e); }
            }
        }
    }

    public void liberarManutencao(Long id) throws NegocioException {
        Connection conn = null;
        try {
            ManutencaoVeiculo m = manutencaoDAO.buscarPorId(id);
            if (m == null) {
                throw new CadastroException("Manutenção não encontrada.");
            }

            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            try {
                m.setStatusManutencao(StatusManutencao.CONCLUIDA);
                m.setDataFim(LocalDate.now());
                manutencaoDAO.atualizar(conn, m);
                veiculoDAO.atualizarStatus(m.getIdVeiculo(), StatusVeiculo.DISPONIVEL, conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao liberar manutenção", e);
            throw new NegocioException("Erro ao liberar manutenção.");
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); }
                catch (SQLException e) { LOG.log(Level.WARNING, "Erro ao fechar conexão", e); }
            }
        }
    }

    public List<OrcamentoManutencaoItem> buscarOrcamentoItens(Long idManutencao) throws NegocioException {
        try {
            return orcamentoDAO.buscarItens(idManutencao);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar itens de orçamento", e);
            throw new NegocioException("Erro ao buscar itens de orçamento.");
        }
    }

    public ManutencaoVeiculo buscarPorId(Long id) {
        try {
            return manutencaoDAO.buscarPorId(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar manutenção", e);
            throw new NegocioException("Erro ao buscar manutenção.");
        }
    }

    public List<ManutencaoVeiculo> listar(String filtro, int pagina, int tamanhoPagina) {
        try {
            return manutencaoDAO.listar(filtro, pagina, Math.max(tamanhoPagina, 10));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar manutenções", e);
            throw new NegocioException("Erro ao listar manutenções.");
        }
    }

    public List<ManutencaoVeiculo> listarPorStatus(StatusManutencao status, int pagina, int tamanhoPagina) {
        try {
            return manutencaoDAO.listarPorStatus(status, pagina, Math.max(tamanhoPagina, 10));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar manutenções por status", e);
            throw new NegocioException("Erro ao listar manutenções.");
        }
    }

    public List<ManutencaoVeiculo> listarPorTipo(TipoManutencao tipo, int pagina, int tamanhoPagina) {
        try {
            return manutencaoDAO.listarPorTipo(tipo, pagina, Math.max(tamanhoPagina, 10));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar manutenções por tipo", e);
            throw new NegocioException("Erro ao listar manutenções.");
        }
    }

    public int contar(String filtro) {
        try {
            return manutencaoDAO.contar(filtro);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar manutenções", e);
            throw new NegocioException("Erro ao contar manutenções.");
        }
    }

    public int contarPorStatus(StatusManutencao status) {
        try {
            return manutencaoDAO.contarPorStatus(status);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar manutenções por status", e);
            throw new NegocioException("Erro ao contar manutenções.");
        }
    }

    public int contarPorTipo(TipoManutencao tipo) {
        try {
            return manutencaoDAO.contarPorTipo(tipo);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar manutenções por tipo", e);
            throw new NegocioException("Erro ao contar manutenções.");
        }
    }

    public int contarLiberadasNoMes() {
        try {
            return manutencaoDAO.contarLiberadasNoMes();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar liberadas", e);
            throw new NegocioException("Erro ao contar liberadas.");
        }
    }

    public void validarManutencao(ManutencaoVeiculo m) {
        if (m == null) throw new CadastroException("Manutenção é obrigatória.");
        if (m.getIdVeiculo() == null) throw new CadastroException("Veículo é obrigatório.");
        if (m.getTipo() == null) throw new CadastroException("Tipo de manutenção é obrigatório.");
        if (m.getDataInicio() == null) throw new CadastroException("Data de início é obrigatória.");
        if (m.getKmAtual() == null || m.getKmAtual().compareTo(BigDecimal.ZERO) <= 0)
            throw new CadastroException("KM atual deve ser maior que zero.");
        if (m.getCusto() == null || m.getCusto().compareTo(BigDecimal.ZERO) < 0)
            throw new CadastroException("Custo não pode ser negativo.");
        if (m.getTipo() == TipoManutencao.SINISTRO && (m.getDescricao() == null || m.getDescricao().trim().isEmpty()))
            throw new CadastroException("Descrição é obrigatória para sinistro.");
    }

    private void validarConclusao(ManutencaoVeiculo m) {
        if (m.getDataFim() == null) throw new CadastroException("Data de conclusão é obrigatória.");
        if (m.getDataFim().isBefore(m.getDataInicio()))
            throw new CadastroException("Data de conclusão não pode ser anterior à data de início.");
        if (m.getKmAtual() == null || m.getKmAtual().compareTo(BigDecimal.ZERO) <= 0)
            throw new CadastroException("KM atual deve ser maior que zero.");
    }
}