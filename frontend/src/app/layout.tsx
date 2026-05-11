import type { Metadata } from "next";
import { DM_Sans, Libre_Baskerville } from "next/font/google";
import "./globals.css";

const dmSans = DM_Sans({
  subsets: ["latin"],
  variable: "--font-primary",
  display: "swap",
});

const libreBaskerville = Libre_Baskerville({
  subsets: ["latin"],
  weight: ["400", "700"],
  style: ["normal", "italic"],
  variable: "--font-secondary",
  display: "swap",
});

export const metadata: Metadata = {
  title: {
    default: "NextLog — Logística que move o Brasil",
    template: "%s | NextLog",
  },
  description:
    "Plataforma de gestão logística com rastreamento em tempo real, cotação de fretes e painel completo para motoristas e gestores.",
  keywords: ["logística", "rastreamento", "frete", "transportadora", "frota"],
  icons: { icon: "/favicon.ico" },
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-BR" className={`${dmSans.variable} ${libreBaskerville.variable}`}>
      <head>
        <link
          rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
        />
      </head>
      <body style={{ fontFamily: "var(--font-primary), DM Sans, -apple-system, sans-serif" }}>
        {children}
      </body>
    </html>
  );
}