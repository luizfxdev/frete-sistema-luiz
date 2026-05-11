import { get, post, patch } from "@/core/api/httpClient";
import { useAuthStore } from "@/core/auth/authStore";
import type { StatusMotorista, StatusSolicitacao } from "@/shared/types/api";

export interface MotoristaPeril {
  id: number;
  nome: string;
  cpf: string;
  cnhNumero: string;
  cnhCategoria: string;
  cnhValidade: string;
  status: StatusMotorista;
  disponivel: boolean;
  fotoUrl: string | null;
  cnhFotoUrl: string | null;
}

export interface SolicitacaoTransporte {
  id: number;
  municipioColeta: string;
  ufColeta: string;
  municipioDestino: string;
  ufDestino: string;
  tipoCarga: string;
  status: StatusSolicitacao;
  criadaEm: string;
}

export interface PerformanceMotorista {
  mes: string;
  totalEntregas: number;
  taxaSucesso: number;
  emAndamento: number;
}

function motoristaId() {
  return useAuthStore.getState().usuario?.id;
}

export async function buscarPerfil(): Promise<MotoristaPeril> {
  return get<MotoristaPeril>(`/motorista/${motoristaId()}/perfil`);
}

export async function setDisponibilidade(disponivel: boolean): Promise<void> {
  await patch<void>(`/motorista/${motoristaId()}/disponibilidade`, { disponivel });
}

export async function uploadFoto(file: File): Promise<{ url: string }> {
  const form = new FormData();
  form.append("file", file);
  return post<{ url: string }>(`/motorista/${motoristaId()}/foto`, form);
}

export async function uploadCnhFoto(file: File): Promise<{ url: string }> {
  const form = new FormData();
  form.append("file", file);
  return post<{ url: string }>(`/motorista/${motoristaId()}/cnh-foto`, form);
}

export async function listarSolicitacoes(): Promise<SolicitacaoTransporte[]> {
  return get<SolicitacaoTransporte[]>(`/motorista/${motoristaId()}/solicitacoes`);
}

export async function responderSolicitacao(
  id: number,
  acao: "ACEITAR" | "RECUSAR"
): Promise<void> {
  await post<void>(`/motorista/solicitacoes/${id}/${acao.toLowerCase()}`, {});
}

export async function buscarPerformanceMotorista(): Promise<PerformanceMotorista[]> {
  return get<PerformanceMotorista[]>(`/motorista/${motoristaId()}/performance`);
}