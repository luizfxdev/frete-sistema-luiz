package br.com.nextlog.candidatura;

import br.com.nextlog.exception.CadastroException;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.CpfValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CandidaturaBO {

    private static final Logger LOG = Logger.getLogger(CandidaturaBO.class.getName());
    private final CandidaturaDAO candidaturaDAO = new CandidaturaDAO();

    public Long registrar(CandidaturaMotorista c) {
        if (!CpfValidator.isValido(c.getCpf())) {
            throw new CadastroException("CPF inválido.");
        }
        if (c.getCnhValidade() == null || c.getCnhValidade().isBefore(LocalDate.now())) {
            throw new CadastroException("CNH vencida ou não informada.");
        }
        try {
            c.setCpf(CpfValidator.limpar(c.getCpf()));
            return candidaturaDAO.inserir(c);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao registrar candidatura", e);
            throw new NegocioException("Erro ao registrar candidatura.");
        }
    }

    public List<CandidaturaMotorista> listar() {
        try {
            return candidaturaDAO.listar();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar candidaturas", e);
            throw new NegocioException("Erro ao listar candidaturas.");
        }
    }

    public void aprovar(Long id) {
        try {
            if (candidaturaDAO.buscarPorId(id) == null) {
                throw new CadastroException("Candidatura não encontrada.");
            }
            candidaturaDAO.atualizarStatus(id, "APROVADA");
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao aprovar candidatura", e);
            throw new NegocioException("Erro ao aprovar candidatura.");
        }
    }

    public void recusar(Long id) {
        try {
            if (candidaturaDAO.buscarPorId(id) == null) {
                throw new CadastroException("Candidatura não encontrada.");
            }
            candidaturaDAO.atualizarStatus(id, "RECUSADA");
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao recusar candidatura", e);
            throw new NegocioException("Erro ao recusar candidatura.");
        }
    }
}