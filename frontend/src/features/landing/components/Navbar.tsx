"use client";

import { useState } from "react";
import Link from "next/link";
import { useLogoScroll } from "@/features/landing/hooks/useLogoScroll";
import { LogoBranca } from "@/shared/components/ui/LogoBranca";
import { LogoPreta } from "@/shared/components/ui/LogoPreta";

const NAV_ITEMS = [
  { label: "Sobre a NextLog", icon: "bi-building-fill", href: "#sobre" },
  { label: "Produtos e Serviços", icon: "bi-box2-fill", href: "#servicos" },
  { label: "Cotação", icon: "bi-calculator-fill", href: "/cotacao" },
  { label: "Trabalhe Conosco", icon: "bi-bag-check-fill", href: "/trabalhe-conosco" },
  { label: "Rastrear Encomenda", icon: "bi-bullseye", href: "#rastrear" },
];

export function Navbar() {
  const scrolled = useLogoScroll(80);
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <header
      className={`fixed top-0 left-0 right-0 z-50 transition-all duration-300 ${
        scrolled
          ? "bg-white shadow-md"
          : "bg-transparent"
      }`}
    >
      <div className="max-w-7xl mx-auto px-6 h-[72px] flex items-center justify-between">
        <Link href="/" className="transition-opacity duration-300">
          {scrolled ? (
            <LogoPreta height={36} />
          ) : (
            <LogoBranca height={36} />
          )}
        </Link>

        <nav className="hidden lg:flex items-center gap-6">
          {NAV_ITEMS.map(({ label, icon, href }) => (
            <Link
              key={label}
              href={href}
              className={`flex items-center gap-1.5 text-sm font-medium transition-all duration-200 hover:-translate-y-0.5 hover:drop-shadow-sm ${
                scrolled
                  ? "text-brand-900 hover:text-accent"
                  : "text-white/90 hover:text-white"
              }`}
            >
              <i className={`bi ${icon} text-xs`} />
              {label}
            </Link>
          ))}

          <Link
            href="/auth/login"
            className="flex items-center gap-1.5 px-4 py-2 rounded-lg text-sm font-semibold bg-accent text-white hover:bg-accent-dark transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-accent/30"
          >
            <i className="bi bi-person-fill" />
            Colaborador
          </Link>
        </nav>

        <button
          className={`lg:hidden p-2 rounded-lg transition-colors ${
            scrolled ? "text-brand-900" : "text-white"
          }`}
          onClick={() => setMenuOpen((v) => !v)}
          aria-label="Menu"
        >
          <i className={`bi ${menuOpen ? "bi-x-lg" : "bi-list"} text-xl`} />
        </button>
      </div>

      {menuOpen && (
        <div className="lg:hidden bg-white border-t border-gray-100 shadow-xl">
          <div className="max-w-7xl mx-auto px-6 py-4 flex flex-col gap-2">
            {NAV_ITEMS.map(({ label, icon, href }) => (
              <Link
                key={label}
                href={href}
                onClick={() => setMenuOpen(false)}
                className="flex items-center gap-2 py-2.5 px-3 rounded-lg text-sm font-medium text-brand-900 hover:bg-gray-50 hover:text-accent transition-colors"
              >
                <i className={`bi ${icon}`} />
                {label}
              </Link>
            ))}

            <Link
              href="/auth/login"
              onClick={() => setMenuOpen(false)}
              className="flex items-center justify-center gap-2 py-2.5 px-3 rounded-lg text-sm font-semibold bg-accent text-white hover:bg-accent-dark transition-colors mt-2"
            >
              <i className="bi bi-person-fill" />
              Entrar
            </Link>
          </div>
        </div>
      )}
    </header>
  );
}