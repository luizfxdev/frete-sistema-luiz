import { ScrollDownIndicator } from "./ScrollDownIndicator";

export function HeroSection() {
  return (
    <section className="relative w-full min-h-screen flex items-center justify-center overflow-hidden">
      <video
        autoPlay
        muted
        loop
        playsInline
        className="absolute inset-0 w-full h-full object-cover"
      >
        <source src="/video/index_video.mp4" type="video/mp4" />
      </video>
      <div className="absolute inset-0 bg-gradient-to-b from-black/30 to-black/70" />

      <div className="relative z-10 text-center px-6 max-w-4xl mx-auto">
        <h1
          className="text-6xl lg:text-7xl font-bold text-white leading-tight tracking-tight mb-6"
          style={{ fontFamily: "var(--font-primary)" }}
        >
          Logística que move o Brasil
        </h1>
        <p
          className="text-2xl text-white/80 leading-relaxed max-w-xl mx-auto mb-10"
          style={{ fontFamily: "var(--font-secondary)" }}
        >
          Precisão, agilidade e cuidado em cada entrega
        </p>
        <div className="flex flex-wrap gap-4 justify-center">
          <a
            href="/cotacao"
            className="flex items-center gap-2 px-7 py-3.5 text-white font-semibold rounded-xl hover:opacity-90 transition-all hover:-translate-y-0.5 shadow-lg"
            style={{ backgroundColor: "#005eff" }}
          >
            <i className="bi bi-calculator-fill" />
            Cotar frete
          </a>
          <a
            href="#rastrear"
            className="flex items-center gap-2 px-7 py-3.5 bg-white/10 border border-white/25 backdrop-blur-sm text-white font-semibold rounded-xl hover:bg-white/20 transition-all hover:-translate-y-0.5"
          >
            <i className="bi bi-bullseye" />
            Rastrear encomenda
          </a>
        </div>
      </div>

      <ScrollDownIndicator />
    </section>
  );
}