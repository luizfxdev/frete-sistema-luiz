"use client";

import { useState } from "react";
import { calcularCotacao, type CotacaoParams, type CotacaoResult } from "@/features/cotacao/adapters/cotacaoService";

export function useCotacao() {
  const [resultado, setResultado] = useState<CotacaoResult | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const calcular = async (params: CotacaoParams) => {
    setLoading(true);
    setError(null);
    try {
      const res = await calcularCotacao(params);
      setResultado(res);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Erro ao calcular cotação");
    } finally {
      setLoading(false);
    }
  };

  return { resultado, loading, error, calcular };
}