"use client";

import { useState, useEffect } from "react";
import {
  buscarKpisManutencao, listarManutencoes, buscarManutencao,
  listarOrcamento, salvarOrcamento,
  type ManutencaoKpis, type Manutencao, type ItemOrcamento,
} from "@/features/manutencao/adapters/manutencaoService";

export function useManutencaoKpis() {
  const [kpis, setKpis] = useState<ManutencaoKpis | null>(null);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    buscarKpisManutencao().then(setKpis).finally(() => setLoading(false));
  }, []);
  return { kpis, loading };
}

export function useManutencoes() {
  const [data, setData] = useState<Manutencao[]>([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    listarManutencoes().then(setData).finally(() => setLoading(false));
  }, []);
  return { data, loading };
}

export function useManutencaoDetalhe(id: number) {
  const [manutencao, setManutencao] = useState<Manutencao | null>(null);
  const [itens, setItens] = useState<ItemOrcamento[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([buscarManutencao(id), listarOrcamento(id)])
      .then(([m, orcamento]) => { setManutencao(m); setItens(orcamento); })
      .finally(() => setLoading(false));
  }, [id]);

  const salvar = async (novosItens: ItemOrcamento[]) => {
    const saved = await salvarOrcamento(id, novosItens);
    setItens(saved);
  };

  return { manutencao, itens, loading, salvar };
}