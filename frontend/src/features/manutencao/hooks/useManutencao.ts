'use client';

import { useState, useEffect } from 'react';
import { manutencaoService } from '../adapters/manutencaoService';
import type {
  ManutencaoVeiculo,
  OrcamentoManutencaoItem,
  DashboardManutencaoKpis,
} from '@/shared/types/api';

export function useManutencaoKpis() {
  const [kpis, setKpis] = useState<DashboardManutencaoKpis | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const carregarKpis = async () => {
      try {
        setLoading(true);
        const dados = await manutencaoService.buscarKpis();
        setKpis(dados);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Erro ao carregar KPIs');
      } finally {
        setLoading(false);
      }
    };
    carregarKpis();
  }, []);

  return { kpis, loading, error };
}

export function useManutencoes() {
  const [data, setData] = useState<ManutencaoVeiculo[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const listar = async () => {
      try {
        setLoading(true);
        const manutencoes = await manutencaoService.listarManutencoes();
        setData(manutencoes || []);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Erro ao listar manutenções');
      } finally {
        setLoading(false);
      }
    };
    listar();
  }, []);

  return { data, loading, error };
}

export function useManutencaoDetalhe(id: number) {
  const [manutencao, setManutencao] = useState<ManutencaoVeiculo | null>(null);
  const [itens, setItens] = useState<OrcamentoManutencaoItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;

    const carregar = async () => {
      try {
        setLoading(true);
        setError(null);

        const resultado = await manutencaoService.buscarManutencao(id);

        if (resultado) {
          setManutencao(resultado.manutencao);
          const itensFlat = resultado.orcamentos || [];
          setItens(itensFlat);
        } else {
          setError('Manutenção não encontrada');
        }
      } catch (err) {
        setError(
          err instanceof Error ? err.message : 'Erro ao carregar manutenção'
        );
      } finally {
        setLoading(false);
      }
    };

    carregar();
  }, [id]);

  const salvar = async (item: OrcamentoManutencaoItem) => {
    try {
      await manutencaoService.salvarOrcamento(item);
      const resultado = await manutencaoService.buscarManutencao(id);
      if (resultado) {
        setManutencao(resultado.manutencao);
        setItens(resultado.orcamentos || []);
      }
    } catch (err) {
      console.error('Erro ao salvar orçamento:', err);
      throw err;
    }
  };

  const liberar = async () => {
    try {
      await manutencaoService.liberarManutencao(id);
      const resultado = await manutencaoService.buscarManutencao(id);
      if (resultado) {
        setManutencao(resultado.manutencao);
      }
    } catch (err) {
      console.error('Erro ao liberar manutenção:', err);
      throw err;
    }
  };

  const cancelar = async () => {
    try {
      await manutencaoService.cancelarManutencao(id);
      const resultado = await manutencaoService.buscarManutencao(id);
      if (resultado) {
        setManutencao(resultado.manutencao);
      }
    } catch (err) {
      console.error('Erro ao cancelar manutenção:', err);
      throw err;
    }
  };

  return { manutencao, itens, loading, error, salvar, liberar, cancelar };
}