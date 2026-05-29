'use client';
import { Badge } from '@/shared/components/ui/Badge';
import type { ManutencaoVeiculo, StatusManutencao, TipoManutencao } from '@/shared/types/api';
import Link from 'next/link';

interface Props {
  data: ManutencaoVeiculo[];
  loading?: boolean;
}

const statusVariants: Record<StatusManutencao, 'default' | 'success' | 'warning' | 'danger' | 'info'> = {
  EM_ANDAMENTO: 'warning',
  CONCLUIDA: 'success',
  CANCELADA: 'danger',
};

const tipoVariants: Record<TipoManutencao, 'default' | 'success' | 'warning' | 'danger' | 'info'> = {
  PREVENTIVA: 'info',
  CORRETIVA: 'danger',
  SINISTRO: 'danger',
};

export function ManutencaoTable({ data, loading }: Props) {
  if (loading) {
    return (
      <div className="space-y-3 p-6">
        {[...Array(5)].map((_, i) => (
          <div key={i} className="h-16 rounded-xl bg-gray-200 animate-pulse" />
        ))}
      </div>
    );
  }

  if (!data || data.length === 0) {
    return (
      <div className="text-center py-12 text-gray-500">
        <i className="bi bi-inbox text-4xl mb-4 block" />
        <p>Nenhuma manutenção encontrada</p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-2xl overflow-hidden border border-gray-100 shadow-sm">
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-b border-gray-100 bg-gray-50">
              <th className="px-6 py-4 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                Placa
              </th>
              <th className="px-6 py-4 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                Tipo
              </th>
              <th className="px-6 py-4 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                Data Início
              </th>
              <th className="px-6 py-4 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                Status
              </th>
              <th className="px-6 py-4 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                Custo
              </th>
              <th className="px-6 py-4 text-left text-xs font-bold text-gray-600 uppercase tracking-wider">
                KM Atual
              </th>
              <th className="px-6 py-4 text-right text-xs font-bold text-gray-600 uppercase tracking-wider">
                Ação
              </th>
            </tr>
          </thead>
          <tbody>
            {data.map((item) => (
              <tr
                key={item.id}
                className="border-b border-gray-100 hover:bg-gray-50 transition-colors"
              >
                <td className="px-6 py-4">
                  <span className="font-mono font-bold text-gray-900">{item.placa}</span>
                </td>
                <td className="px-6 py-4">
                  <Badge
                    label={item.tipo}
                    variant={tipoVariants[item.tipo]}
                  />
                </td>
                <td className="px-6 py-4 text-sm text-gray-600">
                  {new Date(item.dataInicio).toLocaleDateString('pt-BR')}
                </td>
                <td className="px-6 py-4">
                  <Badge
                    label={item.statusManutencao}
                    variant={statusVariants[item.statusManutencao]}
                  />
                </td>
                <td className="px-6 py-4 font-semibold text-gray-900">
                  {new Intl.NumberFormat('pt-BR', {
                    style: 'currency',
                    currency: 'BRL',
                  }).format(item.custo)}
                </td>
                <td className="px-6 py-4 text-sm text-gray-600">
                  {item.kmAtual} km
                </td>
                <td className="px-6 py-4 text-right">
                  <Link
                    href={`/dashboard/manutencao/${item.id}`}
                    className="inline-flex items-center gap-2 px-3 py-2 rounded-lg bg-blue-50 hover:bg-blue-100 text-xs font-semibold text-blue-600 transition-colors"
                  >
                    <i className="bi bi-arrow-right" />
                    Ver
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}