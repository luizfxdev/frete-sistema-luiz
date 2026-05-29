package br.com.nextlog.cadastro.veiculo;

import br.com.nextlog.enums.StatusVeiculo;
import br.com.nextlog.enums.TipoVeiculo;

import java.math.BigDecimal;

public class Veiculo {
    private Long id;
    private String placa;
    private String rntrc;
    private Integer anoFabricacao;
    private String marca;
    private String modelo;
    private String cor;
    private TipoVeiculo tipo;
    private BigDecimal taraKg;
    private BigDecimal capacidadeKg;
    private BigDecimal volumeM3;
    private StatusVeiculo status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getRntrc() { return rntrc; }
    public void setRntrc(String rntrc) { this.rntrc = rntrc; }

    public Integer getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(Integer anoFabricacao) { this.anoFabricacao = anoFabricacao; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public TipoVeiculo getTipo() { return tipo; }
    public void setTipo(TipoVeiculo tipo) { this.tipo = tipo; }

    public BigDecimal getTaraKg() { return taraKg; }
    public void setTaraKg(BigDecimal taraKg) { this.taraKg = taraKg; }

    public BigDecimal getCapacidadeKg() { return capacidadeKg; }
    public void setCapacidadeKg(BigDecimal capacidadeKg) { this.capacidadeKg = capacidadeKg; }

    public BigDecimal getVolumeM3() { return volumeM3; }
    public void setVolumeM3(BigDecimal volumeM3) { this.volumeM3 = volumeM3; }

    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }
}