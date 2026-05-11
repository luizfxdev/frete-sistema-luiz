package br.com.nextlog.transporte;

import java.time.LocalDateTime;

public class SolicitacaoTransporte {
    private Long id;
    private Long idMotorista;
    private String municipioColeta;
    private String ufColeta;
    private String logradouroColeta;
    private String municipioDestino;
    private String ufDestino;
    private String logradouroDestino;
    private String descricaoCarga;
    private String status;
    private String respostaMotorista;
    private LocalDateTime criadoEm;
    private LocalDateTime respondidoEm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdMotorista() { return idMotorista; }
    public void setIdMotorista(Long idMotorista) { this.idMotorista = idMotorista; }
    public String getMunicipioColeta() { return municipioColeta; }
    public void setMunicipioColeta(String municipioColeta) { this.municipioColeta = municipioColeta; }
    public String getUfColeta() { return ufColeta; }
    public void setUfColeta(String ufColeta) { this.ufColeta = ufColeta; }
    public String getLogradouroColeta() { return logradouroColeta; }
    public void setLogradouroColeta(String logradouroColeta) { this.logradouroColeta = logradouroColeta; }
    public String getMunicipioDestino() { return municipioDestino; }
    public void setMunicipioDestino(String municipioDestino) { this.municipioDestino = municipioDestino; }
    public String getUfDestino() { return ufDestino; }
    public void setUfDestino(String ufDestino) { this.ufDestino = ufDestino; }
    public String getLogradouroDestino() { return logradouroDestino; }
    public void setLogradouroDestino(String logradouroDestino) { this.logradouroDestino = logradouroDestino; }
    public String getDescricaoCarga() { return descricaoCarga; }
    public void setDescricaoCarga(String descricaoCarga) { this.descricaoCarga = descricaoCarga; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRespostaMotorista() { return respostaMotorista; }
    public void setRespostaMotorista(String respostaMotorista) { this.respostaMotorista = respostaMotorista; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getRespondidoEm() { return respondidoEm; }
    public void setRespondidoEm(LocalDateTime respondidoEm) { this.respondidoEm = respondidoEm; }
}