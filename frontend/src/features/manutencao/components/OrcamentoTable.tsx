'use client';

import { useState } from 'react';
import { EditOrcamentoModal } from './EditOrcamentoModal';
import type { OrcamentoManutencaoItem } from '@/shared/types/api';

interface Props {
  itens: OrcamentoManutencaoItem[];
  onSalvar: (item: OrcamentoManutencaoItem) => Promise<void>;
}

export function OrcamentoTable({ itens, onSalvar }: Props) {
  const [editando, setEditando] = useState<OrcamentoManutencaoItem | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  const handleSalvar = async (item: OrcamentoManutencaoItem) => {
    setIsSaving(true);
    try {
      await onSalvar(item);
      setEditando(null);
    } finally {
      setIsSaving(false);
    }
  };

  const total = itens.reduce((acc, item) => acc + (item.valorTotal || 0), 0);

  if (!itens.length) {
    return (
      <div className="text-center py-8 text-gray-500">
        <i className="bi bi-inbox text-2xl mb-2 block" />
        <p className="text-sm">Nenhum item de orçamento</p>
      </div>
    );
  }

  return (
    <>
      <div className="space-y-4">
        <div className="bg-white rounded-2xl overflow-hidden border border-gray-100 shadow-sm">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-100 bg-gray-50">
                <th className="px-4 py-3 text-left text-xs font-bold text-gray-600 uppercase">
                  Descrição
                </th>
                <th className="px-4 py-3 text-right text-xs font-bold text-gray-600 uppercase">
                  Qtd
                </th>
                <th className="px-4 py-3 text-right text-xs font-bold text-gray-600 uppercase">
                  Valor Unit.
                </th>
                <th className="px-4 py-3 text-right text-xs font-bold text-gray-600 uppercase">
                  Total
                </th>
                <th className="px-4 py-3 text-right text-xs font-bold text-gray-600 uppercase">
                  Ação
                </th>
              </tr>
            </thead>
            <tbody>
              {itens.map((item) => (
                <tr
                  key={item.id}
                  className="border-b border-gray-100 hover:bg-gray-50 transition-colors"
                >
                  <td className="px-4 py-3 text-sm text-gray-900">
                    {item.descricao}
                  </td>
                  <td className="px-4 py-3 text-right text-sm text-gray-600">
                    {item.quantidade?.toLocaleString('pt-BR', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </td>
                  <td className="px-4 py-3 text-right text-sm text-gray-600">
                    {new Intl.NumberFormat('pt-BR', {
                      style: 'currency',
                      currency: 'BRL',
                    }).format(item.precoUnitario || 0)}
                  </td>
                  <td className="px-4 py-3 text-right text-sm font-semibold text-gray-900">
                    {new Intl.NumberFormat('pt-BR', {
                      style: 'currency',
                      currency: 'BRL',
                    }).format(item.valorTotal || 0)}
                  </td>
                  <td className="px-4 py-3 text-right">
                    <button
                      onClick={() => setEditando(item)}
                      className="text-xs font-semibold text-blue-600 hover:text-blue-700 transition-colors"
                    >
                      Editar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="flex justify-end">
          <div className="text-right">
            <p className="text-xs font-bold text-gray-600 uppercase mb-1">Total</p>
            <p className="text-2xl font-bold text-gray-900">
              {new Intl.NumberFormat('pt-BR', {
                style: 'currency',
                currency: 'BRL',
              }).format(total)}
            </p>
          </div>
        </div>
      </div>

      {editando && (
        <EditOrcamentoModal
          item={editando}
          isOpen={!!editando}
          onClose={() => setEditando(null)}
          onSalvar={handleSalvar}
          isLoading={isSaving}
        />
      )}
    </>
  );
}