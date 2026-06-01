'use client';

import * as httpClient from '@/core/api/httpClient';
import type {
  ManutencaoVeiculo,
  OrcamentoManutencaoItem,
  DashboardManutencaoKpis,
} from '@/shared/types/api';

export const manutencaoService = {
  async buscarKpis(): Promise<DashboardManutencaoKpis> {
    try {
      const response = await httpClient.get<DashboardManutencaoKpis>(
        '/api/dashboard/manutencao/kpis'
      );
      return response || {
        emManutencao: 0,
        preventiva: 0,
        corretiva: 0,
        liberados: 0,
      };
    } catch (error) {
      console.error('[manutencaoService] Erro ao buscar KPIs:', error);
      return {
        emManutencao: 0,
        preventiva: 0,
        corretiva: 0,
        liberados: 0,
      };
    }
  },

  async listarManutencoes(
    pagina: number = 1,
    filtro?: string
  ): Promise<ManutencaoVeiculo[]> {
    try {
      const params = new URLSearchParams();
      params.append('pagina', pagina.toString());
      if (filtro) params.append('filtro', filtro);

      const response = await httpClient.get<{ data: ManutencaoVeiculo[] }>(
        `/api/dashboard/manutencao/lista?${params.toString()}`
      );
      return response?.data || [];
    } catch (error) {
      console.error('[manutencaoService] Erro ao listar:', error);
      return [];
    }
  },

  async buscarManutencao(id: number): Promise<{
    manutencao: ManutencaoVeiculo;
    orcamentos: OrcamentoManutencaoItem[];
  } | null> {
    try {
      const response = await httpClient.get<{
        manutencao: ManutencaoVeiculo;
        orcamentos: OrcamentoManutencaoItem[];
      }>(`/api/manutencoes/${id}`);
      return response || null;
    } catch (error) {
      console.error(`[manutencaoService] Erro ao buscar ID ${id}:`, error);
      return null;
    }
  },

  async registrarManutencao(
    manutencao: Omit<
      ManutencaoVeiculo,
      'id' | 'dataCriacao' | 'dataAtualizacao'
    >
  ): Promise<ManutencaoVeiculo | null> {
    try {
      const response = await httpClient.post<ManutencaoVeiculo>(
        '/api/manutencoes',
        manutencao
      );
      return response || null;
    } catch (error) {
      console.error('[manutencaoService] Erro ao registrar:', error);
      throw error;
    }
  },

  async liberarManutencao(id: number): Promise<void> {
    try {
      await httpClient.patch(
        `/api/manutencoes/${id}/liberar`,
        {}
      );
    } catch (error) {
      console.error('[manutencaoService] Erro ao liberar:', error);
      throw error;
    }
  },

  async cancelarManutencao(id: number): Promise<void> {
    try {
      await httpClient.patch(
        `/api/manutencoes/${id}/cancelar`,
        {}
      );
    } catch (error) {
      console.error('[manutencaoService] Erro ao cancelar:', error);
      throw error;
    }
  },

  async listarOrcamentos(
    idManutencao: number
  ): Promise<OrcamentoManutencaoItem[]> {
    try {
      const response = await httpClient.get<OrcamentoManutencaoItem[]>(
        `/api/orcamentos/manutencao/${idManutencao}`
      );
      return response || [];
    } catch (error) {
      console.error(
        `[manutencaoService] Erro ao listar orçamentos de ${idManutencao}:`,
        error
      );
      return [];
    }
  },

  async salvarOrcamento(
    item: OrcamentoManutencaoItem
  ): Promise<OrcamentoManutencaoItem | null> {
    try {
      if (item.id) {
        const response = await httpClient.put<OrcamentoManutencaoItem>(
          `/api/orcamentos/${item.id}`,
          item
        );
        return response || null;
      }
      const response = await httpClient.post<OrcamentoManutencaoItem>(
        '/api/orcamentos',
        item
      );
      return response || null;
    } catch (error) {
      console.error('[manutencaoService] Erro ao salvar orçamento:', error);
      throw error;
    }
  },

  async deletarOrcamento(id: number): Promise<void> {
    try {
      await httpClient.del(`/api/orcamentos/${id}`);
    } catch (error) {
      console.error('[manutencaoService] Erro ao deletar orçamento:', error);
      throw error;
    }
  },
};