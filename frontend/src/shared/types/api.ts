export interface ApiError {
  message: string;
  status?: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export type StatusManutencao = "AGENDADA" | "EM_ANDAMENTO" | "CONCLUIDA" | "CANCELADA";
export type TipoManutencao = "PREVENTIVA" | "CORRETIVA";
export type StatusEntrega = "EMITIDO" | "SAIDA_CONFIRMADA" | "EM_TRANSITO" | "ENTREGUE" | "NAO_ENTREGUE" | "CANCELADO";
export type RoleUsuario = "ADMIN" | "GESTOR" | "MOTORISTA";
export type StatusMotorista = "ATIVO" | "SUSPENSO";
export type StatusSolicitacao = "PENDENTE" | "ACEITA" | "RECUSADA";
export type TipoVinculo = "FUNCIONARIO" | "AGREGADO" | "TERCEIRO";
export type CnhCategoria = "A" | "B" | "C" | "D" | "E";
export type Modalidade = "TERRESTRE" | "AEREO";