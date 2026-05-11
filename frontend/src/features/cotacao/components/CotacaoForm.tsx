"use client";

import { useState } from "react";
import { buscarCep } from "@/core/utils/cep";

type Modalidade = "TERRESTRE" | "AEREO";
type TipoEntrega = "DOMICILIO" | "AGENCIA";

interface CepField {
  cep: string;
  municipio: string;
  uf: string;
}

interface ResultadoSimulacao {
  valor: number;
  prazo: string;
  modalidade: Modalidade;
  seguro: number;
  total: number;
}

const REGIOES: Record<string, string> = {
  SP: "SE", RJ: "SE", MG: "SE", ES: "SE",
  RS: "S",  PR: "S",  SC: "S",
  BA: "NE", PE: "NE", CE: "NE", RN: "NE", PB: "NE",
  SE: "NE", AL: "NE", PI: "NE", MA: "NE",
  GO: "CO", MT: "CO", MS: "CO", DF: "CO",
  PA: "N",  AM: "N",  RO: "N",  AC: "N",  RR: "N",  AP: "N",  TO: "N",
};

const UFS_VIZINHAS: Record<string, string[]> = {
  SP: ["RJ", "MG", "PR", "MS"],
  RJ: ["SP", "MG", "ES"],
  MG: ["SP", "RJ", "ES", "BA", "GO", "MS"],
  PR: ["SP", "SC", "MS"],
  SC: ["PR", "RS"],
  RS: ["SC"],
  BA: ["MG", "ES", "SE", "AL", "PE", "PI", "GO", "TO"],
  PE: ["BA", "AL", "PB", "CE", "PI"],
  CE: ["PE", "PB", "RN", "PI", "MA"],
  GO: ["MG", "BA", "TO", "MT", "MS", "DF"],
};

function pesoTaxavel(pesoKg: number, largCm: number, altCm: number, compCm: number): number {
  const cubagem = (largCm * altCm * compCm) / 6000;
  return Math.max(pesoKg, cubagem);
}

function calcularFrete(
  ufOrigem: string,
  ufDestino: string,
  pesoKg: number,
  largCm: number,
  altCm: number,
  compCm: number,
  valorMercadoria: number,
  valorColeta: number,
  modalidade: Modalidade,
  fretePago: boolean,
  tipoEntrega: TipoEntrega
): ResultadoSimulacao {
  const pesoCobrado = pesoTaxavel(pesoKg, largCm, altCm, compCm);
  const mesmaUF = ufOrigem === ufDestino;
  const vizinhas = UFS_VIZINHAS[ufOrigem]?.includes(ufDestino) ?? false;
  const mesmaRegiao = REGIOES[ufOrigem] === REGIOES[ufDestino];
  const regiaoO = REGIOES[ufOrigem];
  const regiaoD = REGIOES[ufDestino];
  const norteSul =
    (regiaoO === "N" && regiaoD === "S") || (regiaoO === "S" && regiaoD === "N");

  let adicional = 0;
  if (mesmaUF) adicional = 0;
  else if (vizinhas) adicional = 0.20;
  else if (mesmaRegiao) adicional = 0.35;
  else if (norteSul) adicional = 0.60;
  else adicional = 0.45;

  let frete = 0;
  let prazo = "";

  if (modalidade === "TERRESTRE") {
    const base = 15;
    const porKg = pesoCobrado <= 30
      ? pesoCobrado * 2.5
      : 30 * 2.5 + (pesoCobrado - 30) * 1.8;
    frete = (base + porKg) * (1 + adicional);
    if (mesmaUF) prazo = "2–3 dias úteis";
    else if (vizinhas || mesmaRegiao) prazo = "3–5 dias úteis";
    else prazo = "5–8 dias úteis";
  } else {
    const base = 35;
    const porKg = pesoCobrado <= 10
      ? pesoCobrado * 6
      : 10 * 6 + (pesoCobrado - 10) * 4.5;
    frete = (base + porKg) * (1 + adicional * 1.5);
    prazo = "1–2 dias úteis";
  }

  if (tipoEntrega === "DOMICILIO") frete += 8;
  if (!fretePago) frete += valorColeta;

  const seguro = valorMercadoria > 0 ? valorMercadoria * 0.003 : 0;
  const total = frete + seguro;

  return { valor: frete, prazo, modalidade, seguro, total };
}

const inputCls =
  "w-full bg-white/5 border border-white/10 rounded-xl px-4 py-3 text-white placeholder-white/25 text-sm outline-none focus:border-white/30 transition-colors";
const labelCls = "text-xs font-semibold uppercase tracking-wide text-white/50";
const selectCls =
  "w-full bg-gray-900 border border-white/10 rounded-xl px-4 py-3 text-white text-sm outline-none focus:border-white/30 transition-colors";

export function CotacaoForm() {
  const [origem, setOrigem] = useState<CepField>({ cep: "", municipio: "", uf: "" });
  const [destino, setDestino] = useState<CepField>({ cep: "", municipio: "", uf: "" });
  const [peso, setPeso] = useState("1");
  const [valorMerc, setValorMerc] = useState("0");
  const [valorColeta, setValorColeta] = useState("0");
  const [largura, setLargura] = useState("0");
  const [altura, setAltura] = useState("0");
  const [comprimento, setComprimento] = useState("0");
  const [modalidade, setModalidade] = useState<Modalidade>("TERRESTRE");
  const [fretePago, setFretePago] = useState(true);
  const [entrega, setEntrega] = useState<TipoEntrega>("DOMICILIO");
  const [resultado, setResultado] = useState<ResultadoSimulacao | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleCep = async (valor: string, campo: "origem" | "destino") => {
    const set = campo === "origem" ? setOrigem : setDestino;
    set((prev) => ({ ...prev, cep: valor }));
    if (valor.replace(/\D/g, "").length === 8) {
      const end = await buscarCep(valor);
      if (end) set({ cep: valor, municipio: end.localidade, uf: end.uf });
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    if (!origem.uf || !destino.uf) {
      setError("Informe CEPs válidos de origem e destino.");
      return;
    }
    if (Number(peso) <= 0) {
      setError("Informe o peso da mercadoria.");
      return;
    }
    setLoading(true);
    await new Promise((r) => setTimeout(r, 500));
    const sim = calcularFrete(
      origem.uf,
      destino.uf,
      Number(peso),
      Number(largura),
      Number(altura),
      Number(comprimento),
      Number(valorMerc),
      Number(valorColeta),
      modalidade,
      fretePago,
      entrega
    );
    setResultado(sim);
    setLoading(false);
  };

  return (
    <div className="space-y-6">
      <form onSubmit={handleSubmit} className="space-y-5">
        {/* CEPs */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {(["origem", "destino"] as const).map((campo) => {
            const state = campo === "origem" ? origem : destino;
            return (
              <div key={campo} className="space-y-1.5">
                <label className={labelCls}>CEP de {campo === "origem" ? "Origem" : "Destino"}</label>
                <input
                  type="text"
                  placeholder="00000-000"
                  maxLength={9}
                  value={state.cep}
                  onChange={(e) => handleCep(e.target.value, campo)}
                  className={inputCls}
                />
                {state.municipio && (
                  <p className="text-xs font-medium" style={{ color: "#005eff" }}>
                    <i className="bi bi-geo-alt-fill mr-1" />
                    {state.municipio} — {state.uf}
                  </p>
                )}
              </div>
            );
          })}
        </div>

        {/* Peso e Valor Mercadoria */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="space-y-1.5">
            <label className={labelCls}>Peso (kg)</label>
            <input
              type="number" min="0.1" step="0.1" placeholder="1"
              value={peso} onChange={(e) => setPeso(e.target.value)}
              className={inputCls}
            />
          </div>
          <div className="space-y-1.5">
            <label className={labelCls}>Valor da Mercadoria (R$)</label>
            <input
              type="number" min="0" step="0.01" placeholder="0"
              value={valorMerc} onChange={(e) => setValorMerc(e.target.value)}
              className={inputCls}
            />
          </div>
        </div>

        {/* Valor Coleta + Modalidade */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="space-y-1.5">
            <label className={labelCls}>Valor Coleta (R$)</label>
            <input
              type="number" min="0" step="0.01" placeholder="0"
              value={valorColeta} onChange={(e) => setValorColeta(e.target.value)}
              className={inputCls}
            />
          </div>
          <div className="space-y-1.5">
            <label className={labelCls}>Modalidade de Frete</label>
            <select
              value={modalidade}
              onChange={(e) => setModalidade(e.target.value as Modalidade)}
              className={selectCls}
            >
              <option value="TERRESTRE">Frete Terrestre</option>
              <option value="AEREO">Frete Aéreo</option>
            </select>
          </div>
        </div>

        {/* Dimensões */}
        <div className="grid grid-cols-3 gap-4">
          <div className="space-y-1.5">
            <label className={labelCls}>Larg. (cm)</label>
            <input type="number" min="0" placeholder="0" value={largura} onChange={(e) => setLargura(e.target.value)} className={inputCls} />
          </div>
          <div className="space-y-1.5">
            <label className={labelCls}>Altura (cm)</label>
            <input type="number" min="0" placeholder="0" value={altura} onChange={(e) => setAltura(e.target.value)} className={inputCls} />
          </div>
          <div className="space-y-1.5">
            <label className={labelCls}>Compr. (cm)</label>
            <input type="number" min="0" placeholder="0" value={comprimento} onChange={(e) => setComprimento(e.target.value)} className={inputCls} />
          </div>
        </div>

        {/* Frete a pagar + Entrega */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="space-y-1.5">
            <label className={labelCls}>Frete a pagar</label>
            <div className="flex gap-6 pt-2">
              {[{ v: true, l: "Sim" }, { v: false, l: "Não" }].map(({ v, l }) => (
                <label key={l} className="flex items-center gap-2 cursor-pointer text-sm text-white/70">
                  <input
                    type="radio"
                    checked={fretePago === v}
                    onChange={() => setFretePago(v)}
                    className="accent-blue-500"
                  />
                  {l}
                </label>
              ))}
            </div>
          </div>
          <div className="space-y-1.5">
            <label className={labelCls}>Entrega</label>
            <select
              value={entrega}
              onChange={(e) => setEntrega(e.target.value as TipoEntrega)}
              className={selectCls}
            >
              <option value="DOMICILIO">Domicílio (+R$ 8,00)</option>
              <option value="AGENCIA">Retirada na Agência</option>
            </select>
          </div>
        </div>

        {error && (
          <p className="text-red-400 text-sm bg-red-500/10 border border-red-500/20 rounded-xl px-4 py-3">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={loading}
          className="w-full py-3.5 text-white font-bold rounded-xl hover:opacity-90 transition-all hover:-translate-y-0.5 disabled:opacity-50 disabled:cursor-not-allowed"
          style={{ backgroundColor: "#005eff" }}
        >
          {loading ? (
            <span className="flex items-center justify-center gap-2">
              <i className="bi bi-arrow-repeat animate-spin" /> Calculando...
            </span>
          ) : (
            <span className="flex items-center justify-center gap-2">
              <i className="bi bi-calculator-fill" /> Simular
            </span>
          )}
        </button>
      </form>

      {resultado && (
        <div
          className="rounded-2xl p-6 space-y-4 border"
          style={{ backgroundColor: "rgba(255,255,255,0.05)", borderColor: "rgba(255,255,255,0.1)" }}
        >
          <p
            className="text-white font-bold text-base"
            style={{ fontFamily: "var(--font-primary)" }}
          >
            Resultado da Simulação — {resultado.modalidade === "TERRESTRE" ? "Terrestre" : "Aéreo"}
          </p>
          <div className="grid grid-cols-3 gap-3">
            {[
              { label: "Frete", value: `R$ ${resultado.valor.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}` },
              { label: "Seguro AD Valorem", value: `R$ ${resultado.seguro.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}` },
              { label: "Total", value: `R$ ${resultado.total.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}` },
            ].map((item) => (
              <div
                key={item.label}
                className="rounded-xl p-3 text-center"
                style={{ backgroundColor: "rgba(255,255,255,0.05)" }}
              >
                <p className="text-white/40 text-xs mb-1" style={{ fontFamily: "var(--font-primary)" }}>{item.label}</p>
                <p className="font-bold text-white text-sm" style={{ fontFamily: "var(--font-primary)" }}>{item.value}</p>
              </div>
            ))}
          </div>
          <p className="text-white/30 text-xs" style={{ fontFamily: "var(--font-primary)" }}>
            Prazo estimado: {resultado.prazo} &nbsp;•&nbsp; Valores sujeitos a confirmação.
          </p>
        </div>
      )}
    </div>
  );
}