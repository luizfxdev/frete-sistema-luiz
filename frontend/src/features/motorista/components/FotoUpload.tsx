"use client";

import { useState, useRef } from "react";
import Image from "next/image";
import { uploadFoto } from "@/features/motorista/adapters/motoristaService";

interface Props {
  currentUrl?: string | null;
  onSuccess: (url: string) => void;
}

export function FotoUpload({ currentUrl, onSuccess }: Props) {
  const [preview, setPreview] = useState<string | null>(currentUrl ?? null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const handleFile = async (file: File) => {
    if (!file.type.startsWith("image/")) { setError("Somente imagens são aceitas."); return; }
    if (file.size > 5 * 1024 * 1024) { setError("Tamanho máximo: 5 MB."); return; }
    setError(null);
    const reader = new FileReader();
    reader.onload = (e) => setPreview(e.target?.result as string);
    reader.readAsDataURL(file);
    setLoading(true);
    try {
      const { url } = await uploadFoto(file);
      onSuccess(url);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Erro no upload.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-2">
      <p className="text-xs font-semibold uppercase tracking-wide text-white/40">Foto de perfil</p>
      <div
        onClick={() => inputRef.current?.click()}
        onDrop={(e) => { e.preventDefault(); const f = e.dataTransfer.files[0]; if (f) handleFile(f); }}
        onDragOver={(e) => e.preventDefault()}
        className="relative w-32 h-32 rounded-xl border-2 border-dashed border-white/15 hover:border-accent/50 cursor-pointer flex items-center justify-center overflow-hidden transition-colors group"
      >
        {preview ? (
          <Image src={preview} alt="Foto" fill className="object-cover" />
        ) : (
          <div className="flex flex-col items-center gap-1 text-white/30 group-hover:text-accent transition-colors">
            <i className="bi bi-camera text-2xl" />
            <span className="text-[10px]">Adicionar foto</span>
          </div>
        )}
        {loading && (
          <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
            <i className="bi bi-arrow-repeat animate-spin text-white text-xl" />
          </div>
        )}
      </div>
      {error && <p className="text-xs text-red-400">{error}</p>}
      <input ref={inputRef} type="file" accept="image/*" className="hidden" onChange={(e) => { const f = e.target.files?.[0]; if (f) handleFile(f); }} />
    </div>
  );
}