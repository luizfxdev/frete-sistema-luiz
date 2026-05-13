import { get } from "@/core/api/publicHttpClient";
import type { StatusEntrega } from "@/shared/types/api";

export interface EntregaRastreamento {
    numero: string;
    status: StatusEntrega;
    remetente: string;
    destinatario: string;
    municipioOrigem: string;
    ufOrigem: string;
    municipioDestino: string;
    ufDestino: string;
    dataEmissao: string;
    dataPrevisaoEntrega: string;
    dataSaida: string | null;
    dataEntrega: string | null;
}

export interface OcorrenciaRastreamento {
    tipo: string;
    dataHora: string;
    municipio: string;
    uf: string;
    descricao: string;
}

export interface RastreamentoResponse {
    sucesso: boolean;
    frete: EntregaRastreamento;
    ocorrencias: OcorrenciaRastreamento[];
}

export async function buscarRastreamento(numero: string): Promise<RastreamentoResponse> {
    if (!numero || !numero.match(/^FRT-\d{4}-\d{5}$/)) {
        throw new Error("Número de frete inválido");
    }
    return get<RastreamentoResponse>(`/api/rastreamento/${numero.toUpperCase()}`);
}