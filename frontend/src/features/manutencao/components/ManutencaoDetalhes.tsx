'use client';
import { Badge } from '@/shared/components/ui/Badge';
import type { ManutencaoVeiculo, StatusManutencao } from '@/shared/types/api';

interface ManutencaoDetalhesProps {
  manutencao: ManutencaoVeiculo;
}

const statusVariants: Record<StatusManutencao, 'default' | 'success' | 'warning' | 'danger' | 'info'> = {
  EM_ANDAMENTO: 'warning',
  CONCLUIDA: 'success',
  CANCELADA: 'danger',
};

const tipoVariants: Record<string, 'default' | 'success' | 'warning' | 'danger' | 'info'> = {
  PREVENTIVA: 'info',
  CORRETIVA: 'danger',
  SINISTRO: 'danger',
};

export function ManutencaoDetalhes({ manutencao }: ManutencaoDetalhesProps) {
  return (
    <div className="bg-white rounded-2xl p-8 space-y-6 border border-gray-100 shadow-sm">
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-6">
        <div>
          <p className="text-xs font-bold text-gray-600 uppercase mb-2">Placa</p>
          <p className="text-xl font-bold text-gray-900 font-mono">{manutencao.placa}</p>
        </div>
        <div>
          <p className="text-xs font-bold text-gray-600 uppercase mb-2">Tipo</p>
          <Badge label={manutencao.tipo} variant={tipoVariants[manutencao.tipo] || 'default'} />
        </div>
        <div>
          <p className="text-xs font-bold text-gray-600 uppercase mb-2">Status</p>
          <Badge label={manutencao.statusManutencao} variant={statusVariants[manutencao.statusManutencao]} />
        </div>
        <div>
          <p className="text-xs font-bold text-gray-600 uppercase mb-2">Custo</p>
          <p className="text-xl font-bold text-gray-900">
            {new Intl.NumberFormat('pt-BR', {
              style: 'currency',
              currency: 'BRL',
            }).format(manutencao.custo)}
          </p>
        </div>
      </div>

      <div className="border-t border-gray-100 pt-6">
        <div className="grid grid-cols-2 sm:grid-cols-3 gap-6">
          <div>
            <p className="text-xs font-bold text-gray-600 uppercase mb-2">Data Início</p>
            <p className="text-sm font-semibold text-gray-900">
              {new Date(manutencao.dataInicio).toLocaleDateString('pt-BR')}
            </p>
          </div>
          {manutencao.dataFim && (
            <div>
              <p className="text-xs font-bold text-gray-600 uppercase mb-2">Data Conclusão</p>
              <p className="text-sm font-semibold text-gray-900">
                {new Date(manutencao.dataFim).toLocaleDateString('pt-BR')}
              </p>
            </div>
          )}
          <div>
            <p className="text-xs font-bold text-gray-600 uppercase mb-2">KM Atual</p>
            <p className="text-sm font-semibold text-gray-900">
              {new Intl.NumberFormat('pt-BR').format(Math.round(manutencao.kmAtual))} km
            </p>
          </div>
        </div>
      </div>

      {manutencao.descricao && (
        <div className="border-t border-gray-100 pt-6">
          <p className="text-xs font-bold text-gray-600 uppercase mb-3">Descrição</p>
          <p className="text-sm text-gray-600 leading-relaxed">{manutencao.descricao}</p>
        </div>
      )}
    </div>
  );
}