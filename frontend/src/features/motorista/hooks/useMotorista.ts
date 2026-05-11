"use client";

import { useState, useEffect, useCallback } from "react";
import {
  buscarPerfil, listarSolicitacoes, buscarPerformanceMotorista,
  type MotoristaPeril, type SolicitacaoTransporte, type PerformanceMotorista,
} from "@/features/motorista/adapters/motoristaService";

export function usePerfil() {
  const [data, setData] = useState<MotoristaPeril | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetch = useCallback(async () => {
    setLoading(true);
    buscarPerfil()
      .then(setData)
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => { fetch(); }, [fetch]);
  return { data, loading, error, refetch: fetch, setData };
}

export function useSolicitacoes() {
  const [data, setData] = useState<SolicitacaoTransporte[]>([]);
  const [loading, setLoading] = useState(true);

  const fetch = useCallback(() => {
    setLoading(true);
    listarSolicitacoes().then(setData).finally(() => setLoading(false));
  }, []);

  useEffect(() => { fetch(); }, [fetch]);
  return { data, loading, refetch: fetch };
}

export function usePerformanceMotorista() {
  const [data, setData] = useState<PerformanceMotorista[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    buscarPerformanceMotorista().then(setData).finally(() => setLoading(false));
  }, []);

  return { data, loading };
}