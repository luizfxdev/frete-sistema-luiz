import { get } from "@/core/api/httpClient";

export interface DashboardKpis {
  totalFretes: number;
  emAberto: number;
  entregues: number;
  naoEntregues: number;
  cancelados: number;
  motoristasAtivos: number;
  veiculosDisponiveis: number;
  veiculosEmViagem: number;
}

export interface DashboardIndicadores {
  periodo: string;
  entregasNoPrazo: number;
  entregasForaDoPrazo: number;
  entregasNaoRealizadas: number;
  distanciaMedia: number;
  consumoCombustivel: number;
  fretesCompletados: number;
  ticketMedio: string;
  taxaUtilizacaoFrota: number;
}

export async function buscarKpis(): Promise<DashboardKpis> {
  return get<DashboardKpis>("/dashboard/kpis");
}

export async function buscarIndicadores(): Promise<DashboardIndicadores> {
  return get<DashboardIndicadores>("/dashboard/indicadores");
}