import { CandidaturaForm } from "@/features/trabalhe-conosco/components/CandidaturaForm";
import { LogoBranca } from "@/shared/components/ui/LogoBranca";
import Link from "next/link";
import type { Metadata } from "next";

export const metadata: Metadata = { title: "Trabalhe Conosco" };

export default function TrabalheConoscoPage() {
  return (
    <div
      className="min-h-screen relative"
      style={{
        backgroundImage: "url('/assets/backgrounds/backgroundtrabalheconosco.png')",
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundAttachment: "fixed",
      }}
    >
      <div className="absolute inset-0" style={{ backgroundColor: "rgba(5,15,26,0.65)" }} />
      <div className="relative z-10">
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
        <main className="px-6 py-12">
          <div className="w-full max-w-2xl mx-auto">
            <div className="mb-8 text-center">
              <h1
                className="text-5xl font-bold text-white leading-tight mb-4"
                style={{ fontFamily: "var(--font-primary)" }}
              >
                Trabalhe conosco
              </h1>
              <p
                className="text-white/70 text-lg"
                style={{ fontFamily: "var(--font-secondary)" }}
              >
                Junte-se à equipe NextLog. Preencha o formulário e entraremos em contato em até 5 dias úteis.
              </p>
            </div>
            <div
              className="rounded-2xl p-8 mb-8"
              style={{
                backdropFilter: "blur(16px)",
                WebkitBackdropFilter: "blur(16px)",
                backgroundColor: "rgba(255,255,255,0.07)",
                border: "1px solid rgba(255,255,255,0.12)",
              }}
            >
              <CandidaturaForm />
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