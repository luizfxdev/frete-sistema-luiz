'use client';
import { KpiCard } from '@/features/dashboard/components/KpiCard';
import type { DashboardManutencaoKpis } from '@/shared/types/api';

interface Props {
  kpis: DashboardManutencaoKpis;
}

export function ManutencaoKpiCards({ kpis }: Props) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 animate-fadeIn">
      <KpiCard
        title="Em manutenção"
        value={kpis.emManutencao}
        icon="bi-tools"
        backgroundColor="bg-white"
        iconBackgroundColor="bg-amber-100"
        textColor="text-amber-600"
      />
      <KpiCard
        title="Preventiva"
        value={kpis.preventiva}
        icon="bi-shield-check"
        backgroundColor="bg-white"
        iconBackgroundColor="bg-blue-100"
        textColor="text-blue-600"
      />
      <KpiCard
        title="Corretiva"
        value={kpis.corretiva}
        icon="bi-wrench-adjustable"
        backgroundColor="bg-white"
        iconBackgroundColor="bg-red-100"
        textColor="text-red-600"
      />
      <KpiCard
        title="Veículos liberados"
        value={kpis.liberados}
        icon="bi-check-circle-fill"
        backgroundColor="bg-white"
        iconBackgroundColor="bg-green-100"
        textColor="text-green-600"
      />
    </div>
  );
}