import Image from "next/image";
import type { MotoristaPeril } from "@/features/motorista/adapters/motoristaService";
import { formatDate } from "@/core/utils/formatters";

interface Props {
  perfil: MotoristaPeril;
}

export function PerfilCard({ perfil }: Props) {
  return (
    <div className="bg-white/5 border border-white/7 rounded-2xl p-6 flex flex-col sm:flex-row gap-6 items-start">
      <div className="relative flex-shrink-0">
        {perfil.fotoUrl ? (
          <Image
            src={perfil.fotoUrl}
            alt={perfil.nome}
            width={88}
            height={88}
            className="w-22 h-22 rounded-xl object-cover"
          />
        ) : (
          <div className="w-22 h-22 w-[88px] h-[88px] rounded-xl bg-accent/15 flex items-center justify-center text-accent text-3xl font-bold">
            {perfil.nome.charAt(0)}
          </div>
        )}
        <span
          className={`absolute -bottom-2 -right-2 text-[10px] font-bold px-2 py-0.5 rounded-full ${
            perfil.status === "ATIVO"
              ? "bg-success text-white"
              : "bg-red-500 text-white"
          }`}
        >
          {perfil.status}
        </span>
      </div>

      <div className="flex-1 min-w-0">
        <h2 className="text-xl font-bold text-white mb-0.5">{perfil.nome}</h2>
        <p className="text-white/40 text-sm mb-4">{perfil.cpf}</p>
        <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
          {[
            { label: "CNH", value: perfil.cnhNumero },
            { label: "Categoria", value: `Categoria ${perfil.cnhCategoria}` },
            { label: "Validade CNH", value: formatDate(perfil.cnhValidade) },
          ].map(({ label, value }) => (
            <div key={label}>
              <p className="text-xs text-white/30 mb-0.5">{label}</p>
              <p className="text-sm font-semibold text-white">{value}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}