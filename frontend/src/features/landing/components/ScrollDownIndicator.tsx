"use client";

export function ScrollDownIndicator() {
  const handleClick = () => {
    const next = document.querySelector("#sobre") as HTMLElement | null;
    if (next) next.scrollIntoView({ behavior: "smooth" });
  };

  return (
    <button
      onClick={handleClick}
      aria-label="Rolar para baixo"
      className="absolute bottom-10 left-1/2 -translate-x-1/2 flex flex-col items-center gap-1 text-white/60 hover:text-white transition-colors group"
    >
      <span className="text-xs tracking-widest uppercase font-medium opacity-70">
        Saiba mais
      </span>
      <i className="bi bi-chevron-double-down text-xl animate-bounce" />
    </button>
  );
}