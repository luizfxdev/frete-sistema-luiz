"use client";

import type { Manutencao, ItemOrcamento } from "@/features/manutencao/adapters/manutencaoService";
import { formatCurrency, formatDate } from "@/core/utils/formatters";

interface Props {
  manutencao: Manutencao;
  itens: ItemOrcamento[];
}

export function OrcamentoPdfButton({ manutencao, itens }: Props) {
  const total = itens.reduce((acc, i) => acc + i.quantidade * i.precoUnitario, 0);

  const handlePrint = () => {
    const html = `
      <!DOCTYPE html>
      <html lang="pt-BR">
      <head>
        <meta charset="UTF-8" />
        <title>Orçamento — ${manutencao.placa}</title>
        <style>
          * { box-sizing: border-box; margin: 0; padding: 0; }
          body { font-family: -apple-system, sans-serif; padding: 40px; color: #0a2540; }
          .header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 32px; }
          .logo { font-size: 28px; font-weight: 800; letter-spacing: -1px; }
          .logo span { font-weight: 400; }
          .badge { font-size: 11px; font-weight: 700; color: #fff; background: #005eff; padding: 4px 10px; border-radius: 20px; }
          h2 { font-size: 18px; font-weight: 700; margin-bottom: 16px; border-bottom: 2px solid #005eff; padding-bottom: 8px; }
          .info-grid { display: grid; grid-template-columns: 1fr 1fr 1fr 1fr; gap: 16px; margin-bottom: 32px; background: #f8f6f1; padding: 16px; border-radius: 12px; }
          .info-item label { font-size: 10px; font-weight: 700; text-transform: uppercase; letter-spacing: .05em; color: #666; display: block; margin-bottom: 4px; }
          .info-item p { font-size: 13px; font-weight: 600; }
          table { width: 100%; border-collapse: collapse; }
          th { font-size: 10px; font-weight: 700; text-transform: uppercase; letter-spacing: .05em; color: #666; text-align: left; padding: 8px 12px; background: #f8f6f1; }
          td { padding: 10px 12px; font-size: 13px; border-bottom: 1px solid #eee; }
          tfoot td { font-weight: 700; font-size: 15px; background: #f8f6f1; }
          .footer { margin-top: 40px; font-size: 11px; color: #999; }
          @media print { body { padding: 20px; } }
        </style>
      </head>
      <body>
        <div class="header">
          <div class="logo">Next<span>Log</span></div>
          <span class="badge">ORÇAMENTO DE MANUTENÇÃO</span>
        </div>
        <h2>Dados do Veículo</h2>
        <div class="info-grid">
          <div class="info-item"><label>Veículo</label><p>${manutencao.veiculo}</p></div>
          <div class="info-item"><label>Placa</label><p>${manutencao.placa}</p></div>
          <div class="info-item"><label>Tipo</label><p>${manutencao.tipo}</p></div>
          <div class="info-item"><label>Oficina</label><p>${manutencao.oficina}</p></div>
          <div class="info-item"><label>Início</label><p>${formatDate(manutencao.dataInicio)}</p></div>
          <div class="info-item"><label>Status</label><p>${manutencao.status}</p></div>
        </div>
        <h2>Itens do Orçamento</h2>
        <table>
          <thead><tr><th>Descrição</th><th>Qtd</th><th>Preço Unit.</th><th>Total</th></tr></thead>
          <tbody>
            ${itens.map((i) => `<tr><td>${i.descricao}</td><td>${i.quantidade}</td><td>${formatCurrency(i.precoUnitario)}</td><td>${formatCurrency(i.quantidade * i.precoUnitario)}</td></tr>`).join("")}
          </tbody>
          <tfoot><tr><td colspan="3" style="text-align:right">Total Geral</td><td>${formatCurrency(total)}</td></tr></tfoot>
        </table>
        <div class="footer">Documento gerado em ${new Date().toLocaleString("pt-BR")} · NextLog — Logística que move o Brasil</div>
      </body>
      </html>
    `;
    const win = window.open("", "_blank");
    if (!win) return;
    win.document.write(html);
    win.document.close();
    win.focus();
    setTimeout(() => { win.print(); }, 400);
  };

  return (
    <button
      onClick={handlePrint}
      className="flex items-center gap-2 px-5 py-2.5 border border-white/15 text-white/70 rounded-xl text-sm font-semibold hover:border-accent hover:text-accent transition-all"
    >
      <i className="bi bi-file-earmark-pdf" />
      Emitir PDF
    </button>
  );
}