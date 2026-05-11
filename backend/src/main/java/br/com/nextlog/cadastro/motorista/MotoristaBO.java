package br.com.nextlog.cadastro.motorista;

import br.com.nextlog.enums.StatusMotorista;
import br.com.nextlog.exception.CadastroException;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.CpfValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MotoristaBO {

    private static final Logger LOG = Logger.getLogger(MotoristaBO.class.getName());
    private final MotoristaDAO motoristaDAO = new MotoristaDAO();

    public Long salvar(Motorista m) {
        validar(m);
        try {
            String cpfLimpo = CpfValidator.limpar(m.getCpf());
            m.setCpf(cpfLimpo);

            if (motoristaDAO.existePorCpf(cpfLimpo, m.getId())) {
                throw new CadastroException("Já existe um motorista cadastrado com este CPF.");
            }

            if (m.getStatus() == null) m.setStatus(StatusMotorista.ATIVO);

            if (m.getCep() != null && !m.getCep().trim().isEmpty()) {
                m.setCep(m.getCep().replaceAll("\\D", ""));
            }
            if (m.getUf() != null && !m.getUf().trim().isEmpty()) {
                m.setUf(m.getUf().toUpperCase());
            }

            if (m.getId() != null && m.getStatus() == StatusMotorista.INATIVO
                    && motoristaDAO.possuiFreteAtivo(m.getId())) {
                throw new CadastroException("Não é permitido inativar motorista com frete em andamento.");
            }

            if (m.getId() == null) return motoristaDAO.inserir(m);
            motoristaDAO.atualizar(m);
            return m.getId();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao salvar motorista", e);
            throw new NegocioException("Erro ao salvar motorista. Tente novamente.");
        }
    }

    public void atualizarDisponibilidade(Long idMotorista, boolean disponivel) {
        try {
            if (disponivel && motoristaDAO.possuiFreteEmAndamento(idMotorista)) {
                throw new CadastroException("Motorista possui frete em andamento e não pode ser marcado como disponível.");
            }
            motoristaDAO.atualizarDisponibilidade(idMotorista, disponivel);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao atualizar disponibilidade do motorista", e);
            throw new NegocioException("Erro ao atualizar disponibilidade.");
        }
    }

    public void excluir(Long id) {
        try {
            if (motoristaDAO.possuiFreteAtivo(id)) {
                throw new CadastroException("Motorista possui fretes ativos e não pode ser excluído.");
            }
            motoristaDAO.excluir(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao excluir motorista", e);
            throw new NegocioException("Erro ao excluir motorista.");
        }
    }

    public Motorista buscarPorId(Long id) {
        try {
            return motoristaDAO.buscarPorId(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar motorista", e);
            throw new NegocioException("Erro ao buscar motorista.");
        }
    }

    public List<Motorista> listar(String filtro, int pagina, int tamanhoPagina) {
        try {
            return motoristaDAO.listar(filtro, pagina, Math.max(tamanhoPagina, 10));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar motoristas", e);
            throw new NegocioException("Erro ao listar motoristas.");
        }
    }

    public int contar(String filtro) {
        try {
            return motoristaDAO.contar(filtro);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar motoristas", e);
            throw new NegocioException("Erro ao contar motoristas.");
        }
    }

    public List<Motorista> listarAtivos() {
        try {
            return motoristaDAO.listarAtivos();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar motoristas ativos", e);
            throw new NegocioException("Erro ao listar motoristas ativos.");
        }
    }

    public Map<StatusMotorista, Integer> contarPorStatus() {
        try {
            Map<StatusMotorista, Integer> totais = new EnumMap<>(StatusMotorista.class);
            for (StatusMotorista s : StatusMotorista.values()) {
                totais.put(s, motoristaDAO.contarPorStatus(s));
            }
            return totais;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar motoristas por status", e);
            throw new NegocioException("Erro ao contar motoristas por status.");
        }
    }

    public int contarCnhVencida() {
        try {
            return motoristaDAO.contarCnhVencida();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar motoristas com CNH vencida", e);
            throw new NegocioException("Erro ao contar motoristas com CNH vencida.");
        }
    }

    private void validar(Motorista m) {
        if (m == null) throw new CadastroException("Motorista é obrigatório.");
        if (m.getNome() == null || m.getNome().trim().isEmpty())
            throw new CadastroException("O nome do motorista é obrigatório.");
        if (!CpfValidator.isValido(m.getCpf()))
            throw new CadastroException("O CPF informado é inválido.");
        if (m.getDataNascimento() == null || m.getDataNascimento().isAfter(LocalDate.now().minusYears(18)))
            throw new CadastroException("Motorista deve ter no mínimo 18 anos.");
        if (m.getCnhNumero() == null || m.getCnhNumero().trim().isEmpty())
            throw new CadastroException("O número da CNH é obrigatório.");
        if (m.getCnhCategoria() == null)
            throw new CadastroException("A categoria da CNH é obrigatória.");
        if (m.getCnhValidade() == null)
            throw new CadastroException("A validade da CNH é obrigatória.");
        if (m.getTipoVinculo() == null)
            throw new CadastroException("O tipo de vínculo é obrigatório.");
    }
}