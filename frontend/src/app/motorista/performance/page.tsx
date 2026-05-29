"use client";
import { usePerformanceMotorista } from "@/features/motorista/hooks/useMotorista";
import { MotoristaEntregasLineChart, type MotoristaEntregaDia } from "@/features/performance/components/Motoristaperformancecharts";
import { KpiCard } from "@/features/dashboard/components/KpiCard";
import { Truck, Award, RotateCw } from "lucide-react";

interface PerformanceData {
  mes: string;
  totalEntregas: number;
  emAndamento: number;
  taxaSucesso: number;
}

interface Totais {
  entregas: number;
  emAndamento: number;
  taxaMedia: number;
}

interface SkeletonProps {
  count?: number;
}

function LoadingSkeleton({ count = 3 }: SkeletonProps) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
      {Array.from({ length: count }).map((_, i) => (
        <div key={i} className="h-32 rounded-2xl bg-white/5 animate-pulse" />
      ))}
    </div>
  );
}

interface EmptyStateProps {
  title?: string;
  message?: string;
}

function EmptyState({ title = "Sem dados", message = "Nenhuma entrega registrada ainda." }: EmptyStateProps) {
  return (
    <div className="bg-white/5 border border-white/10 rounded-2xl p-12 text-center">
      <i className="bi bi-inbox text-4xl text-white/30 mb-4 block"></i>
      <h3 className="text-white font-semibold mb-2">{title}</h3>
      <p className="text-white/40 text-sm">{message}</p>
    </div>
  );
}

export default function MotoristaPerformancePage() {
  const { data, loading } = usePerformanceMotorista();

  const hasData = data && data.length > 0;

  const totais: Totais = hasData
    ? data.reduce(
        (acc: Totais, d: PerformanceData) => ({
          entregas: acc.entregas + d.totalEntregas,
          emAndamento: acc.emAndamento + d.emAndamento,
          taxaMedia: acc.taxaMedia + d.taxaSucesso,
        }),
        { entregas: 0, emAndamento: 0, taxaMedia: 0 }
      )
    : { entregas: 0, emAndamento: 0, taxaMedia: 0 };

  const count = data?.length || 1;
  const taxaMediaFinal = (totais.taxaMedia / count).toFixed(1);

  const lineData: MotoristaEntregaDia[] = hasData
    ? data.map((d: PerformanceData) => ({
        mes: d.mes,
        entregas: d.totalEntregas,
        canceladas: Math.round(d.totalEntregas * (1 - d.taxaSucesso / 100)),
      }))
    : [];

  return (
    <div className="space-y-6 max-w-4xl">
      <div>
        <h2 className="text-2xl font-bold text-white mb-1">Minha Performance</h2>
        <p className="text-white/40 text-sm">Histórico e indicadores pessoais de entrega.</p>
      </div>

      {loading ? (
        <LoadingSkeleton count={3} />
      ) : (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <KpiCard
              title="Total entregue"
              value={totais.entregas}
              icon={<Truck size={24} />}
              backgroundColor="bg-white"
              iconBackgroundColor="bg-blue-100"
              textColor="text-blue-600"
            />
            <KpiCard
              title="Taxa de sucesso"
              value={`${taxaMediaFinal}%`}
              icon={<Award size={24} />}
              backgroundColor="bg-white"
              iconBackgroundColor="bg-green-100"
              textColor="text-green-600"
            />
            <KpiCard
              title="Em andamento"
              value={totais.emAndamento}
              icon={<RotateCw size={24} />}
              backgroundColor="bg-white"
              iconBackgroundColor="bg-amber-100"
              textColor="text-amber-600"
            />
          </div>

          {hasData && lineData.length > 0 ? (
            <MotoristaEntregasLineChart data={lineData} />
          ) : (
            <EmptyState 
              title="Nenhuma entrega" 
              message="Você ainda não possui histórico de entregas registrado."
            />
          )}
        </>
      )}
    </div>
  );
}