"use client";

import { useState } from "react";
import { z } from "zod";
import { useCandidatura } from "@/features/trabalhe-conosco/hooks/useCandidatura";
import { ConfirmacaoModal } from "./ConfirmacaoModal";
import { stripMask } from "@/core/utils/formatters";

const schema = z.object({
  nome: z.string().min(3, "Nome muito curto"),
  cpf: z.string().length(11, "CPF inválido"),
  dataNascimento: z.string().min(1, "Obrigatório"),
  telefone: z.string().min(10, "Telefone inválido"),
  email: z.string().email("E-mail inválido"),
  cnhNumero: z.string().min(5, "Número inválido"),
  cnhCategoria: z.enum(["A", "B", "C", "D", "E"]),
  cnhValidade: z.string().min(1, "Obrigatório"),
  tipoVinculo: z.enum(["FUNCIONARIO", "AGREGADO", "TERCEIRO"]),
  municipio: z.string().min(2, "Obrigatório"),
  uf: z.string().length(2, "UF inválida"),
  mensagem: z.string().optional(),
});

type FormData = z.infer<typeof schema>;

const INIT: FormData = {
  nome: "", cpf: "", dataNascimento: "", telefone: "", email: "",
  cnhNumero: "", cnhCategoria: "B", cnhValidade: "",
  tipoVinculo: "FUNCIONARIO", municipio: "", uf: "", mensagem: "",
};

const inputClass = (hasError: boolean) =>
  `w-full rounded-xl px-4 py-3 text-sm outline-none transition-colors bg-white/5 border text-white placeholder-white/25 ${
    hasError
      ? "border-red-400/60 focus:border-red-400"
      : "border-white/10 focus:border-white/30"
  }`;

const selectClass =
  "w-full rounded-xl px-4 py-3 text-sm outline-none transition-colors bg-gray-900 border border-white/10 focus:border-white/30 text-white";

const labelClass = "text-xs font-semibold uppercase tracking-wide text-white/50";

export function CandidaturaForm() {
  const { loading, sucesso, error, enviar } = useCandidatura();
  const [form, setForm] = useState<FormData>(INIT);
  const [errors, setErrors] = useState<Partial<Record<keyof FormData, string>>>({});

  const set = (k: keyof FormData, v: string) =>
    setForm((f) => ({ ...f, [k]: v }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const parsed = schema.safeParse({
      ...form,
      cpf: stripMask(form.cpf),
      telefone: stripMask(form.telefone),
    });
    if (!parsed.success) {
      const errs: typeof errors = {};
      parsed.error.errors.forEach((e) => {
        if (e.path[0]) errs[e.path[0] as keyof FormData] = e.message;
      });
      setErrors(errs);
      return;
    }
    setErrors({});
    await enviar(parsed.data);
  };

  const field = (
    label: string,
    key: keyof FormData,
    props: React.InputHTMLAttributes<HTMLInputElement> = {}
  ) => (
    <div className="space-y-1.5">
      <label className={labelClass}>{label}</label>
      <input
        {...props}
        value={form[key] as string}
        onChange={(e) => set(key, e.target.value)}
        className={inputClass(!!errors[key])}
      />
      {errors[key] && <p className="text-xs text-red-400">{errors[key]}</p>}
    </div>
  );

  return (
    <>
      {sucesso && <ConfirmacaoModal onClose={() => (window.location.href = "/")} />}

      <form onSubmit={handleSubmit} className="space-y-5">
        {field("Nome completo", "nome", { placeholder: "Seu nome completo" })}

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {field("CPF", "cpf", { placeholder: "000.000.000-00", maxLength: 14 })}
          {field("Data de nascimento", "dataNascimento", { type: "date" })}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {field("Telefone", "telefone", { placeholder: "(00) 00000-0000" })}
          {field("E-mail", "email", { type: "email", placeholder: "seu@email.com" })}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {field("Número da CNH", "cnhNumero", { placeholder: "00000000000" })}
          <div className="space-y-1.5">
            <label className={labelClass}>Categoria CNH</label>
            <select
              value={form.cnhCategoria}
              onChange={(e) => set("cnhCategoria", e.target.value)}
              className={selectClass}
            >
              {["A", "B", "C", "D", "E"].map((c) => (
                <option key={c}>{c}</option>
              ))}
            </select>
          </div>
          {field("Validade CNH", "cnhValidade", { type: "date" })}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="space-y-1.5">
            <label className={labelClass}>Tipo de vínculo</label>
            <select
              value={form.tipoVinculo}
              onChange={(e) => set("tipoVinculo", e.target.value)}
              className={selectClass}
            >
              <option value="FUNCIONARIO">Funcionário</option>
              <option value="AGREGADO">Agregado</option>
              <option value="TERCEIRO">Terceiro</option>
            </select>
          </div>
          {field("Município", "municipio", { placeholder: "Recife" })}
          {field("UF", "uf", { placeholder: "PE", maxLength: 2 })}
        </div>

        <div className="space-y-1.5">
          <label className={labelClass}>Mensagem (opcional)</label>
          <textarea
            value={form.mensagem}
            onChange={(e) => set("mensagem", e.target.value)}
            placeholder="Conte um pouco sobre você e suas experiências..."
            rows={4}
            className="w-full bg-white/5 border border-white/10 rounded-xl px-4 py-3 text-sm outline-none focus:border-white/30 resize-none transition-colors text-white placeholder-white/25"
          />
        </div>

        {error && (
          <p className="text-red-400 text-sm bg-red-500/10 border border-red-500/20 rounded-xl px-4 py-3">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={loading}
          className="w-full py-3.5 text-white font-bold rounded-xl hover:opacity-90 transition-all hover:-translate-y-0.5 hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed"
          style={{ backgroundColor: "#005eff" }}
        >
          {loading ? (
            <span className="flex items-center justify-center gap-2">
              <i className="bi bi-arrow-repeat animate-spin" /> Enviando...
            </span>
          ) : (
            <span className="flex items-center justify-center gap-2">
              <i className="bi bi-bag-check-fill" /> Enviar candidatura
            </span>
          )}
        </button>
      </form>
    </>
  );
}