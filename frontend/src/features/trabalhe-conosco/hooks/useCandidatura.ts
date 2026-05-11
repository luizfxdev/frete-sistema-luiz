"use client";

import { useState } from "react";
import { enviarCandidatura, type CandidaturaPayload } from "@/features/trabalhe-conosco/adapters/candidaturaService";

export function useCandidatura() {
  const [loading, setLoading] = useState(false);
  const [sucesso, setSucesso] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const enviar = async (payload: CandidaturaPayload) => {
    setLoading(true);
    setError(null);
    try {
      await enviarCandidatura(payload);
      setSucesso(true);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Erro ao enviar candidatura.");
    } finally {
      setLoading(false);
    }
  };

  return { loading, sucesso, error, enviar };
}