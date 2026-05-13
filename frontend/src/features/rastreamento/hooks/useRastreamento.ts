"use client";
import { useState, useEffect } from "react";
import { buscarRastreamento, type RastreamentoResponse } from "@/features/rastreamento/adapters/rastreamentoService";

export function useRastreamento(parametro: string) {
    const [data, setData] = useState<RastreamentoResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!parametro || !parametro.match(/^FRT-\d{4}-\d{5}$/)) {
            setError("Número de frete inválido");
            setLoading(false);
            return;
        }

        setLoading(true);
        setError(null);

        buscarRastreamento(parametro)
            .then(setData)
            .catch((e: Error) => setError(e.message))
            .finally(() => setLoading(false));
    }, [parametro]);

    return { data, loading, error };
}