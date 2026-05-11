"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { post } from "@/core/api/publicHttpClient";
import { useAuthStore, type AuthUser } from "@/core/auth/authStore";

interface LoginResponse {
  id: number;
  nome: string;
  email: string;
  role: AuthUser["role"];
  token: string;
}

export function LoginForm() {
  const router = useRouter();
  const setUsuario = useAuthStore((s) => s.setUsuario);
  const [form, setForm] = useState({ email: "", senha: "" });
  const [showSenha, setShowSenha] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await post<LoginResponse>("/api/auth/login", form);
      setUsuario({ id: res.id, nome: res.nome, email: res.email, role: res.role }, res.token);
      
      await new Promise(resolve => setTimeout(resolve, 100));
      
      router.push(res.role === "MOTORISTA" ? "/motorista/perfil" : "/dashboard");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "E-mail ou senha inválidos.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-5">
      <div className="space-y-2">
        <label
          className="text-xs font-semibold uppercase tracking-wide text-white/50"
          style={{ fontFamily: "var(--font-primary)" }}
        >
          E-mail
        </label>
        <div className="relative">
          <i className="bi bi-envelope absolute left-4 top-1/2 -translate-y-1/2 text-white/30" />
          <input
            type="email"
            autoComplete="email"
            placeholder="seu@email.com"
            value={form.email}
            onChange={(e) => setForm((f) => ({ ...f, email: e.target.value }))}
            required
            className="w-full rounded-xl pl-11 pr-4 py-3.5 text-white text-sm outline-none transition-colors"
            style={{
              backgroundColor: "rgba(255,255,255,0.05)",
              border: "1px solid rgba(255,255,255,0.1)",
              fontFamily: "var(--font-primary)",
            }}
          />
        </div>
      </div>

      <div className="space-y-2">
        <label
          className="text-xs font-semibold uppercase tracking-wide text-white/50"
          style={{ fontFamily: "var(--font-primary)" }}
        >
          Senha
        </label>
        <div className="relative">
          <i className="bi bi-lock absolute left-4 top-1/2 -translate-y-1/2 text-white/30" />
          <input
            type={showSenha ? "text" : "password"}
            autoComplete="current-password"
            placeholder={showSenha ? "suasenhaaqui" : "••••••••"}
            value={form.senha}
            onChange={(e) => setForm((f) => ({ ...f, senha: e.target.value }))}
            required
            className="w-full rounded-xl pl-11 pr-12 py-3.5 text-white text-sm outline-none transition-colors"
            style={{
              backgroundColor: "rgba(255,255,255,0.05)",
              border: "1px solid rgba(255,255,255,0.1)",
              fontFamily: "var(--font-primary)",
            }}
          />
          <button
            type="button"
            onClick={() => setShowSenha((v) => !v)}
            className="absolute right-4 top-1/2 -translate-y-1/2 text-white/30 hover:text-white/60 transition-colors"
          >
            <i className={`bi ${showSenha ? "bi-eye-slash" : "bi-eye"}`} />
          </button>
        </div>
      </div>

      {error && (
        <div
          className="rounded-xl px-4 py-3 flex items-center gap-2"
          style={{ backgroundColor: "rgba(239,68,68,0.1)", border: "1px solid rgba(239,68,68,0.2)" }}
        >
          <i className="bi bi-exclamation-triangle text-red-400 flex-shrink-0" />
          <p className="text-red-400 text-sm" style={{ fontFamily: "var(--font-primary)" }}>{error}</p>
        </div>
      )}

      <button
        type="submit"
        disabled={loading}
        className="w-full py-3.5 text-white font-bold rounded-xl hover:opacity-90 transition-all hover:-translate-y-0.5 disabled:opacity-50 disabled:cursor-not-allowed"
        style={{ backgroundColor: "#005eff", fontFamily: "var(--font-primary)" }}
      >
        {loading ? (
          <span className="flex items-center justify-center gap-2">
            <i className="bi bi-arrow-repeat animate-spin" /> Entrando...
          </span>
        ) : (
          <span className="flex items-center justify-center gap-2">
            <i className="bi bi-box-arrow-in-right" /> Entrar
          </span>
        )}
      </button>
    </form>
  );
}