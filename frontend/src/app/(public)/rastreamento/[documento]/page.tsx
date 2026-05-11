"use client";

import { use } from "react";
import { Navbar } from "@/features/landing/components/Navbar";
import { Footer } from "@/features/landing/components/Footer";
import { ProgressTimeline } from "@/features/rastreamento/components/ProgressTimeline";
import { StatusBadge } from "@/features/rastreamento/components/StatusBadge";
import { useRastreamento } from "@/features/rastreamento/hooks/useRastreamento";
import { formatDate, formatDocumento } from "@/core/utils/formatters";

interface Props {
  params: Promise<{ documento: string }>;
}

export default function RastreamentoPage({ params }: Props) {
  const { documento } = use(params);
  const { data, loading, error } = useRastreamento(documento);

  return (
    <>
      <Navbar />
      <main className="min-h-screen bg-[#050f1a] pt-24 pb-16">
        <div className="max-w-4xl mx-auto px-6">
          <div className="mb-10">
            <span className="text-xs font-bold uppercase tracking-widest text-accent mb-2 block">
              Rastreamento
            </span>
            <h1 className="text-3xl font-bold text-white mb-1">
              Documento:{" "}
              <span className="text-white/60">{formatDocumento(documento)}</span>
            </h1>
          </div>

          {loading && (
            <div className="space-y-4">
              {[1, 2].map((i) => (
                <div
                  key={i}
                  className="h-40 rounded-2xl bg-white/5 animate-pulse"
                />
              ))}
            </div>
          )}

          {error && (
            <div className="bg-red-500/10 border border-red-500/20 rounded-2xl p-8 text-center">
              <i className="bi bi-exclamation-circle text-red-400 text-4xl mb-3 block" />
              <p className="text-red-400 font-medium">{error}</p>
            </div>
          )}

          {!loading && !error && data.length === 0 && (
            <div className="bg-white/5 border border-white/10 rounded-2xl p-12 text-center">
              <i className="bi bi-search text-white/20 text-5xl mb-4 block" />
              <p className="text-white/40">Nenhuma entrega encontrada para este documento.</p>
            </div>
          )}

          <div className="space-y-6">
            {data.map((entrega) => (
              <div
                key={entrega.numero}
                className="bg-white/5 border border-white/8 rounded-2xl overflow-hidden"
              >
                <div className="p-6 border-b border-white/8 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                  <div>
                    <p className="text-xs text-white/40 mb-1">Nº do envio</p>
                    <p className="text-lg font-bold text-white">{entrega.numero}</p>
                  </div>
                  <StatusBadge status={entrega.status} />
                </div>

                <div className="p-6 space-y-6">
                  <ProgressTimeline status={entrega.status} />

                  <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
                    {[
                      { label: "Origem", value: `${entrega.municipioOrigem}/${entrega.ufOrigem}` },
                      { label: "Destino", value: `${entrega.municipioDestino}/${entrega.ufDestino}` },
                      { label: "Emitido em", value: formatDate(entrega.dataEmissao) },
                      {
                        label: entrega.dataEntrega ? "Entregue em" : "Previsão",
                        value: formatDate(entrega.dataEntrega ?? entrega.dataPrevisaoEntrega),
                      },
                    ].map(({ label, value }) => (
                      <div key={label}>
                        <p className="text-xs text-white/30 mb-0.5">{label}</p>
                        <p className="text-sm font-semibold text-white/80">{value}</p>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </main>
      <Footer />
    </>
  );
}