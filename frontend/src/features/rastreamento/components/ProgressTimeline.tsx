import type { StatusEntrega } from "@/shared/types/api";

const ETAPAS: { status: StatusEntrega; label: string; icon: string }[] = [
  { status: "EMITIDO", label: "Emitido", icon: "bi-check-lg" },
  { status: "SAIDA_CONFIRMADA", label: "Saída Confirmada", icon: "bi-check-lg" },
  { status: "EM_TRANSITO", label: "Em Trânsito", icon: "bi-truck" },
  { status: "ENTREGUE", label: "Entregue", icon: "bi-check-lg" },
];

const ORDEM: StatusEntrega[] = ["EMITIDO", "SAIDA_CONFIRMADA", "EM_TRANSITO", "ENTREGUE"];

function getEtapaState(etapa: StatusEntrega, statusAtual: StatusEntrega) {
  const idxEtapa = ORDEM.indexOf(etapa);
  const idxAtual = ORDEM.indexOf(statusAtual);
  if (statusAtual === "NAO_ENTREGUE" || statusAtual === "CANCELADO") {
    return idxEtapa < idxAtual ? "concluida" : idxEtapa === idxAtual ? "erro" : "pendente";
  }
  if (idxEtapa < idxAtual) return "concluida";
  if (idxEtapa === idxAtual) return statusAtual === "EM_TRANSITO" ? "transito" : "concluida";
  return "pendente";
}

interface Props {
  status: StatusEntrega;
}

export function ProgressTimeline({ status }: Props) {
  const isErro = status === "NAO_ENTREGUE" || status === "CANCELADO";

  return (
    <div className="bg-[#1a1a2e] rounded-2xl p-8">
      <div className="flex items-center justify-between relative">
        <div className="absolute top-6 left-8 right-8 h-0.5 bg-white/10" />
        <div
          className="absolute top-6 left-8 h-0.5 bg-accent transition-all duration-700"
          style={{
            width: isErro
              ? "100%"
              : `${(ORDEM.indexOf(status) / (ORDEM.length - 1)) * 100}%`,
            backgroundColor: isErro ? "#ef4444" : "#005eff",
          }}
        />

        {ETAPAS.map(({ status: etapaStatus, label, icon }) => {
          const state = getEtapaState(etapaStatus, status);

          const dotStyle =
            state === "concluida"
              ? "bg-success border-success text-white"
              : state === "transito"
              ? "bg-accent border-accent text-white"
              : state === "erro"
              ? "bg-red-500 border-red-500 text-white"
              : "bg-[#1a1a2e] border-white/20 text-white/30";

          return (
            <div key={etapaStatus} className="relative flex flex-col items-center gap-3 z-10">
              <div
                className={`w-12 h-12 rounded-full border-2 flex items-center justify-center transition-all duration-300 ${dotStyle}`}
              >
                <i className={`bi ${icon} text-sm`} />
              </div>
              <span
                className={`text-xs font-medium text-center max-w-[80px] leading-tight ${
                  state === "pendente" ? "text-white/30" : "text-white/80"
                }`}
              >
                {label}
              </span>
            </div>
          );
        })}
      </div>

      {isErro && (
        <div className="mt-6 flex items-center gap-2 bg-red-500/10 border border-red-500/20 rounded-xl px-4 py-3">
          <i className="bi bi-exclamation-triangle-fill text-red-500" />
          <span className="text-red-400 text-sm font-medium">
            {status === "NAO_ENTREGUE" ? "Tentativa de entrega sem sucesso" : "Envio cancelado"}
          </span>
        </div>
      )}
    </div>
  );
}