package br.com.nextlog.manutencao;

import br.com.nextlog.enums.StatusManutencao;
import br.com.nextlog.enums.TipoManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ManutencaoVeiculo {
    private Long id;
    private Long idVeiculo;
    private TipoManutencao tipo;
    private String placa;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal kmAtual;
    private BigDecimal custo;
    private String descricao;
    private StatusManutencao statusManutencao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String veiculoPlaca;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(Long idVeiculo) { this.idVeiculo = idVeiculo; }

    public TipoManutencao getTipo() { return tipo; }
    public void setTipo(TipoManutencao tipo) { this.tipo = tipo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public BigDecimal getKmAtual() { return kmAtual; }
    public void setKmAtual(BigDecimal kmAtual) { this.kmAtual = kmAtual; }

    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusManutencao getStatusManutencao() { return statusManutencao; }
    public void setStatusManutencao(StatusManutencao statusManutencao) { this.statusManutencao = statusManutencao; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public String getVeiculoPlaca() { return veiculoPlaca; }
    public void setVeiculoPlaca(String veiculoPlaca) { this.veiculoPlaca = veiculoPlaca; }
}