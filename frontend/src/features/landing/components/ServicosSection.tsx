import Link from "next/link";
import { UnidadesFranquias } from "./UnidadesFranquias";
import { Sustentabilidade } from "./Sustentabilidade";

const SERVICOS = [
  {
    icon: "bi-geo-alt-fill",
    title: "Rastreamento Online",
    desc: "Acompanhe cada etapa da entrega com atualizações em tempo real e histórico completo de eventos.",
    href: "#rastrear",
    cta: "Rastrear agora",
  },
  {
    icon: "bi-calculator-fill",
    title: "Cotação de Frete",
    desc: "Calcule o valor do seu frete em segundos e sem compromisso.",
    href: "/cotacao",
    cta: "Calcular frete",
  },
];

export function ServicosSection() {
  return (
    <section id="servicos" className="py-28" style={{ backgroundColor: "#ffffff" }}>
      <div className="max-w-7xl mx-auto px-6">
        <div className="mb-16" data-aos="fade-up">
          <span
            className="text-xs font-bold uppercase tracking-widest mb-3 block"
            style={{ color: "#005eff", fontFamily: "var(--font-primary)" }}
          >
            O que oferecemos
          </span>
          <h2
            className="text-4xl lg:text-5xl font-bold leading-tight"
            style={{ color: "#0a2540", fontFamily: "var(--font-primary)" }}
          >
            Soluções para toda a cadeia logística
          </h2>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          {SERVICOS.map(({ icon, title, desc, href, cta }, i) => (
            <div
              key={title}
              data-aos="fade-up"
              data-aos-delay={i * 80}
              className="rounded-2xl p-10 border flex flex-col gap-5 transition-colors duration-200"
              style={{ backgroundColor: "#f8faff", borderColor: "#e0eaff" }}
            >
              <div className="w-12 h-12 rounded-xl bg-white flex items-center justify-center border border-blue-100">
                <i className={`bi ${icon} text-xl`} style={{ color: "#005eff" }} />
              </div>
              <h3
                className="text-xl font-bold"
                style={{ color: "#0a2540", fontFamily: "var(--font-primary)" }}
              >
                {title}
              </h3>
              <p
                className="text-gray-500 text-sm leading-relaxed flex-1"
                style={{ fontFamily: "var(--font-primary)" }}
              >
                {desc}
              </p>
              <Link
                href={href}
                className="text-sm font-semibold self-start hover:opacity-80 transition-opacity"
                style={{ color: "#005eff", fontFamily: "var(--font-primary)" }}
              >
                {cta} →
              </Link>
            </div>
          ))}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div data-aos="fade-up" data-aos-delay="160">
            <UnidadesFranquias />
          </div>
          <div data-aos="fade-up" data-aos-delay="240">
            <Sustentabilidade />
          </div>
        </div>
      </div>
    </section>
  );
}