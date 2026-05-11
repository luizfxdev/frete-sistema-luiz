"use client";

import {
  ComposedChart, Bar, Line, XAxis, YAxis,
  CartesianGrid, Tooltip, Legend, ResponsiveContainer,
} from "recharts";

interface ResumoMensal {
  mes: string;
  fretes: number;
  entregues: number;
  cancelados: number;
}

interface Props {
  data: ResumoMensal[];
}

export function ResumoChart({ data }: Props) {
  return (
    <div className="bg-white/5 border border-white/7 rounded-2xl p-6">
      <h3 className="text-sm font-bold text-white mb-6">Resumo Operacional</h3>
      <ResponsiveContainer width="100%" height={280}>
        <ComposedChart data={data} margin={{ top: 4, right: 8, bottom: 0, left: -20 }}>
          <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.05)" vertical={false} />
          <XAxis
            dataKey="mes"
            tick={{ fill: "rgba(255,255,255,0.35)", fontSize: 11 }}
            axisLine={false}
            tickLine={false}
          />
          <YAxis
            tick={{ fill: "rgba(255,255,255,0.35)", fontSize: 11 }}
            axisLine={false}
            tickLine={false}
          />
          <Tooltip
            contentStyle={{
              background: "#0a2540",
              border: "1px solid rgba(255,255,255,0.1)",
              borderRadius: 12,
              color: "#fff",
              fontSize: 12,
            }}
            cursor={{ fill: "rgba(255,255,255,0.03)" }}
          />
          <Legend wrapperStyle={{ fontSize: 11, color: "rgba(255,255,255,0.5)" }} />
          <Bar dataKey="fretes" name="Total fretes" fill="rgba(0,94,255,0.25)" radius={[4, 4, 0, 0]} barSize={20} />
          <Line type="monotone" dataKey="entregues" name="Entregues" stroke="#22c55e" strokeWidth={2.5} dot={{ r: 4, fill: "#22c55e" }} activeDot={{ r: 6 }} />
          <Line type="monotone" dataKey="cancelados" name="Cancelados" stroke="#ef4444" strokeWidth={2} strokeDasharray="4 3" dot={{ r: 3, fill: "#ef4444" }} />
        </ComposedChart>
      </ResponsiveContainer>
    </div>
  );
}