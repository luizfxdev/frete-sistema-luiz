"use client";

import { usePerfil, useSolicitacoes } from "@/features/motorista/hooks/useMotorista";
import { PerfilCard } from "@/features/motorista/components/PerfilCard";
import { FotoUpload } from "@/features/motorista/components/FotoUpload";
import { CnhUpload } from "@/features/motorista/components/CnhUpload";
import { DisponibilidadeToggle } from "@/features/motorista/components/DisponibilidadeToggle";
import { SolicitacoesTable } from "@/features/motorista/components/SolicitacoesTable";

function Skeleton({ h = "h-32" }: { h?: string }) {
  return <div className={`${h} rounded-2xl bg-white/5 animate-pulse`} />;
}

export default function MotoristaPerfilPage() {
  const { data: perfil, loading, setData } = usePerfil();
  const { data: solicitacoes, loading: loadingSol, refetch } = useSolicitacoes();

  return (
    <div className="space-y-6 max-w-3xl">
      <div>
        <h2 className="text-2xl font-bold text-white mb-1">Meu Perfil</h2>
        <p className="text-white/40 text-sm">Gerencie seus dados e disponibilidade.</p>
      </div>

      {loading ? (
        <><Skeleton h="h-40" /><Skeleton /><Skeleton /></>
      ) : perfil ? (
        <>
          <PerfilCard perfil={perfil} />

          <DisponibilidadeToggle
            inicial={perfil.disponivel}
            onChange={(v) => setData((p) => p ? { ...p, disponivel: v } : p)}
          />

          <div className="bg-white/5 border border-white/7 rounded-2xl p-6 space-y-5">
            <h3 className="text-sm font-bold text-white">Documentos</h3>
            <div className="flex flex-wrap gap-6">
              <FotoUpload
                currentUrl={perfil.fotoUrl}
                onSuccess={(url) => setData((p) => p ? { ...p, fotoUrl: url } : p)}
              />
              <CnhUpload
                currentUrl={perfil.cnhFotoUrl}
                onSuccess={(url) => setData((p) => p ? { ...p, cnhFotoUrl: url } : p)}
              />
            </div>
          </div>

          <SolicitacoesTable
            solicitacoes={solicitacoes}
            onResposta={refetch}
          />
        </>
      ) : null}
    </div>
  );
}