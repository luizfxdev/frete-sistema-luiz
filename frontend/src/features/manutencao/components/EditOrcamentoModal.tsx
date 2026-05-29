'use client';

import { useState } from 'react';
import { X } from 'lucide-react';
import type { OrcamentoManutencaoItem } from '@/shared/types/api';

interface EditOrcamentoModalProps {
  item: OrcamentoManutencaoItem;
  isOpen: boolean;
  onClose: () => void;
  onSalvar: (item: OrcamentoManutencaoItem) => Promise<void>;
  isLoading?: boolean;
}

export function EditOrcamentoModal({
  item,
  isOpen,
  onClose,
  onSalvar,
  isLoading = false,
}: EditOrcamentoModalProps) {
  const [formData, setFormData] = useState<OrcamentoManutencaoItem>(item);
  const [error, setError] = useState<string | null>(null);

  const handleChange = (field: keyof OrcamentoManutencaoItem, value: any) => {
    setFormData((prev) => {
      const updated = { ...prev } as OrcamentoManutencaoItem;
      
      if (field === 'descricao') {
        updated.descricao = value as string;
      } else if (field === 'quantidade') {
        updated.quantidade = parseFloat(value);
      } else if (field === 'precoUnitario') {
        updated.precoUnitario = parseFloat(value);
      }

      if (field === 'quantidade' || field === 'precoUnitario') {
        const qty = updated.quantidade || 0;
        const price = updated.precoUnitario || 0;
        updated.valorTotal = qty * price;
      }

      return updated;
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!formData.descricao?.trim()) {
      setError('Descrição é obrigatória');
      return;
    }
    if (!formData.quantidade || formData.quantidade <= 0) {
      setError('Quantidade deve ser maior que 0');
      return;
    }
    if (!formData.precoUnitario || formData.precoUnitario <= 0) {
      setError('Preço unitário deve ser maior que 0');
      return;
    }

    try {
      await onSalvar(formData);
      onClose();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro ao salvar');
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl shadow-xl max-w-md w-full p-6 animate-fadeIn">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold text-gray-900">Editar Item</h2>
          <button
            onClick={onClose}
            disabled={isLoading}
            className="p-1 hover:bg-gray-100 rounded transition-colors disabled:opacity-50"
            aria-label="Fechar"
          >
            <X size={20} className="text-gray-600" />
          </button>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-red-100 border border-red-300 rounded-lg">
            <p className="text-sm text-red-800">{error}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-semibold text-gray-900 mb-2">
              Descrição
            </label>
            <input
              type="text"
              value={formData.descricao || ''}
              onChange={(e) => handleChange('descricao', e.target.value)}
              disabled={isLoading}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
              placeholder="Ex: Óleo sintetizado 10W30"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-semibold text-gray-900 mb-2">
                Quantidade
              </label>
              <input
                type="number"
                step="0.01"
                value={formData.quantidade || 0}
                onChange={(e) => handleChange('quantidade', parseFloat(e.target.value))}
                disabled={isLoading}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
              />
            </div>

            <div>
              <label className="block text-sm font-semibold text-gray-900 mb-2">
                Preço Unit.
              </label>
              <input
                type="number"
                step="0.01"
                value={formData.precoUnitario || 0}
                onChange={(e) => handleChange('precoUnitario', parseFloat(e.target.value))}
                disabled={isLoading}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
              />
            </div>
          </div>

          <div className="pt-2 pb-4 border-t border-gray-200">
            <p className="text-sm text-gray-600">
              Total: <span className="font-semibold text-gray-900">
                R$ {(formData.valorTotal || 0).toFixed(2).replace('.', ',')}
              </span>
            </p>
          </div>

          <div className="flex gap-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              disabled={isLoading}
              className="flex-1 px-4 py-2 border border-gray-300 rounded-lg text-gray-700 font-semibold hover:bg-gray-50 transition-colors disabled:opacity-50"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isLoading}
              className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg font-semibold hover:bg-blue-700 transition-colors disabled:opacity-50"
            >
              {isLoading ? 'Salvando...' : 'Salvar'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}