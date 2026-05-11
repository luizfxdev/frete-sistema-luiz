import { Navbar } from "@/features/landing/components/Navbar";
import { Footer } from "@/features/landing/components/Footer";
import { CotacaoForm } from "@/features/cotacao/components/CotacaoForm";
import { LogoBranca } from "@/shared/components/ui/LogoBranca";
import Link from "next/link";
import type { Metadata } from "next";

export const metadata: Metadata = { title: "Cotação de Frete" };
 
export default function CotacaoPage() {
  return (
    <div
      className="min-h-screen relative"
      style={{
        backgroundImage: "url('/assets/backgrounds/backgroundcotacao.png')",
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundAttachment: "fixed",
      }}
    >
      <div className="absolute inset-0" style={{ backgroundColor: "rgba(5,15,26,0.65)" }} />
 
      <div className="relative z-10 min-h-screen flex flex-col">
        {/* Header com logo e link home */}
        <header className="flex items-center justify-between px-8 py-5">
          <Link href="/">
            <LogoBranca height={56} />
          </Link>
          <Link
            href="/"
            className="flex items-center gap-2 text-white/60 hover:text-white text-sm transition-colors"
          >
            <i className="bi bi-arrow-left" />
            Página Inicial
          </Link>
        </header>
 
        <main className="flex-1 flex items-center justify-center px-6 py-8">
          <div className="w-full max-w-2xl">
            <div className="mb-6 text-center">
              <span
                className="text-xs font-bold uppercase tracking-widest mb-3 block"
                style={{ color: "#005eff", fontFamily: "var(--font-primary)" }}
              >
                Cotação
              </span>
              <h1
                className="text-4xl font-bold text-white leading-tight mb-2"
                style={{ fontFamily: "var(--font-primary)" }}
              >
                Calcule o valor do seu frete
              </h1>
              <p
                className="text-white/70"
                style={{ fontFamily: "var(--font-secondary)" }}
              >
                Preencha os dados e receba uma estimativa imediata
              </p>
            </div>
 
            <div
              className="rounded-2xl p-8"
              style={{
                backdropFilter: "blur(16px)",
                WebkitBackdropFilter: "blur(16px)",
                backgroundColor: "rgba(255,255,255,0.07)",
                border: "1px solid rgba(255,255,255,0.12)",
              }}
            >
              <CotacaoForm />
            </div>
          </div>
        </main>
 
        <footer className="py-4 text-center">
          <p className="text-white/20 text-xs" style={{ fontFamily: "var(--font-primary)" }}>
            © {new Date().getFullYear()} NextLog. Todos os direitos reservados.
          </p>
        </footer>
      </div>
    </div>
  );
}
