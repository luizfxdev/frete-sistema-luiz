import { useState, useEffect } from "react";
import { format, subDays } from "date-fns";
import { useAuthStore } from "@/core/auth/authStore";

interface PerformanceData {
  kpis: {
    totalFretes: number;
    fretesEntregues: number;
    fretesAtrasados: number;
    fretesEmAndamento: number;
    taxaPontualidade: number;
    receita: number;
  };
  entregasPorDia: Array<{ dia: string; entregues: number; emTransito: number; atrasados: number }>;
  fretosPorStatus: Array<{ name: string; value: number }>;
  volumeTransportado: Array<{ dia: string; volume: number }>;
  fretesPorRegiao: Array<{ uf: string; quantidade: number }>;
  taxaSucessoAtraso: Array<{ semana: string; sucesso: number; atraso: number }>;
  topMotoristas: Array<{ nome: string; totalFretes: number }>;
  ultimosFretes: Array<any>;
  entregasPorEstado: Array<{ estado: string; quantidade: number }>;
}

const API_URL = process.env.NEXT_PUBLIC_API_URL;

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

        const response = await fetch(
          `${API_URL}/dashboard/performance?dataInicio=${inicio}&dataFim=${fim}`,
          {
            credentials: "include",
            redirect: "manual",
          },
        );

        if (response.status === 401 || response.type === "opaqueredirect") {
          setError("Sessão expirada. Faça login novamente.");
          return;
        }

        if (!response.ok) throw new Error("Erro ao carregar dados de performance");

        setData(await response.json());
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