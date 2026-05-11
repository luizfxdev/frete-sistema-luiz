"use client";

import { useState, useEffect } from "react";
import { buscarRastreamento, type EntregaRastreamento } from "@/features/rastreamento/adapters/rastreamentoService";

export function useRastreamento(documento: string) {
  const [data, setData] = useState<EntregaRastreamento[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!documento) return;
    setLoading(true);
    setError(null);
    buscarRastreamento(documento)
      .then(setData)
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, [documento]);

  return { data, loading, error };
}