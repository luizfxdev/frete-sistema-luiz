"use client";

import { useState } from "react";
import { responderSolicitacao, type SolicitacaoTransporte } from "@/features/motorista/adapters/motoristaService";

interface Props {
  solicitacoes: SolicitacaoTransporte[];
  onResposta: () => void;
}

const STATUS_CFG: Record<string, { label: string; color: string }> = {
  PENDENTE: { label: "Pendente", color: "text-yellow-400 bg-yellow-400/10" },
  ACEITA: { label: "Aceita", color: "text-success bg-success/10" },
  RECUSADA: { label: "Recusada", color: "text-red-400 bg-red-400/10" },
};

export function SolicitacoesTable({ solicitacoes, onResposta }: Props) {
  const [processando, setProcessando] = useState<number | null>(null);

  const responder = async (id: number, acao: "ACEITAR" | "RECUSAR") => {
    setProcessando(id);
    try {
      await responderSolicitacao(id, acao);
      onResposta();
    } finally {
      setProcessando(null);
    }
  };

  if (!solicitacoes.length) {
    return (
      <div className="bg-white/5 border border-white/7 rounded-2xl py-14 text-center">
        <i className="bi bi-inbox text-white/20 text-4xl mb-3 block" />
        <p className="text-white/30 text-sm">Nenhuma solicitação de transporte no momento.</p>
      </div>
    );
  }

  return (
    <div className="bg-white/5 border border-white/7 rounded-2xl overflow-hidden">
      <div className="px-6 py-4 border-b border-white/7">
        <h3 className="text-sm font-bold text-white">Solicitações de Transporte</h3>
      </div>
      <div className="divide-y divide-white/5">
        {solicitacoes.map((s) => {
          const st = STATUS_CFG[s.status] ?? { label: s.status, color: "text-white/40" };
          const isPendente = s.status === "PENDENTE";
          return (
            <div key={s.id} className="px-6 py-4 flex flex-col sm:flex-row sm:items-center gap-4 hover:bg-white/3 transition-colors">
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2 mb-1">
                  <i className="bi bi-geo-alt-fill text-accent text-xs" />
                  <p className="text-sm font-semibold text-white">
                    {s.municipioColeta}/{s.ufColeta}
                    <span className="text-white/30 mx-2">→</span>
                    {s.municipioDestino}/{s.ufDestino}
                  </p>
                </div>
                <p className="text-xs text-white/40">{s.tipoCarga}</p>
              </div>
              <div className="flex items-center gap-3 flex-shrink-0">
                <span className={`text-[11px] font-bold px-2.5 py-1 rounded-full ${st.color}`}>
                  {st.label}
                </span>
                {isPendente && (
                  <>
                    <button
                      onClick={() => responder(s.id, "ACEITAR")}
                      disabled={processando === s.id}
                      className="flex items-center gap-1.5 px-3 py-1.5 bg-success text-white text-xs font-bold rounded-lg hover:bg-success-dark transition-all disabled:opacity-50"
                    >
                      <i className="bi bi-check-lg" />
                      Aceitar
                    </button>
                    <button
                      onClick={() => responder(s.id, "RECUSAR")}
                      disabled={processando === s.id}
                      className="flex items-center gap-1.5 px-3 py-1.5 border border-red-400/30 text-red-400 text-xs font-bold rounded-lg hover:bg-red-400/10 transition-all disabled:opacity-50"
                    >
                      <i className="bi bi-x-lg" />
                      Recusar
                    </button>
                  </>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}