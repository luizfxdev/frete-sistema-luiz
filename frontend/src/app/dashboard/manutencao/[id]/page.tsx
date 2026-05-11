"use client";

import { use } from "react";
import Link from "next/link";
import { useManutencaoDetalhe } from "@/features/manutencao/hooks/useManutencao";
import { ManutencaoDetalhes } from "@/features/manutencao/components/ManutencaoDetalhes";
import { OrcamentoTable } from "@/features/manutencao/components/OrcamentoTable";
import { OrcamentoPdfButton } from "@/features/manutencao/components/OrcamentoPdfButton";

interface Props {
  params: Promise<{ id: string }>;
}

export default function ManutencaoDetalhePage({ params }: Props) {
  const { id } = use(params);
  const { manutencao, itens, loading, salvar } = useManutencaoDetalhe(Number(id));

  if (loading) {
    return (
      <div className="space-y-4 max-w-4xl">
        {[1, 2, 3].map((i) => <div key={i} className="h-32 rounded-2xl bg-white/5 animate-pulse" />)}
      </div>
    );
  }

  if (!manutencao) {
    return (
      <div className="text-center py-20 text-white/30">
        <i className="bi bi-exclamation-circle text-5xl mb-4 block" />
        <p>Manutenção não encontrada.</p>
      </div>
    );
  }

  return (
    <div className="space-y-6 max-w-4xl">
      <div className="flex items-center gap-3">
        <Link
          href="/dashboard/manutencao"
          className="text-white/40 hover:text-white transition-colors text-sm"
        >
          <i className="bi bi-arrow-left mr-1" />
          Voltar
        </Link>
      </div>

      <ManutencaoDetalhes manutencao={manutencao} />

      <div className="flex items-center justify-between">
        <h3 className="text-base font-bold text-white">Orçamento</h3>
        <OrcamentoPdfButton manutencao={manutencao} itens={itens} />
      </div>

      <OrcamentoTable itens={itens} onSalvar={salvar} />
    </div>
  );
}