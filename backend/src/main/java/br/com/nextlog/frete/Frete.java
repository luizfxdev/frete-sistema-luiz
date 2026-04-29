package br.com.nextlog.frete;

import br.com.nextlog.enums.StatusFrete;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Frete {

    private Long id;
    private String numero;
    private Long idRemetente;
    private Long idDestinatario;
    private Long idMotorista;
    private Long idVeiculo;

    private String municipioOrigem;
    private String ufOrigem;
    private String municipioDestino;
    private String ufDestino;

    private String descricaoCarga;
    private BigDecimal pesoKg;
    private Integer volumes;

    private BigDecimal valorFrete;
    private BigDecimal aliquotaIcms;
    private BigDecimal valorIcms;
    private BigDecimal valorTotal;

    private StatusFrete status;
    private LocalDate dataEmissao;
    private LocalDate dataPrevisaoEntrega;
    private LocalDateTime dataSaida;
    private LocalDateTime dataEntrega;

    private String remetenteRazaoSocial;
    private String destinatarioRazaoSocial;
    private String motoristaNome;
    private String veiculoPlaca;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Long getIdRemetente() { return idRemetente; }
    public void setIdRemetente(Long idRemetente) { this.idRemetente = idRemetente; }

    public Long getIdDestinatario() { return idDestinatario; }
    public void setIdDestinatario(Long idDestinatario) { this.idDestinatario = idDestinatario; }

    public Long getIdMotorista() { return idMotorista; }
    public void setIdMotorista(Long idMotorista) { this.idMotorista = idMotorista; }

    public Long getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(Long idVeiculo) { this.idVeiculo = idVeiculo; }

    public String getMunicipioOrigem() { return municipioOrigem; }
    public void setMunicipioOrigem(String municipioOrigem) { this.municipioOrigem = municipioOrigem; }

    public String getUfOrigem() { return ufOrigem; }
    public void setUfOrigem(String ufOrigem) { this.ufOrigem = ufOrigem; }

    public String getMunicipioDestino() { return municipioDestino; }
    public void setMunicipioDestino(String municipioDestino) { this.municipioDestino = municipioDestino; }

    public String getUfDestino() { return ufDestino; }
    public void setUfDestino(String ufDestino) { this.ufDestino = ufDestino; }

    public String getDescricaoCarga() { return descricaoCarga; }
    public void setDescricaoCarga(String descricaoCarga) { this.descricaoCarga = descricaoCarga; }

    public BigDecimal getPesoKg() { return pesoKg; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }

    public Integer getVolumes() { return volumes; }
    public void setVolumes(Integer volumes) { this.volumes = volumes; }

    public BigDecimal getValorFrete() { return valorFrete; }
    public void setValorFrete(BigDecimal valorFrete) { this.valorFrete = valorFrete; }

    public BigDecimal getAliquotaIcms() { return aliquotaIcms; }
    public void setAliquotaIcms(BigDecimal aliquotaIcms) { this.aliquotaIcms = aliquotaIcms; }

    public BigDecimal getValorIcms() { return valorIcms; }
    public void setValorIcms(BigDecimal valorIcms) { this.valorIcms = valorIcms; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public StatusFrete getStatus() { return status; }
    public void setStatus(StatusFrete status) { this.status = status; }

    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }

    public LocalDate getDataPrevisaoEntrega() { return dataPrevisaoEntrega; }
    public void setDataPrevisaoEntrega(LocalDate dataPrevisaoEntrega) { this.dataPrevisaoEntrega = dataPrevisaoEntrega; }

    public LocalDateTime getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDateTime dataSaida) { this.dataSaida = dataSaida; }

    public LocalDateTime getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDateTime dataEntrega) { this.dataEntrega = dataEntrega; }

    public String getRemetenteRazaoSocial() { return remetenteRazaoSocial; }
    public void setRemetenteRazaoSocial(String remetenteRazaoSocial) { this.remetenteRazaoSocial = remetenteRazaoSocial; }

    public String getDestinatarioRazaoSocial() { return destinatarioRazaoSocial; }
    public void setDestinatarioRazaoSocial(String destinatarioRazaoSocial) { this.destinatarioRazaoSocial = destinatarioRazaoSocial; }

    public String getMotoristaNome() { return motoristaNome; }
    public void setMotoristaNome(String motoristaNome) { this.motoristaNome = motoristaNome; }

    public String getVeiculoPlaca() { return veiculoPlaca; }
    public void setVeiculoPlaca(String veiculoPlaca) { this.veiculoPlaca = veiculoPlaca; }
}