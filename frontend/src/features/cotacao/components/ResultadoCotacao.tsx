import { formatCurrency } from "@/core/utils/formatters";
import type { CotacaoResult } from "@/features/cotacao/adapters/cotacaoService";

interface Props {
  resultado: CotacaoResult;
  pesoKg: number;
}

export function ResultadoCotacao({ resultado, pesoKg }: Props) {
  return (
    <div className="bg-accent/5 border border-accent/20 rounded-2xl p-6 space-y-4">
      <h3 className="font-bold text-brand-900 text-base flex items-center gap-2">
        <i className="bi bi-calculator-fill text-accent" />
        Resultado da Cotação
      </h3>
      <div className="grid grid-cols-2 gap-3">
        {[
          { label: "Valor Base", value: formatCurrency(resultado.valorBase) },
          { label: "Valor por kg", value: `${formatCurrency(resultado.valorPorKg)}/kg` },
          { label: "Peso informado", value: `${pesoKg} kg` },
        ].map(({ label, value }) => (
          <div key={label} className="bg-white rounded-xl p-4 border border-gray-100">
            <p className="text-xs text-gray-400 mb-1">{label}</p>
            <p className="font-semibold text-brand-900">{value}</p>
          </div>
        ))}
        <div className="bg-accent rounded-xl p-4 col-span-2">
          <p className="text-xs text-white/70 mb-1">Valor estimado total</p>
          <p className="text-2xl font-bold text-white">{formatCurrency(resultado.valorSugerido)}</p>
        </div>
      </div>
      <p className="text-xs text-gray-400">
        * Valores estimados. Sujeitos a alteração conforme condições da carga.
      </p>
    </div>
  );
}