package br.com.nextlog.cadastro.cliente;

import br.com.nextlog.exception.CadastroException;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.CnpjValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteBO {

    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public Long salvar(Cliente cliente) {
        validar(cliente);
        try {
            String cnpjLimpo = CnpjValidator.limpar(cliente.getCnpj());
            cliente.setCnpj(cnpjLimpo);

            if (clienteDAO.existePorCnpj(cnpjLimpo, cliente.getId())) {
                throw new CadastroException("Já existe um cliente cadastrado com este CNPJ.");
            }

            if (cliente.getStatus() == null || cliente.getStatus().trim().isEmpty()) {
                cliente.setStatus("ATIVO");
            }

            if (cliente.getId() == null) {
                return clienteDAO.inserir(cliente);
            }
            clienteDAO.atualizar(cliente);
            return cliente.getId();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao salvar cliente", e);
            throw new NegocioException("Erro ao salvar cliente. Tente novamente.");
        }
    }

    public void excluir(Long id) {
        if (id == null) throw new CadastroException("ID do cliente é obrigatório.");
        try {
            if (clienteDAO.possuiFretes(id)) {
                throw new CadastroException("Não é permitido excluir cliente com fretes vinculados.");
            }
            clienteDAO.excluir(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao excluir cliente", e);
            throw new NegocioException("Erro ao excluir cliente. Tente novamente.");
        }
    }

    public Cliente buscarPorId(Long id) {
        try {
            return clienteDAO.buscarPorId(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao buscar cliente", e);
            throw new NegocioException("Erro ao buscar cliente.");
        }
    }

    public List<Cliente> listar(String filtro, int pagina, int tamanhoPagina) {
        try {
            return clienteDAO.listar(filtro, pagina, Math.max(tamanhoPagina, 10));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar clientes", e);
            throw new NegocioException("Erro ao listar clientes.");
        }
    }

    public int contar(String filtro) {
        try {
            return clienteDAO.contar(filtro);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar clientes", e);
            throw new NegocioException("Erro ao contar clientes.");
        }
    }

    public List<Cliente> listarAtivos() {
        try {
            return clienteDAO.listarAtivos();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao listar clientes ativos", e);
            throw new NegocioException("Erro ao listar clientes ativos.");
        }
    }

    private void validar(Cliente c) {
        if (c == null) throw new CadastroException("Cliente é obrigatório.");
        if (c.getRazaoSocial() == null || c.getRazaoSocial().trim().isEmpty())
            throw new CadastroException("A razão social é obrigatória.");
        if (c.getTipo() == null)
            throw new CadastroException("O tipo do cliente é obrigatório.");
        if (!CnpjValidator.isValido(c.getCnpj()))
            throw new CadastroException("O CNPJ informado é inválido.");
        if (c.getLogradouro() == null || c.getLogradouro().trim().isEmpty())
            throw new CadastroException("O logradouro é obrigatório.");
        if (c.getNumero() == null || c.getNumero().trim().isEmpty())
            throw new CadastroException("O número do endereço é obrigatório.");
        if (c.getBairro() == null || c.getBairro().trim().isEmpty())
            throw new CadastroException("O bairro é obrigatório.");
        if (c.getMunicipio() == null || c.getMunicipio().trim().isEmpty())
            throw new CadastroException("O município é obrigatório.");
        if (c.getUf() == null || c.getUf().length() != 2)
            throw new CadastroException("A UF deve ter 2 caracteres.");
        if (c.getCep() == null || c.getCep().replaceAll("\\D", "").length() != 8)
            throw new CadastroException("O CEP informado é inválido.");
        c.setCep(c.getCep().replaceAll("\\D", ""));
        c.setUf(c.getUf().toUpperCase());
    }
}