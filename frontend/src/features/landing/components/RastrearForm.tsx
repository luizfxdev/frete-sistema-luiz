"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { stripMask } from "@/core/utils/formatters";

export function RastrearForm() {
    const router = useRouter();
    const [documento, setDocumento] = useState("");
    const [carregando, setCarregando] = useState(false);
    const [erro, setErro] = useState("");

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setErro("");
        
        const clean = stripMask(documento);
        if (clean.length < 11) {
            setErro("CPF ou CNPJ inválido");
            return;
        }

        setCarregando(true);
        
        try {
            const response = await fetch(`http://localhost:8080/nextlog/api/rastreamento/buscar?documento=${clean}`);
            const data = await response.json();

            if (!response.ok) {
                setErro(data.mensagem || "Nenhuma encomenda encontrada para este CPF/CNPJ.");
                setCarregando(false);
                return;
            }

            router.push(`/rastreamento/${clean}`);
        } catch (err) {
            setErro("Erro ao conectar com o servidor");
            setCarregando(false);
        }
    };

    return (
        <section id="rastrear" className="py-28 bg-brand-900 relative overflow-hidden">
            <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_center,rgba(0,94,255,0.12)_0%,transparent_70%)] pointer-events-none" />
            <div className="max-w-3xl mx-auto px-6 text-center relative z-10">
                <span className="text-xs font-bold uppercase tracking-widest text-accent mb-3 block">
                    Rastreamento
                </span>
                <h2
                    className="text-4xl lg:text-5xl font-bold text-white mb-4"
                    style={{ fontFamily: "Avenir Next, -apple-system, sans-serif" }}
                >
                    Onde está sua encomenda?
                </h2>
                <p className="text-white/50 mb-10">
                    Informe seu CPF ou CNPJ para acompanhar suas entregas
                </p>
                
                {erro && (
                    <div className="bg-red-500/20 border border-red-500/50 text-red-200 rounded-lg px-4 py-3 mb-6 text-sm">
                        {erro}
                    </div>
                )}

                <form
                    onSubmit={handleSubmit}
                    className="flex flex-col sm:flex-row gap-3 bg-white/5 border border-white/10 backdrop-blur-md rounded-2xl p-3"
                >
                    <div className="relative flex-1">
                        <i className="bi bi-bullseye absolute left-4 top-1/2 -translate-y-1/2 text-white/30 text-lg" />
                        <input
                            type="text"
                            placeholder="Digite seu CPF ou CNPJ"
                            value={documento}
                            onChange={(e) => {
                                setDocumento(e.target.value);
                                setErro("");
                            }}
                            maxLength={18}
                            className="w-full bg-transparent pl-11 pr-4 py-3.5 text-white placeholder-white/30 outline-none text-sm"
                            required
                            disabled={carregando}
                        />
                    </div>
                    <button
                        type="submit"
                        disabled={carregando}
                        className="flex items-center justify-center gap-2 px-6 py-3.5 bg-accent text-white font-semibold rounded-xl hover:bg-accent-dark transition-all hover:-translate-y-0.5 hover:shadow-lg hover:shadow-accent/40 text-sm whitespace-nowrap disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        <i className="bi bi-bullseye" />
                        {carregando ? "Buscando..." : "Rastrear"}
                    </button>
                </form>
            </div>
        </section>
    );
}
