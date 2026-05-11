import { KpiCard } from "@/features/dashboard/components/KpiCard";
import type { ManutencaoKpis } from "@/features/manutencao/adapters/manutencaoService";

interface Props {
  kpis: ManutencaoKpis;
}

export function ManutencaoKpiCards({ kpis }: Props) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <KpiCard label="Em manutenção" value={kpis.emManutencao} icon="bi-tools" color="#f59e0b" />
      <KpiCard label="Preventiva" value={kpis.preventiva} icon="bi-shield-check" color="#005eff" />
      <KpiCard label="Corretiva" value={kpis.corretiva} icon="bi-wrench-adjustable" color="#ef4444" />
      <KpiCard label="Veículos liberados" value={kpis.liberados} icon="bi-check-circle-fill" color="#22c55e" />
    </div>
  );
}