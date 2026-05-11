import { get } from "@/core/api/httpClient";
import type { Modalidade } from "@/shared/types/api";

export interface CotacaoParams {
  municipioOrigem: string;
  ufOrigem: string;
  municipioDestino: string;
  ufDestino: string;
  pesoKg: number;
  modalidade: Modalidade;
}

export interface CotacaoResult {
  valorBase: number;
  valorPorKg: number;
  valorSugerido: number;
}

export async function calcularCotacao(params: CotacaoParams): Promise<CotacaoResult> {
  const qs = new URLSearchParams({
    municipioOrigem: params.municipioOrigem,
    ufOrigem: params.ufOrigem,
    municipioDestino: params.municipioDestino,
    ufDestino: params.ufDestino,
    pesoKg: String(params.pesoKg),
    modalidade: params.modalidade,
  });
  return get<CotacaoResult>(`/cotacao?${qs}`);
}