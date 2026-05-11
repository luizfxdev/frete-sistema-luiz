"use client";

import { useState } from "react";
import type { ItemOrcamento } from "@/features/manutencao/adapters/manutencaoService";
import { formatCurrency } from "@/core/utils/formatters";

interface Props {
  itens: ItemOrcamento[];
  onSalvar: (itens: ItemOrcamento[]) => Promise<void>;
}

const ITEM_VAZIO: ItemOrcamento = { descricao: "", quantidade: 1, precoUnitario: 0 };

export function OrcamentoTable({ itens: initial, onSalvar }: Props) {
  const [itens, setItens] = useState<ItemOrcamento[]>(initial);
  const [saving, setSaving] = useState(false);

  const total = itens.reduce((acc, i) => acc + i.quantidade * i.precoUnitario, 0);

  const update = (idx: number, field: keyof ItemOrcamento, value: string | number) =>
    setItens((prev) => prev.map((it, i) => (i === idx ? { ...it, [field]: value } : it)));

  const adicionar = () => setItens((prev) => [...prev, { ...ITEM_VAZIO }]);
  const remover = (idx: number) => setItens((prev) => prev.filter((_, i) => i !== idx));

  const handleSalvar = async () => {
    setSaving(true);
    try { await onSalvar(itens); } finally { setSaving(false); }
  };

  return (
    <div className="bg-white/5 border border-white/7 rounded-2xl overflow-hidden">
      <div className="px-6 py-4 border-b border-white/7 flex items-center justify-between">
        <h3 className="text-sm font-bold text-white">Orçamento de Serviços</h3>
        <div className="flex items-center gap-2">
          <button
            onClick={adicionar}
            className="flex items-center gap-1.5 text-xs font-semibold text-accent hover:text-accent-light transition-colors"
          >
            <i className="bi bi-plus-circle" /> Adicionar item
          </button>
          <button
            onClick={handleSalvar}
            disabled={saving}
            className="flex items-center gap-1.5 px-3 py-1.5 bg-accent text-white text-xs font-semibold rounded-lg hover:bg-accent-dark transition-all disabled:opacity-50"
          >
            {saving ? <i className="bi bi-arrow-repeat animate-spin" /> : <i className="bi bi-floppy" />}
            Salvar
          </button>
        </div>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-white/6">
              {["Descrição", "Qtd", "Preço Unit.", "Total", ""].map((h) => (
                <th key={h} className="px-5 py-3 text-left text-xs font-bold uppercase tracking-widest text-white/30">
                  {h}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {itens.map((item, i) => (
              <tr key={i} className="border-b border-white/4">
                <td className="px-5 py-3">
                  <input
                    value={item.descricao}
                    onChange={(e) => update(i, "descricao", e.target.value)}
                    placeholder="Ex: Troca de óleo"
                    className="w-full bg-transparent text-white/80 text-sm outline-none border-b border-white/10 focus:border-accent transition-colors pb-0.5"
                  />
                </td>
                <td className="px-5 py-3 w-20">
                  <input
                    type="number"
                    min={1}
                    value={item.quantidade}
                    onChange={(e) => update(i, "quantidade", Number(e.target.value))}
                    className="w-full bg-transparent text-white/80 text-sm outline-none border-b border-white/10 focus:border-accent transition-colors pb-0.5"
                  />
                </td>
                <td className="px-5 py-3 w-32">
                  <input
                    type="number"
                    min={0}
                    step="0.01"
                    value={item.precoUnitario}
                    onChange={(e) => update(i, "precoUnitario", Number(e.target.value))}
                    className="w-full bg-transparent text-white/80 text-sm outline-none border-b border-white/10 focus:border-accent transition-colors pb-0.5"
                  />
                </td>
                <td className="px-5 py-3 text-white/70 font-medium">
                  {formatCurrency(item.quantidade * item.precoUnitario)}
                </td>
                <td className="px-5 py-3">
                  <button onClick={() => remover(i)} className="text-white/20 hover:text-red-400 transition-colors">
                    <i className="bi bi-trash3" />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
          <tfoot>
            <tr className="border-t border-white/10 bg-white/3">
              <td colSpan={3} className="px-5 py-4 text-right text-xs font-bold uppercase tracking-widest text-white/40">
                Total
              </td>
              <td className="px-5 py-4 font-bold text-white text-base">
                {formatCurrency(total)}
              </td>
              <td />
            </tr>
          </tfoot>
        </table>
      </div>
    </div>
  );
}