import { cn } from "@/core/utils/cn";

type Variant = "default" | "success" | "warning" | "danger" | "info";

const VARIANTS: Record<Variant, string> = {
  default: "bg-white/8 text-white/60",
  success: "bg-success/10 text-success",
  warning: "bg-yellow-400/10 text-yellow-400",
  danger: "bg-red-500/10 text-red-400",
  info: "bg-accent/10 text-accent",
};

interface Props {
  label: string;
  variant?: Variant;
  className?: string;
}

export function Badge({ label, variant = "default", className }: Props) {
  return (
    <span
      className={cn(
        "inline-flex items-center text-[11px] font-bold uppercase tracking-wide px-2.5 py-1 rounded-full",
        VARIANTS[variant],
        className
      )}
    >
      {label}
    </span>
  );
}