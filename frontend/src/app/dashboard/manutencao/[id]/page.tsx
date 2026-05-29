'use client';

import { useRouter, useParams } from 'next/navigation';
import { useState } from 'react';
import { ArrowLeft, Download, Check, X } from 'lucide-react';
import { useManutencaoDetalhe } from '@/features/manutencao/hooks/useManutencao';
import { ManutencaoDetalhes } from '@/features/manutencao/components/ManutencaoDetalhes';
import { OrcamentoTable } from '@/features/manutencao/components/OrcamentoTable';

function Skeleton({ h = 'h-32' }: { h?: string }) {
  return (
    <div className={`${h} rounded-2xl bg-gradient-to-r from-gray-200 to-gray-100 animate-pulse`} />
  );
}

export default function ManutencaoDetalhePage() {
  const router = useRouter();
  const params = useParams();
  const id = typeof params.id === 'string' ? parseInt(params.id) : 0;

  const { manutencao, itens, loading, error, salvar, liberar, cancelar } =
    useManutencaoDetalhe(id);
  const [isLiberando, setIsLiberando] = useState(false);
  const [isCancelando, setIsCancelando] = useState(false);
  const [showCancelConfirm, setShowCancelConfirm] = useState(false);

  const handleLiberar = async () => {
    if (!manutencao) return;
    if (manutencao.statusManutencao === 'CONCLUIDA') {
      alert('Manutenção já foi liberada');
      return;
    }

    setIsLiberando(true);
    try {
      await liberar();
      alert('Veículo liberado com sucesso!');
    } catch (err) {
      alert('Erro ao liberar: ' + (err instanceof Error ? err.message : 'Desconhecido'));
    } finally {
      setIsLiberando(false);
    }
  };

  const handleCancelar = async () => {
    setIsCancelando(true);
    try {
      await cancelar();
      alert('Manutenção cancelada com sucesso!');
      setShowCancelConfirm(false);
    } catch (err) {
      alert('Erro ao cancelar: ' + (err instanceof Error ? err.message : 'Desconhecido'));
    } finally {
      setIsCancelando(false);
    }
  };

  const handlePrint = () => {
    window.print();
  };

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-red-50">
        <div className="text-center">
          <p className="text-lg text-gray-900 font-semibold mb-4">{error}</p>
          <button
            onClick={() => router.back()}
            className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
          >
            Voltar
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen" style={{ backgroundColor: '#f5f5f5' }}>
      <style>{`
        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(10px); }
          to { opacity: 1; transform: translateY(0); }
        }
        .animate-fadeIn { animation: fadeIn 0.5s ease-out forwards; }
        @media print {
          .no-print { display: none; }
        }
      `}</style>

      {/* Header */}
      <div className="bg-white border-b border-gray-100 sticky top-0 z-50 no-print">
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
              <h1 className="text-2xl font-bold text-gray-900">
                {loading ? 'Carregando...' : `Manutenção - ${manutencao?.placa}`}
              </h1>
              <p className="text-sm text-gray-500">Detalhes e orçamentos</p>
            </div>
          </div>

          <div className="flex gap-2">
            {manutencao && (
              <>
                <button
                  onClick={handlePrint}
                  className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
                  title="Imprimir/PDF"
                >
                  <Download size={20} className="text-gray-600" />
                </button>

                {manutencao.statusManutencao === 'EM_ANDAMENTO' && (
                  <>
                    <button
                      onClick={handleLiberar}
                      disabled={isLiberando}
                      className="flex items-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:opacity-50 transition-colors"
                    >
                      <Check size={16} />
                      {isLiberando ? 'Liberando...' : 'Liberar Veículo'}
                    </button>

                    <button
                      onClick={() => setShowCancelConfirm(true)}
                      className="flex items-center gap-2 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
                    >
                      <X size={16} />
                      Cancelar
                    </button>
                  </>
                )}
              </>
            )}
          </div>
        </div>
      </div>

      {/* Main Content */}
      <main className="w-full px-8 py-8 pb-12">
        {loading ? (
          <div className="space-y-6">
            <Skeleton h="h-48" />
            <Skeleton h="h-96" />
          </div>
        ) : manutencao ? (
          <div className="space-y-8 animate-fadeIn">
            {/* Dados da Manutenção */}
            <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
              <h2 className="text-xl font-bold text-gray-900 mb-6">
                Informações da Manutenção
              </h2>
              <ManutencaoDetalhes manutencao={manutencao} />
            </div>

            {/* Orçamentos */}
            <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
              <h2 className="text-xl font-bold text-gray-900 mb-6">Orçamentos</h2>
              <OrcamentoTable itens={itens} onSalvar={salvar} />
            </div>
          </div>
        ) : null}
      </main>

      {/* Confirm Cancel Dialog */}
      {showCancelConfirm && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl shadow-xl max-w-md w-full p-6">
            <h3 className="text-lg font-bold text-gray-900 mb-2">
              Cancelar Manutenção?
            </h3>
            <p className="text-gray-600 mb-6">
              Esta ação não pode ser desfeita. Deseja continuar?
            </p>
            <div className="flex gap-3">
              <button
                onClick={() => setShowCancelConfirm(false)}
                disabled={isCancelando}
                className="flex-1 px-4 py-2 border border-gray-300 rounded-lg text-gray-700 font-semibold hover:bg-gray-50 disabled:opacity-50"
              >
                Não
              </button>
              <button
                onClick={handleCancelar}
                disabled={isCancelando}
                className="flex-1 px-4 py-2 bg-red-600 text-white rounded-lg font-semibold hover:bg-red-700 disabled:opacity-50"
              >
                {isCancelando ? 'Cancelando...' : 'Sim, Cancelar'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}