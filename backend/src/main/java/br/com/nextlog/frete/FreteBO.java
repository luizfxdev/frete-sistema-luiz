package br.com.nextlog.frete;

import br.com.nextlog.cadastro.motorista.Motorista;
import br.com.nextlog.cadastro.motorista.MotoristaDAO;
import br.com.nextlog.cadastro.veiculo.Veiculo;
import br.com.nextlog.cadastro.veiculo.VeiculoDAO;
import br.com.nextlog.enums.StatusFrete;
import br.com.nextlog.enums.StatusMotorista;
import br.com.nextlog.enums.StatusVeiculo;
import br.com.nextlog.exception.FreteException;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.ConnectionPool;
import br.com.nextlog.util.FreteNumberGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FreteBO {

    private static final Logger LOG = Logger.getLogger(FreteBO.class.getName());

    private final FreteDAO freteDAO = new FreteDAO();
    private final MotoristaDAO motoristaDAO = new MotoristaDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    public Long registrarFrete(Frete frete) {
        validarDadosBasicos(frete);

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            Motorista motorista = motoristaDAO.buscarPorId(frete.getIdMotorista());
            Veiculo veiculo     = veiculoDAO.buscarPorId(frete.getIdVeiculo());

            validarMotoristaParaFrete(motorista, frete);
            validarVeiculoParaFrete(veiculo, frete);

            calcularTotais(frete);
            frete.setStatus(StatusFrete.EMITIDO);
            frete.setNumero(FreteNumberGenerator.gerar(conn));

            Long id = freteDAO.inserir(conn, frete);
            conn.commit();
            return id;
        } catch (NegocioException ne) {
            rollback(conn);
            throw ne;
        } catch (SQLException e) {
            rollback(conn);
            LOG.log(Level.SEVERE, "Erro ao registrar frete", e);
            throw new FreteException("Erro ao registrar frete. Tente novamente.");
        } finally {
            close(conn);
        }
    }

    public void confirmarSaida(Long idFrete) {
        if (idFrete == null) throw new FreteException("ID do frete é obrigatório.");

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            Frete f = freteDAO.buscarPorIdParaTransacao(conn, idFrete);
            if (f == null) throw new FreteException("Frete não encontrado.");
            if (f.getStatus() != StatusFrete.EMITIDO)
                throw new FreteException("Só é possível confirmar saída de frete EMITIDO.");

            freteDAO.atualizarStatus(conn, idFrete, StatusFrete.SAIDA_CONFIRMADA);
            freteDAO.atualizarDataSaida(conn, idFrete, Timestamp.valueOf(LocalDateTime.now()));
            veiculoDAO.atualizarStatus(f.getIdVeiculo(), StatusVeiculo.EM_VIAGEM, conn);

            conn.commit();
        } catch (NegocioException ne) {
            rollback(conn);
            throw ne;
        } catch (SQLException e) {
            rollback(conn);
            LOG.log(Level.SEVERE, "Erro ao confirmar saída", e);
            throw new FreteException("Erro ao confirmar saída do frete.");
        } finally {
            close(conn);
        }
    }

    public void marcarEmTransito(Long idFrete) {
        if (idFrete == null) throw new FreteException("ID do frete é obrigatório.");

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            Frete f = freteDAO.buscarPorIdParaTransacao(conn, idFrete);
            if (f == null) throw new FreteException("Frete não encontrado.");
            if (f.getStatus() != StatusFrete.SAIDA_CONFIRMADA)
                throw new FreteException("Só é possível marcar em trânsito após confirmação de saída.");

            freteDAO.atualizarStatus(conn, idFrete, StatusFrete.EM_TRANSITO);
            conn.commit();
        } catch (NegocioException ne) {
            rollback(conn);
            throw ne;
        } catch (SQLException e) {
            rollback(conn);
            LOG.log(Level.SEVERE, "Erro ao marcar em trânsito", e);
            throw new FreteException("Erro ao atualizar status do frete.");
        } finally {
            close(conn);
        }
    }

    public void registrarEntrega(Long idFrete) {
        if (idFrete == null) throw new FreteException("ID do frete é obrigatório.");

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            Frete f = freteDAO.buscarPorIdParaTransacao(conn, idFrete);
            if (f == null) throw new FreteException("Frete não encontrado.");
            if (f.getStatus() != StatusFrete.EM_TRANSITO)
                throw new FreteException("Só é possível registrar entrega de frete EM TRÂNSITO.");

            freteDAO.atualizarStatus(conn, idFrete, StatusFrete.ENTREGUE);
            freteDAO.atualizarDataEntrega(conn, idFrete, Timestamp.valueOf(LocalDateTime.now()));
            veiculoDAO.atualizarStatus(f.getIdVeiculo(), StatusVeiculo.DISPONIVEL, conn);

            conn.commit();
        } catch (NegocioException ne) {
            rollback(conn);
            throw ne;
        } catch (SQLException e) {
            rollback(conn);
            LOG.log(Level.SEVERE, "Erro ao registrar entrega", e);
            throw new FreteException("Erro ao registrar entrega.");
        } finally {
            close(conn);
        }
    }

    public void registrarNaoEntrega(Long idFrete) {
        if (idFrete == null) throw new FreteException("ID do frete é obrigatório.");

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            Frete f = freteDAO.buscarPorIdParaTransacao(conn, idFrete);
            if (f == null) throw new FreteException("Frete não encontrado.");
            if (f.getStatus() != StatusFrete.EM_TRANSITO)
                throw new FreteException("Só é possível registrar não entrega em frete EM TRÂNSITO.");

            freteDAO.atualizarStatus(conn, idFrete, StatusFrete.NAO_ENTREGUE);
            veiculoDAO.atualizarStatus(f.getIdVeiculo(), StatusVeiculo.DISPONIVEL, conn);

            conn.commit();
        } catch (NegocioException ne) {
            rollback(conn);
            throw ne;
        } catch (SQLException e) {
            rollback(conn);
            LOG.log(Level.SEVERE, "Erro ao registrar não entrega", e);
            throw new FreteException("Erro ao registrar não entrega.");
        } finally {
            close(conn);
        }
    }

    public void cancelar(Long idFrete) {
        if (idFrete == null) throw new FreteException("ID do frete é obrigatório.");

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            Frete f = freteDAO.buscarPorIdParaTransacao(conn, idFrete);
            if (f == null) throw new FreteException("Frete não encontrado.");

            if (f.getStatus() == StatusFrete.ENTREGUE
                    || f.getStatus() == StatusFrete.NAO_ENTREGUE
                    || f.getStatus() == StatusFrete.CANCELADO) {
                throw new FreteException("Não é possível cancelar um frete já finalizado.");
            }
            if (f.getStatus() != StatusFrete.EMITIDO) {
                throw new FreteException("Só é possível cancelar um frete antes da saída ser confirmada.");
            }

            freteDAO.atualizarStatus(conn, idFrete, StatusFrete.CANCELADO);
            conn.commit();
        } catch (NegocioException ne) {
            rollback(conn);
            throw ne;
        } catch (SQLException e) {
            rollback(conn);
            LOG.log(Level.SEVERE, "Erro ao cancelar frete", e);
            throw new FreteException("Erro ao cancelar frete.");
        } finally {
            close(conn);
        }
    }

    public Frete buscarPorId(Long id) {
        try {
            return freteDAO.buscarPorId(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar frete", e);
            throw new NegocioException("Erro ao buscar frete.");
        }
    }

    public List<Frete> listar(String filtro, String statusFiltro, int pagina, int tamanhoPagina) {
        try {
            return freteDAO.listar(filtro, statusFiltro, pagina, Math.max(tamanhoPagina, 10));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar fretes", e);
            throw new NegocioException("Erro ao listar fretes.");
        }
    }

    public int contar(String filtro, String statusFiltro) {
        try {
            return freteDAO.contar(filtro, statusFiltro);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar fretes", e);
            throw new NegocioException("Erro ao contar fretes.");
        }
    }

    public List<Frete> listarEmAberto() {
        try {
            return freteDAO.listarEmAberto();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar fretes em aberto", e);
            throw new NegocioException("Erro ao listar fretes em aberto.");
        }
    }

    public List<Frete> listarRomaneio(Long idMotorista, LocalDate data) {
        try {
            return freteDAO.listarPorMotoristaEData(idMotorista, data);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar romaneio", e);
            throw new NegocioException("Erro ao gerar romaneio.");
        }
    }

    public String resumoMensalJson(int meses) {
        try {
            List<Map<String, Object>> dados = freteDAO.contarMensalUltimosMeses(meses);

            StringBuilder mesesJson      = new StringBuilder("[");
            StringBuilder concluidosJson = new StringBuilder("[");
            StringBuilder canceladosJson = new StringBuilder("[");

            for (int i = 0; i < dados.size(); i++) {
                Map<String, Object> d = dados.get(i);
                String sep = i < dados.size() - 1 ? "," : "";
                mesesJson.append("\"").append(d.get("mes")).append("\"").append(sep);
                concluidosJson.append(d.get("concluidos")).append(sep);
                canceladosJson.append(d.get("cancelados")).append(sep);
            }

            mesesJson.append("]");
            concluidosJson.append("]");
            canceladosJson.append("]");

            return mesesJson + "|" + concluidosJson + "|" + canceladosJson;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao gerar resumo mensal", e);
            throw new NegocioException("Erro ao gerar resumo mensal.");
        }
    }

    private void validarDadosBasicos(Frete f) {
        if (f == null) throw new FreteException("Frete é obrigatório.");
        if (f.getIdRemetente() == null)    throw new FreteException("Remetente é obrigatório.");
        if (f.getIdDestinatario() == null) throw new FreteException("Destinatário é obrigatório.");
        if (f.getIdMotorista() == null)    throw new FreteException("Motorista é obrigatório.");
        if (f.getIdVeiculo() == null)      throw new FreteException("Veículo é obrigatório.");
        if (f.getMunicipioOrigem() == null || f.getUfOrigem() == null)
            throw new FreteException("Origem é obrigatória.");
        if (f.getMunicipioDestino() == null || f.getUfDestino() == null)
            throw new FreteException("Destino é obrigatório.");
        if (f.getDescricaoCarga() == null || f.getDescricaoCarga().trim().isEmpty())
            throw new FreteException("Descrição da carga é obrigatória.");
        if (f.getPesoKg() == null || f.getPesoKg().compareTo(BigDecimal.ZERO) <= 0)
            throw new FreteException("Peso da carga deve ser maior que zero.");
        if (f.getVolumes() == null || f.getVolumes() <= 0)
            throw new FreteException("Quantidade de volumes deve ser maior que zero.");
        if (f.getValorFrete() == null || f.getValorFrete().compareTo(BigDecimal.ZERO) <= 0)
            throw new FreteException("Valor do frete deve ser maior que zero.");
        if (f.getAliquotaIcms() == null || f.getAliquotaIcms().compareTo(BigDecimal.ZERO) < 0)
            throw new FreteException("Alíquota de ICMS inválida.");
        if (f.getDataEmissao() == null)
            throw new FreteException("Data de emissão é obrigatória.");
        if (f.getDataPrevisaoEntrega() == null)
            throw new FreteException("Data prevista de entrega é obrigatória.");
        if (!f.getDataPrevisaoEntrega().isAfter(f.getDataEmissao()))
            throw new FreteException("A data prevista de entrega deve ser posterior à data de emissão.");
    }

    private void validarMotoristaParaFrete(Motorista m, Frete f) throws SQLException {
        if (m == null) throw new FreteException("Motorista não encontrado.");
        if (m.getStatus() != StatusMotorista.ATIVO)
            throw new FreteException("Motorista não está ativo.");
        if (m.getCnhValidade() == null || m.getCnhValidade().isBefore(f.getDataEmissao()))
            throw new FreteException("CNH do motorista vencida na data de emissão do frete.");
        if (motoristaDAO.possuiFreteEmAndamento(m.getId()))
            throw new FreteException("Motorista já possui frete em andamento.");
    }

    private void validarVeiculoParaFrete(Veiculo v, Frete f) {
        if (v == null) throw new FreteException("Veículo não encontrado.");
        if (v.getStatus() != StatusVeiculo.DISPONIVEL)
            throw new FreteException("Veículo não está disponível.");
        if (f.getPesoKg().compareTo(v.getCapacidadeKg()) > 0)
            throw new FreteException("Peso da carga excede a capacidade do veículo.");
    }

    private void calcularTotais(Frete f) {
        BigDecimal aliquota = f.getAliquotaIcms().divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
        BigDecimal icms     = f.getValorFrete().multiply(aliquota).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total    = f.getValorFrete().add(icms).setScale(2, RoundingMode.HALF_UP);
        f.setValorIcms(icms);
        f.setValorTotal(total);
    }

    private void rollback(Connection conn) {
        if (conn == null) return;
        try { conn.rollback(); } catch (SQLException ex) { LOG.log(Level.WARNING, "Falha no rollback", ex); }
    }

    private void close(Connection conn) {
        if (conn == null) return;
        try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { LOG.log(Level.WARNING, "Falha ao fechar conexão", ex); }
    }
}