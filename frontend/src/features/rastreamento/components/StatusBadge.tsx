import type { StatusEntrega } from "@/shared/types/api";

const STATUS_CONFIG: Record<StatusEntrega, { label: string; icon: string; color: string; bg: string }> = {
  EMITIDO: { label: "Emitido", icon: "bi-check-lg", color: "text-success", bg: "bg-success/10" },
  SAIDA_CONFIRMADA: { label: "Saída Confirmada", icon: "bi-check-lg", color: "text-success", bg: "bg-success/10" },
  EM_TRANSITO: { label: "Em Trânsito", icon: "bi-truck", color: "text-orange-500", bg: "bg-orange-500/10" },
  ENTREGUE: { label: "Entregue", icon: "bi-check-lg", color: "text-success", bg: "bg-success/10" },
  NAO_ENTREGUE: { label: "Não Entregue", icon: "bi-x-lg", color: "text-red-500", bg: "bg-red-500/10" },
  CANCELADO: { label: "Cancelado", icon: "bi-x-lg", color: "text-red-500", bg: "bg-red-500/10" },
};

interface Props {
  status: StatusEntrega;
}

export function StatusBadge({ status }: Props) {
  const cfg = STATUS_CONFIG[status];
  return (
    <span className={`inline-flex items-center gap-1.5 text-xs font-bold uppercase tracking-wide px-3 py-1.5 rounded-full ${cfg.color} ${cfg.bg}`}>
      <i className={`bi ${cfg.icon}`} />
      {cfg.label}
    </span>
  );
}