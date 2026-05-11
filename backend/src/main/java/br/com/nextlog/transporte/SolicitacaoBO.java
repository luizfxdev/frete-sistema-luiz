package br.com.nextlog.transporte;

import br.com.nextlog.cadastro.motorista.Motorista;
import br.com.nextlog.cadastro.motorista.MotoristaDAO;
import br.com.nextlog.exception.NegocioException;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolicitacaoBO {

    private static final Logger LOG = Logger.getLogger(SolicitacaoBO.class.getName());
    private final SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private final MotoristaDAO motoristaDAO = new MotoristaDAO();

    public Long enviar(SolicitacaoTransporte s) {
        try {
            Motorista m = motoristaDAO.buscarPorId(s.getIdMotorista());
            if (m == null || !m.isDisponivel()) {
                throw new NegocioException("Motorista indisponível para receber solicitações.");
            }
            return solicitacaoDAO.inserir(s);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao enviar solicitação", e);
            throw new NegocioException("Erro ao enviar solicitação.");
        }
    }

    public void aceitar(Long id, Long idMotorista) {
        try {
            SolicitacaoTransporte s = solicitacaoDAO.buscarPorId(id);
            if (s == null) throw new NegocioException("Solicitação não encontrada.");
            if (!s.getIdMotorista().equals(idMotorista)) {
                throw new NegocioException("Motorista não é o destinatário desta solicitação.");
            }
            solicitacaoDAO.atualizarStatus(id, "ACEITA", null);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao aceitar solicitação", e);
            throw new NegocioException("Erro ao aceitar solicitação.");
        }
    }

    public void recusar(Long id, Long idMotorista, String motivo) {
        try {
            SolicitacaoTransporte s = solicitacaoDAO.buscarPorId(id);
            if (s == null) throw new NegocioException("Solicitação não encontrada.");
            if (!s.getIdMotorista().equals(idMotorista)) {
                throw new NegocioException("Motorista não é o destinatário desta solicitação.");
            }
            solicitacaoDAO.atualizarStatus(id, "RECUSADA", motivo);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao recusar solicitação", e);
            throw new NegocioException("Erro ao recusar solicitação.");
        }
    }

    public List<SolicitacaoTransporte> listarPorMotorista(Long idMotorista) {
        try {
            return solicitacaoDAO.listarPorMotorista(idMotorista);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar solicitações", e);
            throw new NegocioException("Erro ao listar solicitações.");
        }
    }
}