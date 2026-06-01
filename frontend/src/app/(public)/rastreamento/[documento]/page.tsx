"use client";
import { use } from "react";
import Link from "next/link";
import { LogoPreta } from "@/shared/components/ui/LogoPreta";
import { useRastreamento } from "@/features/rastreamento/hooks/useRastreamento";
import { Footer } from "@/features/landing/components/Footer";

interface Props {
    params: Promise<{ documento: string }>;
}

const statusColors: Record<string, { bg: string; border: string; text: string }> = {
    EMITIDO: { bg: "bg-blue-50", border: "border-blue-200", text: "text-blue-700" },
    SAIDA_CONFIRMADA: { bg: "bg-yellow-50", border: "border-yellow-200", text: "text-yellow-700" },
    EM_TRANSITO: { bg: "bg-purple-50", border: "border-purple-200", text: "text-purple-700" },
    ENTREGUE: { bg: "bg-green-50", border: "border-green-200", text: "text-green-700" },
    NAO_ENTREGUE: { bg: "bg-red-50", border: "border-red-200", text: "text-red-700" },
    CANCELADO: { bg: "bg-gray-50", border: "border-gray-200", text: "text-gray-700" },
};

const statusLabels: Record<string, string> = {
    EMITIDO: "Emitido",
    SAIDA_CONFIRMADA: "Saída Confirmada",
    EM_TRANSITO: "Em Trânsito",
    ENTREGUE: "Entregue",
    NAO_ENTREGUE: "Não Entregue",
    CANCELADO: "Cancelado",
};

export default function RastreamentoPage({ params }: Props) {
    const { documento } = use(params);
    const { data, loading, error } = useRastreamento(documento);

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <header className="bg-white border-b border-gray-200 py-4 px-6">
                <div className="max-w-5xl mx-auto flex items-center justify-between">
                    <LogoPreta />
                    <Link
                        href="/"
                        className="flex items-center gap-2 text-gray-700 hover:text-gray-900 transition-colors font-medium"
                    >
                        Página Inicial
                        <i className="bi bi-arrow-right text-lg" />
                    </Link>
                </div>
            </header>

            <main className="flex-1 pt-12 pb-16">
                <div className="max-w-5xl mx-auto px-6">
                    <div className="mb-8">
                        <span className="text-sm font-semibold uppercase tracking-widest text-blue-600 block mb-2">
                            Rastreamento
                        </span>
                        <h1 className="text-4xl font-bold text-gray-900">
                            Frete: <span className="text-blue-600">{documento.toUpperCase()}</span>
                        </h1>
                    </div>

                    {loading && (
                        <div className="flex flex-col items-center justify-center py-20">
                            <svg
                                width="80"
                                height="80"
                                viewBox="0 0 80 80"
                                fill="none"
                                xmlns="http://www.w3.org/2000/svg"
                                className="mb-4"
                            >
                                <circle cx="40" cy="40" r="35" stroke="#e5e7eb" strokeWidth="3" />
                                <circle
                                    cx="40"
                                    cy="40"
                                    r="35"
                                    stroke="#2563eb"
                                    strokeWidth="3"
                                    strokeDasharray="55 165"
                                    strokeLinecap="round"
                                    style={{
                                        animation: "spin 1.5s linear infinite",
                                    }}
                                />
                                <style>{`
                                    @keyframes spin {
                                        to { transform: rotate(360deg); }
                                    }
                                `}</style>
                            </svg>
                            <p className="text-gray-600 text-lg">Buscando informações do frete...</p>
                        </div>
                    )}

                    {error && (
                        <div className="bg-red-50 border border-red-200 rounded-lg p-6 text-center">
                            <i className="bi bi-exclamation-circle text-red-600 text-4xl mb-3 block" />
                            <p className="text-red-700 font-medium text-lg">{error}</p>
                            <p className="text-red-600 text-sm mt-2">
                                Verifique o número do frete e tente novamente.
                            </p>
                        </div>
                    )}

                    {!loading && !error && !data && (
                        <div className="bg-gray-100 border border-gray-300 rounded-lg p-12 text-center">
                            <i className="bi bi-search text-gray-400 text-5xl mb-4 block" />
                            <p className="text-gray-600 text-lg">Nenhuma entrega encontrada para este número.</p>
                        </div>
                    )}

                    {!loading && !error && data && (
                        <div className="space-y-6">
                            <div className="bg-white border border-gray-200 rounded-xl shadow-sm overflow-hidden">
                                <div className="p-8 border-b border-gray-200">
                                    <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                                        <div>
                                            <p className="text-sm font-medium text-gray-600 mb-2">Nº do Frete</p>
                                            <p className="text-2xl font-bold text-gray-900">{data.frete.numero}</p>
                                        </div>
                                        <div
                                            className={`px-6 py-3 rounded-lg border ${
                                                statusColors[data.frete.status]?.bg || "bg-gray-50"
                                            } ${statusColors[data.frete.status]?.border || "border-gray-200"}`}
                                        >
                                            <p
                                                className={`font-semibold ${
                                                    statusColors[data.frete.status]?.text || "text-gray-700"
                                                }`}
                                            >
                                                {statusLabels[data.frete.status] || data.frete.status}
                                            </p>
                                        </div>
                                    </div>
                                </div>

                                <div className="p-8">
                                    <div className="grid grid-cols-2 md:grid-cols-4 gap-6 mb-8">
                                        {[
                                            { label: "Remetente", value: data.frete.remetente },
                                            { label: "Destinatário", value: data.frete.destinatario },
                                            {
                                                label: "Origem",
                                                value: `${data.frete.municipioOrigem}/${data.frete.ufOrigem}`,
                                            },
                                            {
                                                label: "Destino",
                                                value: `${data.frete.municipioDestino}/${data.frete.ufDestino}`,
                                            },
                                            { label: "Emitido em", value: data.frete.dataEmissao },
                                            { label: "Saída", value: data.frete.dataSaida || "-" },
                                            { label: "Previsão de Entrega", value: data.frete.dataPrevisaoEntrega },
                                            {
                                                label: "Entregue em",
                                                value: data.frete.dataEntrega || "-",
                                            },
                                        ].map(({ label, value }) => (
                                            <div key={label}>
                                                <p className="text-xs font-semibold text-gray-600 mb-1 uppercase tracking-wide">
                                                    {label}
                                                </p>
                                                <p className="text-sm font-medium text-gray-900">{value}</p>
                                            </div>
                                        ))}
                                    </div>

                                    {data.ocorrencias && data.ocorrencias.length > 0 && (
                                        <div className="border-t border-gray-200 pt-8">
                                            <h3 className="text-lg font-bold text-gray-900 mb-6">Histórico de Eventos</h3>
                                            <div className="max-h-96 overflow-y-auto pr-4 scroll-smooth">
                                                <style>{`
                                                    .scroll-smooth {
                                                        scroll-behavior: smooth;
                                                    }
                                                    .scroll-smooth::-webkit-scrollbar {
                                                        width: 6px;
                                                    }
                                                    .scroll-smooth::-webkit-scrollbar-track {
                                                        background: #f3f4f6;
                                                        border-radius: 10px;
                                                    }
                                                    .scroll-smooth::-webkit-scrollbar-thumb {
                                                        background: #cbd5e1;
                                                        border-radius: 10px;
                                                    }
                                                    .scroll-smooth::-webkit-scrollbar-thumb:hover {
                                                        background: #94a3b8;
                                                    }
                                                `}</style>
                                                <div className="space-y-4">
                                                    {data.ocorrencias.map((ocorrencia, idx) => (
                                                        <div
                                                            key={idx}
                                                            className="flex gap-4 animate-fade-in"
                                                            style={{
                                                                animation: `fadeIn 0.5s ease-in-out ${idx * 0.1}s both`,
                                                            }}
                                                        >
                                                            <style>{`
                                                                @keyframes fadeIn {
                                                                    from {
                                                                        opacity: 0;
                                                                        transform: translateY(10px);
                                                                    }
                                                                    to {
                                                                        opacity: 1;
                                                                        transform: translateY(0);
                                                                    }
                                                                }
                                                            `}</style>
                                                            <div className="flex flex-col items-center">
                                                                <div className="w-3 h-3 rounded-full bg-blue-600" />
                                                                {idx < data.ocorrencias.length - 1 && (
                                                                    <div className="w-0.5 h-12 bg-gray-300 mt-2" />
                                                                )}
                                                            </div>
                                                            <div className="flex-1 pb-4">
                                                                <p className="font-semibold text-gray-900">
                                                                    {ocorrencia.tipo
                                                                        .replace(/_/g, " ")
                                                                        .charAt(0)
                                                                        .toUpperCase() +
                                                                        ocorrencia.tipo
                                                                            .replace(/_/g, " ")
                                                                            .slice(1)
                                                                            .toLowerCase()}
                                                                </p>
                                                                <p className="text-sm text-gray-600 mt-1">
                                                                    {ocorrencia.dataHora}
                                                                </p>
                                                                <p className="text-sm text-gray-600">
                                                                    {ocorrencia.municipio}/{ocorrencia.uf}
                                                                </p>
                                                                {ocorrencia.descricao && (
                                                                    <p className="text-sm text-gray-700 mt-2">
                                                                        {ocorrencia.descricao}
                                                                    </p>
                                                                )}
                                                            </div>
                                                        </div>
                                                    ))}
                                                </div>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    )}
                </div>
            </main>

            <Footer />
        </div>
    );
}