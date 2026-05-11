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
  dataEntrega: string | null;
}

export async function buscarRastreamento(documento: string): Promise<EntregaRastreamento[]> {
  return get<EntregaRastreamento[]>(`/api/rastreamento/buscar?documento=${documento}`);
}
