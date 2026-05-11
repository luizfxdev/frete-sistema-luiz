"use client";

import React from "react";
import type { DashboardIndicadores } from "@/features/dashboard/adapters/dashboardService";

interface IndicadoresCardProps {
  data: DashboardIndicadores;
}

export function IndicadoresCard({ data }: IndicadoresCardProps) {
  const indicadores = [
    { label: "Entregas no Prazo", valor: data.entregasNoPrazo, unidade: "%", icon: "bi-check-circle-fill" },
    { label: "Entregas Fora do Prazo", valor: data.entregasForaDoPrazo, unidade: "%", icon: "bi-exclamation-circle-fill" },
    { label: "Entregas Não Realizadas", valor: data.entregasNaoRealizadas, unidade: "%", icon: "bi-x-circle-fill" },
    { label: "Distância Média", valor: data.distanciaMedia, unidade: "km", icon: "bi-geo-alt-fill" },
    { label: "Consumo Combustível", valor: data.consumoCombustivel, unidade: "L/100km", icon: "bi-fuel-pump-fill" },
    { label: "Fretes Completados", valor: data.fretesCompletados, unidade: "un", icon: "bi-box-fill" },
    { label: "Ticket Médio", valor: data.ticketMedio, unidade: "média", icon: "bi-cash-coin" },
    { label: "Taxa de Utilização Frota", valor: data.taxaUtilizacaoFrota, unidade: "%", icon: "bi-truck-front-fill" },
  ];

  return (
    <div className="space-y-6 animate-fadeIn">
      <div className="mb-8">
        <h2 className="text-2xl font-bold text-gray-900" style={{ fontFamily: "var(--font-primary)" }}>
          Indicadores do Período
        </h2>
        <p className="text-sm text-gray-500 mt-1" style={{ fontFamily: "var(--font-primary)" }}>
          {data.periodo}
        </p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {indicadores.map((item, idx) => (
          <div
            key={idx}
            className="bg-white rounded-2xl p-6 shadow-sm hover:shadow-md transition-shadow duration-300 border border-gray-100"
          >
            <div className="flex items-start justify-between mb-4">
              <div
                className="w-12 h-12 rounded-xl flex items-center justify-center text-xl"
                style={{ backgroundColor: "#e0f2fe", color: "#0284c7" }}
              >
                <i className={`bi ${item.icon}`} />
              </div>
            </div>

            <div className="space-y-2">
              <p className="text-xs font-semibold uppercase tracking-wide text-gray-500" style={{ fontFamily: "var(--font-primary)" }}>
                {item.label}
              </p>

              <div className="flex items-baseline gap-2">
                <span className="text-3xl font-bold text-gray-900" style={{ fontFamily: "var(--font-primary)" }}>
                  {typeof item.valor === "string" ? item.valor : item.valor.toFixed(1)}
                </span>
                <span className="text-xs text-gray-400" style={{ fontFamily: "var(--font-primary)" }}>
                  {item.unidade}
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
