export function Sustentabilidade() {
  return (
    <div
      className="rounded-2xl p-8 border flex flex-col gap-5 h-full"
      style={{ backgroundColor: "#0a2540", borderColor: "rgba(255,255,255,0.05)" }}
    >
      <div className="w-12 h-12 rounded-xl bg-white flex items-center justify-center flex-shrink-0">
        <i className="bi bi-tree-fill text-xl" style={{ color: "#005eff" }} />
      </div>
      <h3
        className="text-xl font-bold text-white"
        style={{ fontFamily: "var(--font-primary)" }}
      >
        Sustentabilidade
      </h3>
      <p
        className="text-white/70 text-sm leading-relaxed"
        style={{ fontFamily: "var(--font-primary)" }}
      >
        Somos uma empresa comprometida com a preservação do meio ambiente e a sustentabilidade.
      </p>
      <p
        className="text-white/50 text-sm leading-relaxed"
        style={{ fontFamily: "var(--font-primary)" }}
      >
        Nossa frota conta com veículos de baixo consumo e tecnologia de rastreamento de rotas
        que reduzem emissões de CO₂. Utilizamos embalagens recicláveis e processos logísticos
        planejados para minimizar o desperdício em cada etapa da operação.
      </p>
      <div className="grid grid-cols-3 gap-3">
        {[
          { icon: "bi-wind", label: "Emissões reduzidas" },
          { icon: "bi-recycle", label: "Embalagens sustentáveis" },
          { icon: "bi-graph-down-arrow", label: "Metas ESG" },
        ].map((item) => (
          <div
            key={item.label}
            className="rounded-xl p-3 flex flex-col items-center gap-2 text-center"
            style={{ backgroundColor: "rgba(255,255,255,0.05)" }}
          >
            <i className={`bi ${item.icon} text-lg`} style={{ color: "#005eff" }} />
            <span
              className="text-white/50 text-xs leading-tight"
              style={{ fontFamily: "var(--font-primary)" }}
            >
              {item.label}
            </span>
          </div>
        ))}
      </div>
      <div className="rounded-xl overflow-hidden border" style={{ borderColor: "rgba(255,255,255,0.1)" }}>
        <iframe
          width="100%"
          height="315"
          src="https://www.youtube.com/embed/Okq3yxCeajc?start=5"
          title="Vídeo de Sustentabilidade"
          frameBorder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowFullScreen
          className="w-full aspect-video"
          style={{ borderRadius: "0.75rem" }}
        />
        <p
          className="text-center py-2 text-xs"
          style={{ color: "rgba(255,255,255,0.2)", fontFamily: "var(--font-primary)" }}
        >
          Vídeo institucional — meio ambiente e logística verde
        </p>
      </div>
    </div>
  );
}