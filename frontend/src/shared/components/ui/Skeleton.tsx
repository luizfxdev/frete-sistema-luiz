import { cn } from "@/core/utils/cn";

interface Props {
  className?: string;
  rows?: number;
}

export function Skeleton({ className, rows = 1 }: Props) {
  if (rows === 1) {
    return (
      <div
        className={cn(
          "rounded-xl bg-white/5 animate-pulse",
          className ?? "h-10 w-full"
        )}
      />
    );
  }

  return (
    <div className="space-y-3">
      {Array.from({ length: rows }).map((_, i) => (
        <div
          key={i}
          className={cn(
            "rounded-xl bg-white/5 animate-pulse h-10",
            i === rows - 1 ? "w-2/3" : "w-full",
            className
          )}
        />
      ))}
    </div>
  );
}