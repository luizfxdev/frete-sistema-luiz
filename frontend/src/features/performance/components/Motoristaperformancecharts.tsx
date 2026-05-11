import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";

export interface MotoristaEntregaDia {
  mes: string;
  entregas: number;
  canceladas: number;
}

interface MotoristaEntregasLineChartProps {
  data: MotoristaEntregaDia[];
}

export function MotoristaEntregasLineChart({ data }: MotoristaEntregasLineChartProps) {
  return (
    <div className="bg-white/5 border border-white/10 rounded-2xl p-6">
      <h3 className="text-sm font-bold text-white mb-6">Evolução de Entregas</h3>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={data || []}>
          <CartesianGrid strokeDasharray="3 3" stroke="#ffffff10" />
          <XAxis dataKey="mes" stroke="#9ca3af" />
          <YAxis stroke="#9ca3af" />
          <Tooltip 
            contentStyle={{ 
              backgroundColor: "#1f2937", 
              border: "1px solid #ffffff20", 
              borderRadius: "8px",
              color: "#fff"
            }} 
          />
          <Legend wrapperStyle={{ color: "#fff" }} />
          <Line 
            type="monotone" 
            dataKey="entregas" 
            stroke="#22c55e" 
            strokeWidth={2.5} 
            name="Entregas" 
            dot={{ r: 4, fill: "#22c55e" }}
          />
          <Line 
            type="monotone" 
            dataKey="canceladas" 
            stroke="#ef4444" 
            strokeWidth={2} 
            strokeDasharray="4 3" 
            name="Canceladas" 
            dot={{ r: 3, fill: "#ef4444" }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}