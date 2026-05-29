package br.com.nextlog.enums;

public enum StatusVeiculo {
    DISPONIVEL("Disponível"),
    EM_VIAGEM("Em viagem"),
    EM_MANUTENCAO("Em manutenção");

    private final String descricao;

    StatusVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}