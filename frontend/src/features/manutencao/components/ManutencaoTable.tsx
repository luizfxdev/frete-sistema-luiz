import Link from "next/link";
import type { Manutencao } from "@/features/manutencao/adapters/manutencaoService";
import { formatDate } from "@/core/utils/formatters";

const STATUS_CFG: Record<string, { label: string; color: string }> = {
  AGENDADA: { label: "Agendada", color: "text-accent bg-accent/10" },
  EM_ANDAMENTO: { label: "Em andamento", color: "text-yellow-400 bg-yellow-400/10" },
  CONCLUIDA: { label: "Concluída", color: "text-success bg-success/10" },
  CANCELADA: { label: "Cancelada", color: "text-white/30 bg-white/5" },
};

const TIPO_CFG: Record<string, string> = {
  PREVENTIVA: "text-accent",
  CORRETIVA: "text-red-400",
};

interface Props {
  data: Manutencao[];
  loading: boolean;
}

export function ManutencaoTable({ data, loading }: Props) {
  if (loading) {
    return (
      <div className="space-y-2">
        {Array.from({ length: 4 }).map((_, i) => (
          <div key={i} className="h-14 rounded-xl bg-white/5 animate-pulse" />
        ))}
      </div>
    );
  }

  return (
    <div className="bg-white/5 border border-white/7 rounded-2xl overflow-hidden">
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-white/6">
              {["Veículo", "Tipo", "Início", "Fim", "Status", ""].map((h) => (
                <th key={h} className="px-6 py-3.5 text-left text-xs font-bold uppercase tracking-widest text-white/30">
                  {h}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {data.map((m) => {
              const st = STATUS_CFG[m.status] ?? { label: m.status, color: "text-white/50" };
              return (
                <tr key={m.id} className="border-b border-white/4 hover:bg-white/3 transition-colors">
                  <td className="px-6 py-4">
                    <p className="font-semibold text-white">{m.veiculo}</p>
                    <p className="text-xs text-white/35 font-mono">{m.placa}</p>
                  </td>
                  <td className="px-6 py-4">
                    <span className={`text-xs font-bold uppercase tracking-wide ${TIPO_CFG[m.tipo]}`}>
                      {m.tipo}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-white/60 text-xs">{formatDate(m.dataInicio)}</td>
                  <td className="px-6 py-4 text-white/60 text-xs">{m.dataFim ? formatDate(m.dataFim) : "—"}</td>
                  <td className="px-6 py-4">
                    <span className={`text-xs font-bold px-2.5 py-1 rounded-full ${st.color}`}>
                      {st.label}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <Link
                      href={`/dashboard/manutencao/${m.id}`}
                      className="flex items-center gap-1 text-xs font-semibold text-accent hover:text-accent-light transition-colors whitespace-nowrap"
                    >
                      <i className="bi bi-eye" />
                      Detalhes do Veículo
                    </Link>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
        {data.length === 0 && (
          <p className="py-12 text-center text-white/30 text-sm">Nenhuma manutenção registrada.</p>
        )}
      </div>
    </div>
  );
}