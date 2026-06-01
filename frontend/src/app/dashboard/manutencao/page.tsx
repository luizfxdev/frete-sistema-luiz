'use client';

import { useRouter } from 'next/navigation';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { ArrowLeft } from 'lucide-react';
import { useManutencaoKpis, useManutencoes } from '@/features/manutencao/hooks/useManutencao';
import { ManutencaoKpiCards } from '@/features/manutencao/components/ManutencaoKpiCards';
import { ManutencaoTable } from '@/features/manutencao/components/ManutencaoTable';

interface SkeletonProps {
  h?: string;
}

function Skeleton({ h = 'h-32' }: SkeletonProps) {
  return (
    <div
      className={`${h} rounded-2xl bg-gradient-to-r from-gray-200 to-gray-100 animate-pulse`}
    />
  );
}

export default function ManutencaoPage() {
  const router = useRouter();
  const { kpis, loading: loadingKpis } = useManutencaoKpis();
  const { data, loading } = useManutencoes();

  const dataAtualFormatada = format(new Date(), "EEEE, d 'de' MMMM 'de' yyyy", {
    locale: ptBR,
  });

  return (
    <div className="min-h-screen" style={{ backgroundColor: '#f5f5f5' }}>
      <style>{`
        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(10px); }
          to { opacity: 1; transform: translateY(0); }
        }
        .animate-fadeIn { animation: fadeIn 0.5s ease-out forwards; }
      `}</style>

      {/* Header Sticky */}
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
              <h1 className="text-2xl font-bold text-gray-900">Manutenção de Frota</h1>
              <p className="text-sm text-gray-500">Controle preventivo e corretivo dos veículos</p>
            </div>
          </div>

          <button
            onClick={() => router.push('/dashboard')}
            className="px-4 py-2 rounded-lg text-sm font-semibold text-white transition-all hover:opacity-90"
            style={{ backgroundColor: '#292929' }}
            aria-label="Menu Principal"
          >
            Menu Principal
          </button>
        </div>
      </div>

      {/* Main Content */}
      <main className="w-full px-8 py-8 pb-12">
        {/* Data */}
        <div className="mb-8 flex justify-end">
          <p className="text-sm text-gray-500 capitalize">{dataAtualFormatada}</p>
        </div>

        {/* KPI Cards */}
        <div className="mb-8">
          {loadingKpis ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
              {Array.from({ length: 4 }).map((_, i) => (
                <Skeleton key={i} h="h-32" />
              ))}
            </div>
          ) : kpis ? (
            <ManutencaoKpiCards kpis={kpis} />
          ) : null}
        </div>

        {/* Tabela */}
        {loading ? (
          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
            <Skeleton h="h-96" />
          </div>
        ) : (
          <ManutencaoTable data={data} loading={loading} />
        )}
      </main>
    </div>
  );
}