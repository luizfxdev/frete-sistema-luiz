package br.com.nextlog.cadastro.cliente;

import br.com.nextlog.enums.TipoCliente;
import br.com.nextlog.exception.CadastroException;
import br.com.nextlog.exception.NegocioException;
import br.com.nextlog.util.CnpjValidator;
import br.com.nextlog.util.CpfValidator;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteBO {

    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public Long salvar(Cliente cliente) {
        validar(cliente);
        try {
            String documentoLimpo = "CNPJ".equals(cliente.getTipoDocumento())
                    ? CnpjValidator.limpar(cliente.getDocumento())
                    : CpfValidator.limpar(cliente.getDocumento());
            cliente.setDocumento(documentoLimpo);

            if (clienteDAO.existePorDocumento(documentoLimpo, cliente.getId())) {
                throw new CadastroException("Já existe um cliente cadastrado com este documento.");
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

    public Map<String, Integer> contarPorStatus() {
        try {
            Map<String, Integer> totais = new HashMap<>();
            totais.put("ATIVO", clienteDAO.contarPorStatus("ATIVO"));
            totais.put("INATIVO", clienteDAO.contarPorStatus("INATIVO"));
            return totais;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar clientes por status", e);
            throw new NegocioException("Erro ao contar clientes por status.");
        }
    }

    public Map<String, Integer> contarPorTipo() {
        try {
            Map<String, Integer> totais = new HashMap<>();
            totais.put("PF", clienteDAO.contarPorTipoDocumento("CPF"));
            totais.put("PJ", clienteDAO.contarPorTipoDocumento("CNPJ"));
            return totais;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao contar clientes por tipo", e);
            throw new NegocioException("Erro ao contar clientes por tipo.");
        }
    }

    private void validar(Cliente c) {
        if (c == null) throw new CadastroException("Cliente é obrigatório.");
        if (c.getRazaoSocial() == null || c.getRazaoSocial().trim().isEmpty())
            throw new CadastroException("A razão social é obrigatória.");
        if (c.getTipo() == null)
            throw new CadastroException("O tipo do cliente é obrigatório.");
        if (c.getTipoDocumento() == null || c.getTipoDocumento().trim().isEmpty())
            throw new CadastroException("O tipo de documento é obrigatório.");
        if (!"CPF".equals(c.getTipoDocumento()) && !"CNPJ".equals(c.getTipoDocumento()))
            throw new CadastroException("Tipo de documento inválido.");

        if ("CNPJ".equals(c.getTipoDocumento())) {
            if (!CnpjValidator.isValido(c.getDocumento()))
                throw new CadastroException("O CNPJ informado é inválido.");
        } else {
            if (!CpfValidator.isValido(c.getDocumento()))
                throw new CadastroException("O CPF informado é inválido.");
        }

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