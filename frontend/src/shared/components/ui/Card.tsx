import { cn } from "@/core/utils/cn";

interface Props {
  children: React.ReactNode;
  className?: string;
}

export function Card({ children, className }: Props) {
  return (
    <div className={cn("bg-white/5 border border-white/7 rounded-2xl p-6", className)}>
      {children}
    </div>
  );
}