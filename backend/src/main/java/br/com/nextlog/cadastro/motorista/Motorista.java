package br.com.nextlog.cadastro.motorista;

import br.com.nextlog.enums.CnhCategoria;
import br.com.nextlog.enums.StatusMotorista;
import br.com.nextlog.enums.TipoVinculo;

import java.time.LocalDate;

public class Motorista {

    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String cnhNumero;
    private CnhCategoria cnhCategoria;
    private LocalDate cnhValidade;
    private TipoVinculo tipoVinculo;
    private StatusMotorista status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCnhNumero() { return cnhNumero; }
    public void setCnhNumero(String cnhNumero) { this.cnhNumero = cnhNumero; }

    public CnhCategoria getCnhCategoria() { return cnhCategoria; }
    public void setCnhCategoria(CnhCategoria cnhCategoria) { this.cnhCategoria = cnhCategoria; }

    public LocalDate getCnhValidade() { return cnhValidade; }
    public void setCnhValidade(LocalDate cnhValidade) { this.cnhValidade = cnhValidade; }

    public TipoVinculo getTipoVinculo() { return tipoVinculo; }
    public void setTipoVinculo(TipoVinculo tipoVinculo) { this.tipoVinculo = tipoVinculo; }

    public StatusMotorista getStatus() { return status; }
    public void setStatus(StatusMotorista status) { this.status = status; }

    public boolean isCnhVencida() {
        return cnhValidade != null && cnhValidade.isBefore(LocalDate.now());
    }
}