package br.com.nextlog.manutencao.orcamento;

import java.math.BigDecimal;

public class OrcamentoManutencaoItem {
    private Long id;
    private Long idOrcamento;
    private String descricao;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdOrcamento() { return idOrcamento; }
    public void setIdOrcamento(Long idOrcamento) { this.idOrcamento = idOrcamento; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
}