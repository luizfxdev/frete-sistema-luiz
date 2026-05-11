import React from "react";
import { PerformanceData } from "@/features/performance/adapters/performanceService";
import {
  LineChart, Line, BarChart, Bar, PieChart, Pie, Cell,
  AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
  ResponsiveContainer,
} from "recharts";

const COLORS = {
  entregue: "#10b981",
  emTransito: "#3b82f6",
  atrasado: "#ef4444",
  cancelado: "#6b7280",
  pendente: "#f59e0b",
};

interface ChartsProps {
  data: PerformanceData;
}

export function EntregasLineChart({ data }: ChartsProps) {
  return (
    <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
      <h3 className="text-lg font-bold text-gray-900 mb-4">Entregas por Dia</h3>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={data.entregasPorDia || []}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
          <XAxis dataKey="dia" stroke="#9ca3af" />
          <YAxis stroke="#9ca3af" />
          <Tooltip contentStyle={{ backgroundColor: "#fff", border: "1px solid #e5e7eb", borderRadius: "8px" }} />
          <Legend />
          <Line type="monotone" dataKey="entregues" stroke={COLORS.entregue} strokeWidth={2} name="Entregues" />
          <Line type="monotone" dataKey="emTransito" stroke={COLORS.emTransito} strokeWidth={2} name="Em Trânsito" />
          <Line type="monotone" dataKey="atrasados" stroke={COLORS.atrasado} strokeWidth={2} name="Atrasados" />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}

export function StatusPieChart({ data }: ChartsProps) {
  return (
    <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
      <h3 className="text-lg font-bold text-gray-900 mb-4">Status dos Fretes</h3>
      <ResponsiveContainer width="100%" height={300}>
        <PieChart>
          <Pie
            data={data.fretosPorStatus || []}
            cx="50%"
            cy="45%"
            outerRadius={90}
            dataKey="value"
            label={false}
          >
            {(data.fretosPorStatus || []).map((_, index) => (
              <Cell key={`cell-${index}`} fill={Object.values(COLORS)[index % Object.values(COLORS).length] as string} />
            ))}
          </Pie>
          <Tooltip contentStyle={{ backgroundColor: "#fff", border: "1px solid #e5e7eb", borderRadius: "8px" }} formatter={(value, name) => [value, name]} />
          <Legend layout="vertical" align="right" verticalAlign="middle" />
        </PieChart>
      </ResponsiveContainer>
    </div>
  );
}

export function VolumeAreaChart({ data }: ChartsProps) {
  return (
    <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
      <h3 className="text-lg font-bold text-gray-900 mb-4">Volume Transportado (ton)</h3>
      <ResponsiveContainer width="100%" height={280}>
        <AreaChart data={data.volumeTransportado || []}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
          <XAxis dataKey="dia" stroke="#9ca3af" />
          <YAxis stroke="#9ca3af" />
          <Tooltip contentStyle={{ backgroundColor: "#fff", border: "1px solid #e5e7eb", borderRadius: "8px" }} />
          <Area type="monotone" dataKey="volume" fill={COLORS.emTransito} stroke={COLORS.emTransito} strokeWidth={2} />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  );
}

export function TaxaSucessoBarChart({ data }: ChartsProps) {
  return (
    <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
      <h3 className="text-lg font-bold text-gray-900 mb-4">Taxa Sucesso vs Atraso</h3>
      <ResponsiveContainer width="100%" height={280}>
        <BarChart data={data.taxaSucessoAtraso || []}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
          <XAxis dataKey="semana" stroke="#9ca3af" />
          <YAxis stroke="#9ca3af" />
          <Tooltip contentStyle={{ backgroundColor: "#fff", border: "1px solid #e5e7eb", borderRadius: "8px" }} />
          <Legend />
          <Bar dataKey="sucesso" fill={COLORS.entregue} name="Taxa de Sucesso %" />
          <Bar dataKey="atraso" fill={COLORS.atrasado} name="Taxa de Atraso %" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

export function RegioesBarChart({ data }: ChartsProps) {
  return (
    <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
      <h3 className="text-lg font-bold text-gray-900 mb-4">Distribuição por Estado</h3>
      <ResponsiveContainer width="100%" height={280}>
        <BarChart data={data.fretesPorRegiao || []} layout="vertical">
          <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
          <XAxis type="number" stroke="#9ca3af" />
          <YAxis dataKey="uf" type="category" stroke="#9ca3af" width={30} />
          <Tooltip contentStyle={{ backgroundColor: "#fff", border: "1px solid #e5e7eb", borderRadius: "8px" }} />
          <Bar dataKey="quantidade" fill={COLORS.emTransito} name="Fretes" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

export function TopMotoristasCard({ data }: ChartsProps) {
  return (
    <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
      <h3 className="text-lg font-bold text-gray-900 mb-4">Top 5 Motoristas</h3>
      <div className="space-y-3">
        {(data.topMotoristas || []).slice(0, 5).map((motorista, idx) => (
          <div key={idx} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition">
            <div className="flex items-center gap-3">
              <div
                className="w-8 h-8 rounded-full text-white flex items-center justify-center text-sm font-bold"
                style={{ backgroundColor: "#292929" }}
              >
                {idx + 1}
              </div>
              <span className="font-medium text-gray-900">{motorista.nome}</span>
            </div>
            <span className="text-sm font-semibold" style={{ color: "#292929" }}>
              {motorista.totalFretes} fretes
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}

export function UltimosFretesTable({ data }: ChartsProps) {
  return (
    <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
      <h3 className="text-lg font-bold text-gray-900 mb-4">Últimos Fretes</h3>
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-gray-200">
              <th className="text-left py-3 px-4 font-semibold text-gray-700">Número</th>
              <th className="text-left py-3 px-4 font-semibold text-gray-700">Rota</th>
              <th className="text-left py-3 px-4 font-semibold text-gray-700">Motorista</th>
              <th className="text-left py-3 px-4 font-semibold text-gray-700">Valor</th>
              <th className="text-left py-3 px-4 font-semibold text-gray-700">Status</th>
            </tr>
          </thead>
          <tbody>
            {(data.ultimosFretes || []).map((frete, idx) => (
              <tr key={idx} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 font-medium text-gray-900">{frete.numero}</td>
                <td className="py-3 px-4 text-gray-600">{frete.municipioOrigem} → {frete.municipioDestino}</td>
                <td className="py-3 px-4 text-gray-600">{frete.nomeMotorista}</td>
                <td className="py-3 px-4 font-semibold text-gray-900">
                  R$ {typeof frete.valorTotal === "number"
                    ? frete.valorTotal.toLocaleString("pt-BR", { minimumFractionDigits: 2 })
                    : frete.valorTotal}
                </td>
                <td className="py-3 px-4">
                  <StatusBadge status={frete.status} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

interface StatusBadgeProps {
  status: string;
}

function StatusBadge({ status }: StatusBadgeProps) {
  const getColors = (s: string) => {
    switch (s) {
      case "ENTREGUE": return { bg: `${COLORS.entregue}20`, text: COLORS.entregue };
      case "EM_TRANSITO": return { bg: `${COLORS.emTransito}20`, text: COLORS.emTransito };
      case "NAO_ENTREGUE": return { bg: `${COLORS.atrasado}20`, text: COLORS.atrasado };
      default: return { bg: `${COLORS.cancelado}20`, text: COLORS.cancelado };
    }
  };

  const { bg, text } = getColors(status);

  return (
    <span className="inline-block px-3 py-1 rounded-full text-xs font-semibold" style={{ backgroundColor: bg, color: text }}>
      {status}
    </span>
  );
}