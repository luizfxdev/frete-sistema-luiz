"use client";

import { useState } from "react";
import { setDisponibilidade } from "@/features/motorista/adapters/motoristaService";

interface Props {
  inicial: boolean;
  onChange?: (v: boolean) => void;
}

export function DisponibilidadeToggle({ inicial, onChange }: Props) {
  const [ativo, setAtivo] = useState(inicial);
  const [loading, setLoading] = useState(false);

  const toggle = async () => {
    setLoading(true);
    try {
      const novo = !ativo;
      await setDisponibilidade(novo);
      setAtivo(novo);
      onChange?.(novo);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-between bg-white/5 border border-white/7 rounded-2xl px-6 py-4">
      <div>
        <p className="text-xs font-bold uppercase tracking-widest text-white/35 mb-1">Disponibilidade</p>
        <p className={`font-bold text-lg ${ativo ? "text-success" : "text-white/40"}`}>
          {ativo ? "Disponível para viagens" : "Indisponível"}
        </p>
      </div>
      <button
        onClick={toggle}
        disabled={loading}
        aria-label="Alterar disponibilidade"
        className={`relative w-14 h-7 rounded-full transition-all duration-300 disabled:opacity-50 ${
          ativo ? "bg-success" : "bg-white/15"
        }`}
      >
        <span
          className={`absolute top-0.5 w-6 h-6 rounded-full bg-white shadow transition-all duration-300 ${
            ativo ? "left-[calc(100%-26px)]" : "left-0.5"
          }`}
        />
        {loading && (
          <span className="absolute inset-0 flex items-center justify-center">
            <i className="bi bi-arrow-repeat animate-spin text-white text-xs" />
          </span>
        )}
      </button>
    </div>
  );
}