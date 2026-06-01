package br.com.nextlog.enums;
 
public enum StatusManutencao {
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");
 
    private final String descricao;
 
    StatusManutencao(String descricao) {
        this.descricao = descricao;
    }
 
    public String getDescricao() {
        return descricao;
    }
}
 
