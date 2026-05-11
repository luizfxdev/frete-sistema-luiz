"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { LogoBranca } from "@/shared/components/ui/LogoBranca";
import { useAuthStore } from "@/core/auth/authStore";

const NAV = [
  { href: "/motorista/perfil", label: "Meu Perfil", icon: "bi-person-fill" },
  { href: "/motorista/performance", label: "Performance", icon: "bi-graph-up-arrow" },
];

export default function MotoristaLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const router = useRouter();
  const usuario = useAuthStore((s) => s.usuario);
  const logout = useAuthStore((s) => s.logout);

  const handleLogout = () => {
    logout();
    router.push("/colaborador");
  };

  return (
    <div className="flex min-h-screen bg-[#050f1a] text-white font-sans">
      <aside className="w-56 min-h-screen bg-brand-900 border-r border-white/6 flex flex-col">
        <div className="px-4 h-16 flex items-center border-b border-white/6">
          <LogoBranca height={26} />
        </div>
        <div className="px-3 py-2 border-b border-white/6">
          <span className="text-[10px] font-bold uppercase tracking-widest text-accent px-2">
            Área do Motorista
          </span>
        </div>
        <nav className="flex-1 px-2 py-3 space-y-0.5">
          {NAV.map(({ href, label, icon }) => (
            <Link
              key={href}
              href={href}
              className={`flex items-center gap-2.5 px-3 py-2.5 rounded-xl text-sm font-medium transition-all ${
                pathname.startsWith(href)
                  ? "bg-accent/15 text-accent"
                  : "text-white/50 hover:text-white hover:bg-white/5"
              }`}
            >
              <i className={`bi ${icon} text-base`} />
              {label}
            </Link>
          ))}
        </nav>
        <div className="px-2 pb-4 border-t border-white/6 pt-3 space-y-1">
          {usuario && (
            <div className="px-3 py-2 mb-1">
              <p className="text-xs font-semibold text-white truncate">{usuario.nome}</p>
              <p className="text-[10px] text-white/30">Motorista</p>
            </div>
          )}
          <button
            onClick={handleLogout}
            className="flex items-center gap-2.5 w-full px-3 py-2.5 rounded-xl text-sm text-white/40 hover:text-red-400 hover:bg-red-500/10 transition-all"
          >
            <i className="bi bi-box-arrow-left" />
            Sair
          </button>
        </div>
      </aside>

      <div className="flex-1 flex flex-col min-w-0">
        <header className="h-16 border-b border-white/6 flex items-center px-8 bg-brand-900/40 backdrop-blur-sm sticky top-0 z-10">
          <h1 className="text-sm font-bold text-white">
            {NAV.find((n) => pathname.startsWith(n.href))?.label ?? "Motorista"}
          </h1>
        </header>
        <main className="flex-1 p-8">{children}</main>
      </div>
    </div>
  );
}