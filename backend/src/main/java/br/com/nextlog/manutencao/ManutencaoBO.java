package br.com.nextlog.manutencao;

import br.com.nextlog.exception.CadastroException;
import br.com.nextlog.exception.NegocioException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManutencaoBO {

    private static final Logger LOG = Logger.getLogger(ManutencaoBO.class.getName());
    private final ManutencaoDAO manutencaoDAO = new ManutencaoDAO();

    public Long salvar(ManutencaoVeiculo m) {
        validar(m);
        try {
            if (m.getId() == null) return manutencaoDAO.inserir(m);
            manutencaoDAO.atualizar(m);
            return m.getId();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao salvar manutenção", e);
            throw new NegocioException("Erro ao salvar manutenção.");
        }
    }

    public void excluir(Long id) {
        try {
            manutencaoDAO.excluir(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao excluir manutenção", e);
            throw new NegocioException("Erro ao excluir manutenção.");
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

    public int contar(String filtro) {
        try {
            return manutencaoDAO.contar(filtro);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar manutenções", e);
            throw new NegocioException("Erro ao contar manutenções.");
        }
    }

    private void validar(ManutencaoVeiculo m) {
        if (m == null) throw new CadastroException("Manutenção é obrigatória.");
        if (m.getIdVeiculo() == null) throw new CadastroException("Veículo é obrigatório.");
        if (m.getTipo() == null) throw new CadastroException("Tipo de manutenção é obrigatório.");
        if (m.getDescricao() == null || m.getDescricao().trim().isEmpty())
            throw new CadastroException("Descrição é obrigatória.");
        if (m.getDataInicio() == null)
            throw new CadastroException("Data de início é obrigatória.");
        if (m.getDataFim() != null && m.getDataFim().isBefore(m.getDataInicio()))
            throw new CadastroException("Data fim não pode ser anterior à data de início.");
        if (m.getCusto() != null && m.getCusto().compareTo(BigDecimal.ZERO) < 0)
            throw new CadastroException("Custo não pode ser negativo.");
    }
}