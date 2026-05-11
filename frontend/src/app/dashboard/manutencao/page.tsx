"use client";

import { useManutencaoKpis, useManutencoes } from "@/features/manutencao/hooks/useManutencao";
import { ManutencaoKpiCards } from "@/features/manutencao/components/ManutencaoKpiCards";
import { ManutencaoTable } from "@/features/manutencao/components/ManutencaoTable";

export default function ManutencaoPage() {
  const { kpis, loading: loadingKpis } = useManutencaoKpis();
  const { data, loading } = useManutencoes();

  return (
    <div className="space-y-8 max-w-6xl">
      <div>
        <h2 className="text-2xl font-bold text-white mb-1">Manutenção de Frota</h2>
        <p className="text-white/40 text-sm">Controle preventivo e corretivo dos veículos.</p>
      </div>

      {loadingKpis ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {Array.from({ length: 4 }).map((_, i) => (
            <div key={i} className="h-32 rounded-2xl bg-white/5 animate-pulse" />
          ))}
        </div>
      ) : kpis ? (
        <ManutencaoKpiCards kpis={kpis} />
      ) : null}

      <ManutencaoTable data={data} loading={loading} />
    </div>
  );
}