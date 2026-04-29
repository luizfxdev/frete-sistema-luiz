package br.com.nextlog.rota;

import br.com.nextlog.exception.CadastroException;
import br.com.nextlog.exception.NegocioException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TabelaRotaBO {

    private static final Logger LOG = Logger.getLogger(TabelaRotaBO.class.getName());
    private final TabelaRotaDAO tabelaRotaDAO = new TabelaRotaDAO();

    public Long salvar(TabelaFreteRota r) {
        validar(r);
        try {
            r.setUfOrigem(r.getUfOrigem().toUpperCase());
            r.setUfDestino(r.getUfDestino().toUpperCase());
            if (r.getId() == null) return tabelaRotaDAO.inserir(r);
            tabelaRotaDAO.atualizar(r);
            return r.getId();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao salvar rota", e);
            throw new NegocioException("Erro ao salvar rota.");
        }
    }

    public void excluir(Long id) {
        if (id == null) throw new CadastroException("ID da rota é obrigatório.");
        try {
            tabelaRotaDAO.excluir(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao excluir rota", e);
            throw new NegocioException("Erro ao excluir rota.");
        }
    }

    public TabelaFreteRota buscarPorId(Long id) {
        try { return tabelaRotaDAO.buscarPorId(id); }
        catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar rota", e);
            throw new NegocioException("Erro ao buscar rota.");
        }
    }

    public BigDecimal sugerirValor(String municipioOrigem, String ufOrigem,
                                   String municipioDestino, String ufDestino, BigDecimal pesoKg) {
        try {
            TabelaFreteRota r = tabelaRotaDAO.buscarPorRota(municipioOrigem, ufOrigem, municipioDestino, ufDestino);
            if (r == null) return null;
            BigDecimal porPeso = (pesoKg == null) ? BigDecimal.ZERO
                    : r.getValorPorKg().multiply(pesoKg);
            return r.getValorBase().add(porPeso);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao sugerir valor de frete", e);
            throw new NegocioException("Erro ao sugerir valor de frete.");
        }
    }

    public List<TabelaFreteRota> listar(int pagina, int tamanhoPagina) {
        try { return tabelaRotaDAO.listar(pagina, Math.max(tamanhoPagina, 10)); }
        catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar rotas", e);
            throw new NegocioException("Erro ao listar rotas.");
        }
    }

    public int contar() {
        try { return tabelaRotaDAO.contar(); }
        catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar rotas", e);
            throw new NegocioException("Erro ao contar rotas.");
        }
    }

    private void validar(TabelaFreteRota r) {
        if (r == null) throw new CadastroException("Rota é obrigatória.");
        if (r.getMunicipioOrigem() == null || r.getMunicipioOrigem().trim().isEmpty())
            throw new CadastroException("Município de origem é obrigatório.");
        if (r.getUfOrigem() == null || r.getUfOrigem().length() != 2)
            throw new CadastroException("UF de origem inválida.");
        if (r.getMunicipioDestino() == null || r.getMunicipioDestino().trim().isEmpty())
            throw new CadastroException("Município de destino é obrigatório.");
        if (r.getUfDestino() == null || r.getUfDestino().length() != 2)
            throw new CadastroException("UF de destino inválida.");
        if (r.getValorBase() == null || r.getValorBase().compareTo(BigDecimal.ZERO) < 0)
            throw new CadastroException("Valor base inválido.");
        if (r.getValorPorKg() == null || r.getValorPorKg().compareTo(BigDecimal.ZERO) < 0)
            throw new CadastroException("Valor por kg inválido.");
    }
}