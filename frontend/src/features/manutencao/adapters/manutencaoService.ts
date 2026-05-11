import { get, post, put } from "@/core/api/httpClient";
import type { StatusManutencao, TipoManutencao } from "@/shared/types/api";

export interface Manutencao {
  id: number;
  placa: string;
  veiculo: string;
  tipo: TipoManutencao;
  descricao: string;
  dataInicio: string;
  dataFim: string | null;
  status: StatusManutencao;
  oficina: string;
}

export interface ManutencaoKpis {
  emManutencao: number;
  preventiva: number;
  corretiva: number;
  liberados: number;
}

export interface ItemOrcamento {
  id?: number;
  descricao: string;
  quantidade: number;
  precoUnitario: number;
}

export async function buscarKpisManutencao(): Promise<ManutencaoKpis> {
  return get<ManutencaoKpis>("/manutencao/kpis");
}

export async function listarManutencoes(): Promise<Manutencao[]> {
  return get<Manutencao[]>("/manutencao");
}

export async function buscarManutencao(id: number): Promise<Manutencao> {
  return get<Manutencao>(`/manutencao/${id}`);
}

export async function listarOrcamento(manutencaoId: number): Promise<ItemOrcamento[]> {
  return get<ItemOrcamento[]>(`/manutencao/${manutencaoId}/orcamento`);
}

export async function salvarOrcamento(
  manutencaoId: number,
  itens: ItemOrcamento[]
): Promise<ItemOrcamento[]> {
  return put<ItemOrcamento[]>(`/manutencao/${manutencaoId}/orcamento`, itens);
}

export async function adicionarItem(
  manutencaoId: number,
  item: ItemOrcamento
): Promise<ItemOrcamento> {
  return post<ItemOrcamento>(`/manutencao/${manutencaoId}/orcamento`, item);
}