"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { format, subDays } from "date-fns";
import { ptBR } from "date-fns/locale";
import { ArrowLeft } from "lucide-react";
import { usePerformance } from "@/features/performance/hooks/usePerformance";
import {
  EntregasLineChart,
  StatusPieChart,
  VolumeAreaChart,
  TaxaSucessoBarChart,
  RegioesBarChart,
  TopMotoristasCard,
  UltimosFretesTable,
} from "@/features/performance/components/PerformanceChart";

const PERIODOS = [
  { label: "7 dias", days: 7 },
  { label: "30 dias", days: 30 },
  { label: "90 dias", days: 90 },
  { label: "12 meses", days: 365 },
];

interface SkeletonProps {
  h?: string;
}

function Skeleton({ h = "h-80" }: SkeletonProps) {
  return (
    <div className={`${h} rounded-2xl bg-gradient-to-r from-gray-200 to-gray-100 animate-pulse`} />
  );
}

interface KpiCardProps {
  label: string;
  value: string | number;
  icon: string;
  color: string;
  trend?: string;
  trendUp?: boolean;
}

function KpiCard({ label, value, icon, color, trend, trendUp }: KpiCardProps) {
  return (
    <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between mb-4">
        <div
          className="w-12 h-12 rounded-xl flex items-center justify-center"
          style={{ backgroundColor: `${color}15` }}
        >
          <i className={`bi ${icon} text-xl`} style={{ color }}></i>
        </div>
        {trend && (
          <span className={`text-sm font-semibold ${trendUp ? "text-green-600" : "text-red-600"}`}>
            {trendUp ? "↑" : "↓"} {trend}
          </span>
        )}
      </div>
      <p className="text-gray-600 text-sm mb-2">{label}</p>
      <p className="text-2xl font-bold text-gray-900">{value}</p>
    </div>
  );
}

export default function PerformancePage() {
  const router = useRouter();
  const [periodoIdx, setPeriodoIdx] = useState(1);

  const dataFim = format(new Date(), "yyyy-MM-dd");
  const dataInicio = format(subDays(new Date(), PERIODOS[periodoIdx].days), "yyyy-MM-dd");

  const { data, loading, error } = usePerformance(dataInicio, dataFim);

  const dataAtualFormatada = format(new Date(), "EEEE, d 'de' MMMM 'de' yyyy", { locale: ptBR });

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-red-50">
        <div className="text-center">
          <i className="bi bi-exclamation-circle text-5xl text-red-500 mb-4"></i>
          <p className="text-lg text-gray-900 font-semibold">{error}</p>
          <button
            onClick={() => router.back()}
            className="mt-4 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
          >
            Voltar
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen" style={{ backgroundColor: "#f5f5f5" }}>
      <style>{`
        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(10px); }
          to { opacity: 1; transform: translateY(0); }
        }
        .animate-fadeIn { animation: fadeIn 0.5s ease-out forwards; }
      `}</style>

      <div className="bg-white border-b border-gray-100 sticky top-0 z-50">
        <div className="w-full px-8 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <button
              onClick={() => router.back()}
              className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
              aria-label="Voltar"
            >
              <ArrowLeft size={20} className="text-gray-600" />
            </button>
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Performance</h1>
              <p className="text-sm text-gray-500">Dashboard operacional</p>
            </div>
          </div>

          <button
            onClick={() => router.push("/dashboard")}
            className="px-4 py-2 rounded-lg text-sm font-semibold text-white transition-all hover:opacity-90"
            style={{ backgroundColor: "#292929" }}
            aria-label="Menu Principal"
          >
            Menu Principal
          </button>
        </div>
      </div>

      <main className="w-full px-8 py-8 pb-12">
        <div className="mb-8 flex justify-between items-center">
          <div className="flex gap-2 bg-white p-1 rounded-xl border border-gray-200">
            {PERIODOS.map(({ label }, i) => (
              <button
                key={label}
                onClick={() => setPeriodoIdx(i)}
                className="px-4 py-2 rounded-lg text-sm font-semibold transition-all"
                style={{
                  backgroundColor: periodoIdx === i ? "#292929" : "transparent",
                  color: periodoIdx === i ? "#fff" : "#666",
                }}
                aria-pressed={periodoIdx === i}
              >
                {label}
              </button>
            ))}
          </div>
          <p className="text-sm text-gray-500 capitalize">{dataAtualFormatada}</p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          {loading
            ? Array.from({ length: 4 }).map((_, i) => <Skeleton key={i} h="h-32" />)
            : data?.kpis
            ? (
              <>
                <KpiCard label="Total de Fretes" value={data.kpis.totalFretes?.toLocaleString("pt-BR") || "0"} icon="bi-truck-front-fill" color="#005eff" trend="12%" trendUp />
                <KpiCard label="Entregues" value={data.kpis.fretesEntregues?.toLocaleString("pt-BR") || "0"} icon="bi-check-circle-fill" color="#10b981" trend="8%" trendUp />
                <KpiCard label="Em Andamento" value={data.kpis.fretesEmAndamento?.toLocaleString("pt-BR") || "0"} icon="bi-arrow-repeat" color="#f59e0b" />
                <KpiCard label="Taxa de Pontualidade" value={`${data.kpis.taxaPontualidade || 0}%`} icon="bi-clock-fill" color="#22c55e" trend="3%" trendUp />
              </>
            )
            : null}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
          {loading ? <Skeleton h="h-80" /> : data ? <div className="lg:col-span-2"><EntregasLineChart data={data} /></div> : null}
          {loading ? <Skeleton h="h-80" /> : data ? <StatusPieChart data={data} /> : null}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
          {loading ? <Skeleton /> : data ? <VolumeAreaChart data={data} /> : null}
          {loading ? <Skeleton /> : data ? <TaxaSucessoBarChart data={data} /> : null}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
          {loading ? <Skeleton /> : data ? <RegioesBarChart data={data} /> : null}
          {loading ? <Skeleton /> : data ? <TopMotoristasCard data={data} /> : null}
        </div>

        {loading ? <Skeleton h="h-96" /> : data ? <UltimosFretesTable data={data} /> : null}
      </main>
    </div>
  );
}