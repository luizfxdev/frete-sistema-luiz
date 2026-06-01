"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";

export function RastrearForm() {
    const router = useRouter();
    const [numero, setNumero] = useState("");
    const [carregando, setCarregando] = useState(false);
    const [erro, setErro] = useState("");

    const validarFormatoNumero = (valor: string): boolean => {
        return /^FRT-\d{4}-\d{5}$/.test(valor.trim());
    };

    const obterMensagemErro = (valor: string): string => {
        const clean = valor.trim().toUpperCase();

        if (clean.length === 0) {
            return "Digite o número do frete para continuar";
        }

        if (!clean.startsWith("FRT")) {
            return "O número deve começar com FRT (ex: FRT-2026-00001)";
        }

        if (clean.length < 16) {
            return `Digite o número completo: FRT-AAAA-NNNNN (${16 - clean.length} caracteres faltando)`;
        }

        if (!clean.includes("-")) {
            return "Use hífens no formato: FRT-AAAA-NNNNN";
        }

        if (!clean.match(/^\d{4}/)) {
            return "Após FRT-, use 4 dígitos para o ano (ex: FRT-2026-00001)";
        }

        if (!clean.match(/-\d{5}$/)) {
            return "Após FRT-AAAA-, use 5 dígitos para a sequência (ex: FRT-2026-00001)";
        }

        return "Formato inválido. Use: FRT-AAAA-NNNNN (ex: FRT-2026-00001)";
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setNumero(e.target.value);
        setErro("");
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setErro("");

        if (!validarFormatoNumero(numero)) {
            setErro(obterMensagemErro(numero));
            return;
        }

        setCarregando(true);

        try {
            const response = await fetch(
                `http://localhost:8080/nextlog/api/rastreamento/${numero.trim().toUpperCase()}`,
                { method: "GET", headers: { "Content-Type": "application/json" } }
            );

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                setErro(errorData.mensagem || "Frete não encontrado. Verifique o número e tente novamente.");
                setCarregando(false);
                return;
            }

            router.push(`/rastreamento/${numero.trim().toUpperCase()}`);
        } catch (err) {
            setErro("Erro ao conectar com o servidor. Tente novamente em alguns instantes.");
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
                    Digite o número do seu frete para rastrear a entrega
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
                        <i className="bi bi-box-seam absolute left-4 top-1/2 -translate-y-1/2 text-white/30 text-lg" />
                        <input
                            type="text"
                            placeholder="FRT-2026-00001"
                            value={numero}
                            onChange={handleInputChange}
                            maxLength={20}
                            className="w-full bg-transparent pl-11 pr-4 py-3.5 text-white placeholder-white/30 outline-none text-sm uppercase"
                            required
                            disabled={carregando}
                        />
                    </div>
                    <button
                        type="submit"
                        disabled={carregando}
                        className="flex items-center justify-center gap-2 px-6 py-3.5 bg-accent text-white font-semibold rounded-xl hover:bg-accent-dark transition-all hover:-translate-y-0.5 hover:shadow-lg hover:shadow-accent/40 text-sm whitespace-nowrap disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        <i className="bi bi-box-seam" />
                        {carregando ? "Buscando..." : "Rastrear"}
                    </button>
                </form>

                <p className="text-xs text-white/30 mt-6">
                    💡 O número do frete está na sua nota fiscal ou confirmação de entrega
                </p>
            </div>
        </section>
    );
}