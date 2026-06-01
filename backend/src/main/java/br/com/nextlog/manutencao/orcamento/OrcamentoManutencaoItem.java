package br.com.nextlog.manutencao.orcamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrcamentoManutencaoItem {
    
    private Long id;
    private Long idOrcamento;
    private String descricao;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;
    private LocalDateTime criadoEm;

    public OrcamentoManutencaoItem() {}

    public OrcamentoManutencaoItem(Long id, String descricao, BigDecimal quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = quantidade.multiply(precoUnitario);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdOrcamento() { return idOrcamento; }
    public void setIdOrcamento(Long idOrcamento) { this.idOrcamento = idOrcamento; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { 
        this.quantidade = quantidade;
        recalcularTotal();
    }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { 
        this.precoUnitario = precoUnitario;
        recalcularTotal();
    }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    private void recalcularTotal() {
        if (quantidade != null && precoUnitario != null) {
            this.valorTotal = quantidade.multiply(precoUnitario);
        }
    }
}