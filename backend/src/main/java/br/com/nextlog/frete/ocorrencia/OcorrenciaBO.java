package br.com.nextlog.frete.ocorrencia;

import br.com.nextlog.enums.StatusFrete;
import br.com.nextlog.enums.StatusVeiculo;
import br.com.nextlog.enums.TipoOcorrencia;
import br.com.nextlog.cadastro.veiculo.VeiculoDAO;
import br.com.nextlog.exception.FreteException;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.frete.Frete;
import br.com.nextlog.frete.FreteDAO;
import br.com.nextlog.util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OcorrenciaBO {

    private static final Logger LOG = Logger.getLogger(OcorrenciaBO.class.getName());

    private final OcorrenciaDAO ocorrenciaDAO = new OcorrenciaDAO();
    private final FreteDAO freteDAO = new FreteDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    public Long registrarOcorrencia(OcorrenciaFrete o) {
        validar(o);

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);

            Frete f = freteDAO.buscarPorIdParaTransacao(conn, o.getIdFrete());
            if (f == null) throw new FreteException("Frete não encontrado.");

            if (f.getStatus() == StatusFrete.ENTREGUE
                    || f.getStatus() == StatusFrete.NAO_ENTREGUE
                    || f.getStatus() == StatusFrete.CANCELADO) {
                throw new FreteException("Não é possível registrar ocorrência em frete finalizado ou cancelado.");
            }

            LocalDateTime ultima = ocorrenciaDAO.buscarDataHoraUltimaOcorrencia(conn, o.getIdFrete());
            if (ultima != null && o.getDataHora().isBefore(ultima)) {
                throw new FreteException("Data/hora da ocorrência não pode ser anterior à última registrada.");
            }

            if (o.getTipo() == TipoOcorrencia.SAIDA_PATIO && f.getStatus() == StatusFrete.EMITIDO) {
                freteDAO.atualizarStatus(conn, f.getId(), StatusFrete.SAIDA_CONFIRMADA);
                freteDAO.atualizarDataSaida(conn, f.getId(), Timestamp.valueOf(o.getDataHora()));
                veiculoDAO.atualizarStatus(f.getIdVeiculo(), StatusVeiculo.EM_VIAGEM, conn);
            }

            if (o.getTipo() == TipoOcorrencia.EM_ROTA && f.getStatus() == StatusFrete.SAIDA_CONFIRMADA) {
                freteDAO.atualizarStatus(conn, f.getId(), StatusFrete.EM_TRANSITO);
            }

            if (o.getTipo() == TipoOcorrencia.ENTREGA_REALIZADA) {
                freteDAO.atualizarStatus(conn, f.getId(), StatusFrete.ENTREGUE);
                freteDAO.atualizarDataEntrega(conn, f.getId(), Timestamp.valueOf(o.getDataHora()));
                veiculoDAO.atualizarStatus(f.getIdVeiculo(), StatusVeiculo.DISPONIVEL, conn);
            }

            Long id = ocorrenciaDAO.inserir(conn, o);
            conn.commit();
            return id;
        } catch (NegocioException ne) {
            rollback(conn);
            throw ne;
        } catch (SQLException e) {
            rollback(conn);
            LOG.log(Level.SEVERE, "Erro ao registrar ocorrência", e);
            throw new FreteException("Erro ao registrar ocorrência.");
        } finally {
            close(conn);
        }
    }

    public List<OcorrenciaFrete> listarPorFrete(Long idFrete) {
        try {
            return ocorrenciaDAO.listarPorFrete(idFrete);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar ocorrências", e);
            throw new NegocioException("Erro ao listar ocorrências.");
        }
    }

    private void validar(OcorrenciaFrete o) {
        if (o == null)               throw new FreteException("Ocorrência é obrigatória.");
        if (o.getIdFrete() == null)  throw new FreteException("Frete é obrigatório.");
        if (o.getTipo() == null)     throw new FreteException("Tipo da ocorrência é obrigatório.");
        if (o.getDataHora() == null) throw new FreteException("Data/hora da ocorrência é obrigatória.");
        if (o.getMunicipio() == null || o.getMunicipio().trim().isEmpty())
            throw new FreteException("Município da ocorrência é obrigatório.");
        if (o.getUf() == null || o.getUf().length() != 2)
            throw new FreteException("UF da ocorrência inválida.");

        boolean exigeDescricao = o.getTipo() == TipoOcorrencia.AVARIA
                || o.getTipo() == TipoOcorrencia.EXTRAVIO
                || o.getTipo() == TipoOcorrencia.OUTROS;
        if (exigeDescricao && (o.getDescricao() == null || o.getDescricao().trim().isEmpty()))
            throw new FreteException("Descrição é obrigatória para o tipo de ocorrência selecionado.");

        if (o.getTipo() == TipoOcorrencia.ENTREGA_REALIZADA) {
            if (o.getNomeRecebedor() == null || o.getNomeRecebedor().trim().isEmpty())
                throw new FreteException("Nome do recebedor é obrigatório para entrega realizada.");
            if (o.getDocumentoRecebedor() == null || o.getDocumentoRecebedor().trim().isEmpty())
                throw new FreteException("Documento do recebedor é obrigatório para entrega realizada.");
        }
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