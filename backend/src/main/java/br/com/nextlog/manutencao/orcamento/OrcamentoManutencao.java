package br.com.nextlog.manutencao.orcamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoManutencao {
    private Long id;
    private Long idManutencao;
    private String numero;
    private String observacao;
    private BigDecimal valorTotal;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private List<OrcamentoManutencaoItem> itens = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdManutencao() { return idManutencao; }
    public void setIdManutencao(Long idManutencao) { this.idManutencao = idManutencao; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
    public List<OrcamentoManutencaoItem> getItens() { return itens; }
    public void setItens(List<OrcamentoManutencaoItem> itens) { this.itens = itens; }
}