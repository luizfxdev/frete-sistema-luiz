"use client";

import { useState, useEffect } from "react";
import { buscarKpis, buscarIndicadores, type DashboardKpis, type DashboardIndicadores } from "@/features/dashboard/adapters/dashboardService";

export function useDashboard() {
  const [kpis, setKpis] = useState<DashboardKpis | null>(null);
  const [indicadores, setIndicadores] = useState<DashboardIndicadores | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    Promise.all([buscarKpis(), buscarIndicadores()])
      .then(([kpisData, indicadoresData]) => {
        setKpis(kpisData);
        setIndicadores(indicadoresData);
      })
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  return { kpis, indicadores, loading, error };
}