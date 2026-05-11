import Link from "next/link";
import { LogoBranca } from "@/shared/components/ui/LogoBranca";

export function Footer() {
  return (
    <footer className="bg-brand-900 border-t border-white/5 pt-16 pb-8">
      <div className="max-w-7xl mx-auto px-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-10 pb-12 border-b border-white/5">
          <div className="lg:col-span-2">
            <LogoBranca height={32} className="mb-4" />
            <p className="text-white/40 text-sm leading-relaxed max-w-xs">
              Plataforma completa de gestão logística — rastreamento, cotação de fretes e controle de frota em um único lugar.
            </p>
          </div>

          {[
            {
              title: "Plataforma",
              links: [
                { label: "Rastreamento", href: "#rastrear" },
                { label: "Cotação de Frete", href: "/cotacao" },
                { label: "Área do Colaborador", href: "/colaborador" },
              ],
            },
            {
              title: "Empresa",
              links: [
                { label: "Sobre a NextLog", href: "#sobre" },
                { label: "Produtos e Serviços", href: "#servicos" },
                { label: "Trabalhe Conosco", href: "/trabalhe-conosco" },
              ],
            },
            {
              title: "Contato",
              links: [
                { label: "contato@nextlog.com.br", href: "mailto:contato@nextlog.com.br" },
                { label: "(11) 0000-0000", href: "tel:+551100000000" },
              ],
            },
          ].map(({ title, links }) => (
            <div key={title}>
              <h4 className="text-xs font-bold uppercase tracking-widest text-white/25 mb-4">
                {title}
              </h4>
              <ul className="space-y-2.5">
                {links.map(({ label, href }) => (
                  <li key={label}>
                    <Link
                      href={href}
                      className="text-sm text-white/50 hover:text-white transition-colors"
                    >
                      {label}
                    </Link>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>

        <div className="pt-8 flex flex-col sm:flex-row items-center justify-between gap-4">
          <p className="text-xs text-white/25">
            © {new Date().getFullYear()} NextLog. Todos os direitos reservados.
          </p>
          <p className="text-xs text-white/15">
            Logística que move o Brasil.
          </p>
        </div>
      </div>
    </footer>
  );
}