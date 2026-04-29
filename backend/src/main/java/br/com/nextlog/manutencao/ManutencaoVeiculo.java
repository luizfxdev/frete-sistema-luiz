package br.com.nextlog.manutencao;

import br.com.nextlog.enums.TipoManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ManutencaoVeiculo {

    private Long id;
    private Long idVeiculo;
    private TipoManutencao tipo;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal custo;

    private String veiculoPlaca;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(Long idVeiculo) { this.idVeiculo = idVeiculo; }

    public TipoManutencao getTipo() { return tipo; }
    public void setTipo(TipoManutencao tipo) { this.tipo = tipo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }

    public String getVeiculoPlaca() { return veiculoPlaca; }
    public void setVeiculoPlaca(String veiculoPlaca) { this.veiculoPlaca = veiculoPlaca; }
}