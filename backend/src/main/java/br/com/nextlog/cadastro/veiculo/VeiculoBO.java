package br.com.nextlog.cadastro.veiculo;

import br.com.nextlog.enums.StatusVeiculo;
import br.com.nextlog.exception.CadastroException;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.PlacaValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VeiculoBO {

    private static final Logger LOG = Logger.getLogger(VeiculoBO.class.getName());
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    public Long salvar(Veiculo v) {
        validar(v);
        try {
            String placaNorm = PlacaValidator.normalizar(v.getPlaca());
            v.setPlaca(placaNorm);

            if (veiculoDAO.existePorPlaca(placaNorm, v.getId())) {
                throw new CadastroException("Já existe um veículo cadastrado com esta placa.");
            }

            if (v.getStatus() == null) v.setStatus(StatusVeiculo.DISPONIVEL);

            if (v.getId() != null && v.getStatus() == StatusVeiculo.DISPONIVEL
                    && veiculoDAO.estaEmFreteAtivo(v.getId())) {
                throw new CadastroException(
                        "Veículo está em frete ativo e só pode voltar para Disponível pela conclusão do frete.");
            }

            if (v.getId() == null) return veiculoDAO.inserir(v);
            veiculoDAO.atualizar(v);
            return v.getId();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao salvar veículo", e);
            throw new NegocioException("Erro ao salvar veículo. Tente novamente.");
        }
    }

    public void excluir(Long id) {
        try {
            Veiculo v = veiculoDAO.buscarPorId(id);
            if (v != null && v.getStatus() == StatusVeiculo.EM_VIAGEM) {
                throw new CadastroException("Veículo em viagem não pode ser excluído.");
            }
            if (veiculoDAO.estaEmFreteAtivo(id)) {
                throw new CadastroException("Veículo possui fretes ativos e não pode ser excluído.");
            }
            veiculoDAO.excluir(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao excluir veículo", e);
            throw new NegocioException("Erro ao excluir veículo.");
        }
    }

    public Veiculo buscarPorId(Long id) {
        try {
            return veiculoDAO.buscarPorId(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar veículo", e);
            throw new NegocioException("Erro ao buscar veículo.");
        }
    }

    public List<Veiculo> listar(String filtro, int pagina, int tamanhoPagina) {
        try {
            return veiculoDAO.listar(filtro, pagina, Math.max(tamanhoPagina, 10));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar veículos", e);
            throw new NegocioException("Erro ao listar veículos.");
        }
    }

    public int contar(String filtro) {
        try {
            return veiculoDAO.contar(filtro);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar veículos", e);
            throw new NegocioException("Erro ao contar veículos.");
        }
    }

    public Map<StatusVeiculo, Integer> contarPorStatus() {
        try {
            Map<StatusVeiculo, Integer> totais = new EnumMap<>(StatusVeiculo.class);
            for (StatusVeiculo s : StatusVeiculo.values()) {
                totais.put(s, veiculoDAO.contarPorStatus(s));
            }
            return totais;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar veículos por status", e);
            throw new NegocioException("Erro ao contar veículos por status.");
        }
    }

    public List<Veiculo> listarDisponiveis() {
        try {
            return veiculoDAO.listarDisponiveis();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar veículos disponíveis", e);
            throw new NegocioException("Erro ao listar veículos disponíveis.");
        }
    }

    public List<Veiculo> listarTodos() {
        try {
            return veiculoDAO.listarTodos();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar veículos", e);
            throw new NegocioException("Erro ao listar veículos.");
        }
    }

    private void validar(Veiculo v) {
        if (v == null) throw new CadastroException("Veículo é obrigatório.");
        if (!PlacaValidator.isValida(v.getPlaca()))
            throw new CadastroException("A placa informada é inválida.");
        if (v.getAnoFabricacao() == null
                || v.getAnoFabricacao() < 1980
                || v.getAnoFabricacao() > LocalDate.now().getYear() + 1)
            throw new CadastroException("Ano de fabricação inválido.");
        if (v.getTipo() == null)
            throw new CadastroException("O tipo do veículo é obrigatório.");
        if (v.getTaraKg() == null || v.getTaraKg().compareTo(BigDecimal.ZERO) <= 0)
            throw new CadastroException("A tara do veículo deve ser maior que zero.");
        if (v.getCapacidadeKg() == null || v.getCapacidadeKg().compareTo(BigDecimal.ZERO) <= 0)
            throw new CadastroException("A capacidade de carga deve ser maior que zero.");
    }
}