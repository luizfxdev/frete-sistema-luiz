import { get } from "@/core/api/httpClient";

export interface PerformanceKpis {
  totalFretes: number;
  fretesEntregues: number;
  fretesAtrasados: number;
  fretesEmAndamento: number;
  taxaPontualidade: number;
  receita: number;
}

export interface EntregaDia {
  dia: string;
  entregues: number;
  emTransito: number;
  atrasados: number;
}

export interface FretesPorStatus {
  name: string;
  value: number;
}

export interface VolumePorDia {
  dia: string;
  volume: number;
}

export interface FretesPorUf {
  uf: string;
  quantidade: number;
}

export interface TaxaSucessoAtraso {
  semana: string;
  sucesso: number;
  atraso: number;
}

export interface TopMotorista {
  nome: string;
  totalFretes: number;
}

export interface UltimoFrete {
  numero: string;
  municipioOrigem: string;
  municipioDestino: string;
  nomeMotorista: string;
  valorTotal: number;
  status: string;
}

export interface EntregaPorEstado {
  estado: string;
  quantidade: number;
}

export interface PerformanceData {
  kpis: PerformanceKpis;
  entregasPorDia: EntregaDia[];
  fretosPorStatus: FretesPorStatus[];
  volumeTransportado: VolumePorDia[];
  fretesPorRegiao: FretesPorUf[];
  taxaSucessoAtraso: TaxaSucessoAtraso[];
  topMotoristas: TopMotorista[];
  ultimosFretes: UltimoFrete[];
  entregasPorEstado: EntregaPorEstado[];
}

export async function buscarPerformance(
  dataInicio: string,
  dataFim: string
): Promise<PerformanceData> {
  return get<PerformanceData>(
    `/api/dashboard/performance?dataInicio=${dataInicio}&dataFim=${dataFim}`
  );
}