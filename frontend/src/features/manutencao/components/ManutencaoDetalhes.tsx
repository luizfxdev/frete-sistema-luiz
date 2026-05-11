import type { Manutencao } from "@/features/manutencao/adapters/manutencaoService";
import { formatDate } from "@/core/utils/formatters";

const STATUS_CFG: Record<string, { label: string; color: string }> = {
  AGENDADA: { label: "Agendada", color: "text-accent bg-accent/10" },
  EM_ANDAMENTO: { label: "Em andamento", color: "text-yellow-400 bg-yellow-400/10" },
  CONCLUIDA: { label: "Concluída", color: "text-success bg-success/10" },
  CANCELADA: { label: "Cancelada", color: "text-white/30 bg-white/5" },
};

interface Props {
  manutencao: Manutencao;
}

export function ManutencaoDetalhes({ manutencao: m }: Props) {
  const st = STATUS_CFG[m.status] ?? { label: m.status, color: "text-white/50" };

  return (
    <div className="bg-white/5 border border-white/7 rounded-2xl p-6 space-y-4">
      <div className="flex items-start justify-between flex-wrap gap-3">
        <div>
          <h2 className="text-xl font-bold text-white">{m.veiculo}</h2>
          <p className="text-white/40 text-sm font-mono">{m.placa}</p>
        </div>
        <span className={`text-xs font-bold px-3 py-1.5 rounded-full ${st.color}`}>
          {st.label}
        </span>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 pt-2">
        {[
          { label: "Tipo", value: m.tipo },
          { label: "Oficina", value: m.oficina },
          { label: "Data de início", value: formatDate(m.dataInicio) },
          { label: "Conclusão", value: m.dataFim ? formatDate(m.dataFim) : "Em aberto" },
        ].map(({ label, value }) => (
          <div key={label}>
            <p className="text-xs text-white/30 mb-0.5">{label}</p>
            <p className="text-sm font-semibold text-white">{value}</p>
          </div>
        ))}
      </div>

      {m.descricao && (
        <div className="pt-2 border-t border-white/6">
          <p className="text-xs text-white/30 mb-1">Descrição</p>
          <p className="text-sm text-white/70 leading-relaxed">{m.descricao}</p>
        </div>
      )}
    </div>
  );
}