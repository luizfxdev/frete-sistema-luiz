export enum StatusVeiculo {
  DISPONIVEL = 'DISPONIVEL',
  EM_VIAGEM = 'EM_VIAGEM',
  EM_MANUTENCAO = 'EM_MANUTENCAO',
}

export enum TipoManutencao {
  PREVENTIVA = 'PREVENTIVA',
  CORRETIVA = 'CORRETIVA',
  SINISTRO = 'SINISTRO',
}

export enum StatusManutencao {
  EM_ANDAMENTO = 'EM_ANDAMENTO',
  CONCLUIDA = 'CONCLUIDA',
  CANCELADA = 'CANCELADA',
}

export enum CnhCategoria {
  A = 'A',
  B = 'B',
  C = 'C',
  D = 'D',
  E = 'E',
}

export enum TipoVinculo {
  FUNCIONARIO = 'FUNCIONARIO',
  AGREGADO = 'AGREGADO',
  TERCEIRO = 'TERCEIRO',
}

export enum Modalidade {
  RODOVIARIA = 'RODOVIARIA',
  HIDROVIARIA = 'HIDROVIARIA',
  FERROVIARIA = 'FERROVIARIA',
  AEREA = 'AEREA',
}

export enum StatusMotorista {
  ATIVO = 'ATIVO',
  INATIVO = 'INATIVO',
  BLOQUEADO = 'BLOQUEADO',
  PENDENTE = 'PENDENTE',
}

export enum StatusSolicitacao {
  PENDENTE = 'PENDENTE',
  ACEITA = 'ACEITA',
  RECUSADA = 'RECUSADA',
  CANCELADA = 'CANCELADA',
}

export enum StatusEntrega {
  EMITIDO = 'EMITIDO',
  SAIDA_CONFIRMADA = 'SAIDA_CONFIRMADA',
  EM_TRANSITO = 'EM_TRANSITO',
  ENTREGUE = 'ENTREGUE',
  DEVOLVIDO = 'DEVOLVIDO',
  CANCELADO = 'CANCELADO',
  NAO_ENTREGUE = 'NAO_ENTREGUE',
}

export interface Veiculo {
  id: number;
  placa: string;
  marca: string;
  modelo: string;
  cor: string;
  anoFabricacao: number;
  status: StatusVeiculo;
  taraKg: number;
  capacidadeKg: number;
  volumeM3?: number;
}

export interface ManutencaoVeiculo {
  id?: number;
  idVeiculo: number;
  tipo: TipoManutencao;
  placa: string;
  dataInicio: string;
  dataFim?: string;
  kmAtual: number;
  custo: number;
  descricao?: string;
  statusManutencao: StatusManutencao;
  dataCriacao?: string;
  dataAtualizacao?: string;
  veiculoPlaca?: string;
}

export interface OrcamentoManutencao {
  id?: number;
  idManutencao: number;
  numero: string;
  observacao?: string;
  valorTotal: number;
  criadoEm?: string;
  atualizadoEm?: string;
}

export interface OrcamentoManutencaoItem {
  id?: number;
  idOrcamento: number;
  descricao: string;
  quantidade: number;
  precoUnitario: number;
  valorTotal: number;
  criadoEm?: string;
}

export interface DashboardManutencaoKpis {
  emManutencao: number;
  preventiva: number;
  corretiva: number;
  liberados: number;
}

export interface ApiResponse<T> {
  data?: T;
  error?: string;
  success: boolean;
}