"use client";

import { useState, useRef } from "react";
import Image from "next/image";
import { uploadCnhFoto } from "@/features/motorista/adapters/motoristaService";

interface Props {
  currentUrl?: string | null;
  onSuccess: (url: string) => void;
}

export function CnhUpload({ currentUrl, onSuccess }: Props) {
  const [preview, setPreview] = useState<string | null>(currentUrl ?? null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const handleFile = async (file: File) => {
    const valid = ["image/jpeg", "image/png", "image/webp", "application/pdf"];
    if (!valid.includes(file.type)) { setError("Use JPG, PNG, WEBP ou PDF."); return; }
    if (file.size > 10 * 1024 * 1024) { setError("Máximo 10 MB."); return; }
    setError(null);
    if (file.type !== "application/pdf") {
      const reader = new FileReader();
      reader.onload = (e) => setPreview(e.target?.result as string);
      reader.readAsDataURL(file);
    } else {
      setPreview("pdf");
    }
    setLoading(true);
    try {
      const { url } = await uploadCnhFoto(file);
      onSuccess(url);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Erro no upload.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-2">
      <p className="text-xs font-semibold uppercase tracking-wide text-white/40">Foto da CNH</p>
      <div
        onClick={() => inputRef.current?.click()}
        onDrop={(e) => { e.preventDefault(); const f = e.dataTransfer.files[0]; if (f) handleFile(f); }}
        onDragOver={(e) => e.preventDefault()}
        className="relative w-48 h-28 rounded-xl border-2 border-dashed border-white/15 hover:border-accent/50 cursor-pointer flex items-center justify-center overflow-hidden transition-colors group"
      >
        {preview && preview !== "pdf" ? (
          <Image src={preview} alt="CNH" fill className="object-cover" />
        ) : preview === "pdf" ? (
          <div className="flex flex-col items-center gap-1 text-success">
            <i className="bi bi-file-earmark-check text-2xl" />
            <span className="text-[10px] font-semibold">PDF enviado</span>
          </div>
        ) : (
          <div className="flex flex-col items-center gap-1 text-white/30 group-hover:text-accent transition-colors">
            <i className="bi bi-credit-card-2-front text-2xl" />
            <span className="text-[10px]">Frente da CNH</span>
          </div>
        )}
        {loading && (
          <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
            <i className="bi bi-arrow-repeat animate-spin text-white text-xl" />
          </div>
        )}
      </div>
      {error && <p className="text-xs text-red-400">{error}</p>}
      <input ref={inputRef} type="file" accept="image/*,application/pdf" className="hidden" onChange={(e) => { const f = e.target.files?.[0]; if (f) handleFile(f); }} />
    </div>
  );
}