import { useState, useEffect } from "react";
import { format, subDays } from "date-fns";
import { useAuthStore } from "@/core/auth/authStore";
import { buscarPerformance, type PerformanceData } from "@/features/performance/adapters/performanceService";

export function usePerformance(dataInicio?: string, dataFim?: string) {
  const [data, setData] = useState<PerformanceData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { token, isLoading: authLoading } = useAuthStore();

  useEffect(() => {
    if (authLoading || !token) return;

    const fetchData = async () => {
      try {
        setLoading(true);
        const fim = dataFim || format(new Date(), "yyyy-MM-dd");
        const inicio = dataInicio || format(subDays(new Date(), 30), "yyyy-MM-dd");

        const performanceData = await buscarPerformance(inicio, fim);
        setData(performanceData);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Erro desconhecido");
        setData(null);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [dataInicio, dataFim, token, authLoading]);

  return { data, loading, error };
}