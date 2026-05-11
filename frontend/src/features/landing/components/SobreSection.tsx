const CARDS = [
  {
    icon: "bi-geo-alt-fill",
    title: "Rastreamento em tempo real",
    desc: "Visibilidade completa da carga em cada etapa, com notificações automáticas e histórico de eventos.",
  },
  {
    icon: "bi-truck-front-fill",
    title: "Frota especializada",
    desc: "Veículos equipados e motoristas certificados para transportar qualquer tipo de carga com segurança.",
  },
  {
    icon: "bi-globe-americas",
    title: "Cobertura nacional",
    desc: "Presença em todos os estados brasileiros com rotas otimizadas para maior agilidade nas entregas.",
  },
  {
    icon: "bi-cpu-fill",
    title: "Tecnologia de ponta",
    desc: "Plataforma digital completa para gestão de fretes, motoristas, manutenções e relatórios analíticos.",
  },
];

export function SobreSection() {
  const anoFundacao = 1986;
  const anoAtual = new Date().getFullYear();
  const anos = anoAtual - anoFundacao;

  return (
    <section id="sobre" className="py-28" style={{ backgroundColor: "var(--color-cream)" }}>
      <div className="max-w-7xl mx-auto px-6">
        <div className="mb-16" data-aos="fade-up">
          <span
            className="text-xs font-bold uppercase tracking-widest mb-3 block"
            style={{ color: "var(--color-accent)", fontFamily: "var(--font-primary)" }}
          >
            Sobre a NextLog
          </span>
          <h2
            className="text-4xl lg:text-5xl font-bold leading-tight"
            style={{ color: "#0a2540", fontFamily: "var(--font-primary)" }}
          >
            Uma plataforma, toda a operação
          </h2>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {CARDS.map((card, i) => (
            <div
              key={card.title}
              data-aos="zoom-in-up"
              data-aos-delay={i * 100}
              className="bg-white rounded-2xl p-8 border border-gray-100 hover:border-blue-200 hover:shadow-xl transition-all duration-300 group"
            >
              <div
                className="w-12 h-12 rounded-xl flex items-center justify-center mb-5 transition-colors duration-300"
                style={{ backgroundColor: "#e8f0ff" }}
              >
                <i
                  className={`bi ${card.icon} text-xl`}
                  style={{ color: "#005eff" }}
                />
              </div>
              <h3
                className="font-bold text-lg mb-3"
                style={{ color: "#0a2540", fontFamily: "var(--font-primary)" }}
              >
                {card.title}
              </h3>
              <p className="text-gray-500 text-sm leading-relaxed" style={{ fontFamily: "var(--font-primary)" }}>
                {card.desc}
              </p>
            </div>
          ))}
        </div>

        <div
          className="mt-20 grid grid-cols-1 lg:grid-cols-2 gap-12 items-center"
          data-aos="fade-up"
          data-aos-delay="100"
        >
          <div>
            <span
              className="text-xs font-bold uppercase tracking-widest mb-3 block"
              style={{ color: "var(--color-accent)", fontFamily: "var(--font-primary)" }}
            >
              Nossa história
            </span>
            <h3
              className="text-3xl font-bold mb-6 leading-tight"
              style={{ color: "#0a2540", fontFamily: "var(--font-primary)" }}
            >
              Quatro décadas conectando o Brasil
            </h3>
            <p
              className="text-gray-600 leading-relaxed mb-4 text-sm"
              style={{ fontFamily: "var(--font-primary)" }}
            >
              Fundada em 1986, a NextLog nasceu em Pernambuco com uma missão clara: tornar a
              logística brasileira mais eficiente, confiável e acessível para empresas de todos
              os portes.
            </p>
            <p
              className="text-gray-500 text-sm leading-relaxed mb-4"
              style={{ fontFamily: "var(--font-primary)" }}
            >
              Com centros de distribuição estrategicamente posicionados em todos os estados do
              Brasil, garantimos cobertura total e prazos competitivos — do Oiapoque ao Chuí.
              Transportamos alimentos, eletrônicos, produtos farmacêuticos e cargas gerais com
              os mais altos padrões de segurança e rastreabilidade.
            </p>
            <p
              className="text-gray-500 text-sm leading-relaxed"
              style={{ fontFamily: "var(--font-primary)" }}
            >
              Nossa rede de parceiros em toda a cadeia logística nos permite oferecer soluções
              integradas — da coleta à entrega final — com a confiança de quem conhece cada
              quilômetro desse país.
            </p>
          </div>

          <div className="grid grid-cols-2 gap-6">
            {[
              { value: `${anos}`, label: "Anos de experiência" },
              { value: "27", label: "Estados atendidos" },
              { value: "5 mil+", label: "Entregas por dia" },
              { value: "PE", label: "Sede em Pernambuco" },
            ].map((stat) => (
              <div
                key={stat.label}
                className="bg-white rounded-2xl p-6 border border-gray-100 text-center"
              >
                <p
                  className="text-3xl font-bold mb-1"
                  style={{ color: "#005eff", fontFamily: "var(--font-primary)" }}
                >
                  {stat.value}
                </p>
                <p
                  className="text-xs text-gray-500 font-medium uppercase tracking-wide"
                  style={{ fontFamily: "var(--font-primary)" }}
                >
                  {stat.label}
                </p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}