import { post } from "@/core/api/publicHttpClient";
import type { CnhCategoria, TipoVinculo } from "@/shared/types/api";

export interface CandidaturaPayload {
  nome: string;
  cpf: string;
  dataNascimento: string;
  telefone: string;
  email: string;
  cnhNumero: string;
  cnhCategoria: CnhCategoria;
  cnhValidade: string;
  tipoVinculo: TipoVinculo;
  municipio: string;
  uf: string;
  mensagem?: string;
}

export async function enviarCandidatura(payload: CandidaturaPayload): Promise<void> {
  await post<void>("/candidaturas", payload);
}