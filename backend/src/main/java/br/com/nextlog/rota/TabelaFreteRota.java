package br.com.nextlog.rota;

import java.math.BigDecimal;

public class TabelaFreteRota {

    private Long id;
    private String municipioOrigem;
    private String ufOrigem;
    private String municipioDestino;
    private String ufDestino;
    private BigDecimal valorBase;
    private BigDecimal valorPorKg;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMunicipioOrigem() { return municipioOrigem; }
    public void setMunicipioOrigem(String municipioOrigem) { this.municipioOrigem = municipioOrigem; }

    public String getUfOrigem() { return ufOrigem; }
    public void setUfOrigem(String ufOrigem) { this.ufOrigem = ufOrigem; }

    public String getMunicipioDestino() { return municipioDestino; }
    public void setMunicipioDestino(String municipioDestino) { this.municipioDestino = municipioDestino; }

    public String getUfDestino() { return ufDestino; }
    public void setUfDestino(String ufDestino) { this.ufDestino = ufDestino; }

    public BigDecimal getValorBase() { return valorBase; }
    public void setValorBase(BigDecimal valorBase) { this.valorBase = valorBase; }

    public BigDecimal getValorPorKg() { return valorPorKg; }
    public void setValorPorKg(BigDecimal valorPorKg) { this.valorPorKg = valorPorKg; }
}