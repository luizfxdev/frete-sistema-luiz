interface Props {
  label: string;
  value: string | number;
  icon: string;
  color?: string;
  trend?: string;
  trendUp?: boolean;
}

export function KpiCard({ label, value, icon, color = "#005eff", trend, trendUp }: Props) {
  return (
    <div className="bg-white/5 border border-white/7 rounded-2xl p-6 flex flex-col gap-4 hover:border-white/12 transition-colors">
      <div className="flex items-start justify-between">
        <div
          className="w-11 h-11 rounded-xl flex items-center justify-center"
          style={{ background: `${color}1a`, color }}
        >
          <i className={`bi ${icon} text-lg`} />
        </div>
        {trend && (
          <span
            className={`text-xs font-bold px-2 py-1 rounded-full ${
              trendUp
                ? "bg-success/10 text-success"
                : "bg-red-500/10 text-red-400"
            }`}
          >
            {trendUp ? "▲" : "▼"} {trend}
          </span>
        )}
      </div>
      <div>
        <p className="text-2xl font-bold text-white leading-none mb-1">{value}</p>
        <p className="text-xs font-semibold uppercase tracking-widest text-white/35">{label}</p>
      </div>
    </div>
  );
}