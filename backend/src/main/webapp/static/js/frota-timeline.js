document.addEventListener("DOMContentLoaded", function () {
  const timelineEl = document.getElementById("status-timeline");
  if (!timelineEl) return;

  const currentStatus = timelineEl.getAttribute("data-status") || "EMITIDO";

  const statusConfig = {
    EMITIDO: {
      name: "Emitido",
      icon: "📋",
      color: "#e2e8f0",
      textColor: "#64748b",
      borderColor: "#cbd5e1",
      order: 1,
    },
    SAIDA_CONFIRMADA: {
      name: "Saída Confirmada",
      icon: "🚚",
      color: "#dbeafe",
      textColor: "#0284c7",
      borderColor: "#0284c7",
      order: 2,
    },
    EM_TRANSITO: {
      name: "Em Trânsito",
      icon: "🛣️",
      color: "#fef3c7",
      textColor: "#b45309",
      borderColor: "#f59e0b",
      order: 3,
    },
    ENTREGUE: {
      name: "Entregue",
      icon: "✅",
      color: "#dcfce7",
      textColor: "#16a34a",
      borderColor: "#22c55e",
      order: 4,
    },
    NAO_ENTREGUE: {
      name: "Não Entregue",
      icon: "⚠️",
      color: "#fed7aa",
      textColor: "#ea580c",
      borderColor: "#fb923c",
      order: 5,
    },
    CANCELADO: {
      name: "Cancelado",
      icon: "❌",
      color: "#fee2e2",
      textColor: "#dc2626",
      borderColor: "#fca5a5",
      order: 6,
    },
  };

  let progressColor = "#0284c7";
  let progressPercentage = 0;
  let statusLabel = "Em progresso";

  if (currentStatus === "CANCELADO") {
    progressColor = "#dc2626";
    progressPercentage = 100;
    statusLabel = "Cancelado";
  } else if (currentStatus === "NAO_ENTREGUE") {
    progressColor = "#ea580c";
    progressPercentage = 80;
    statusLabel = "Não entregue";
  } else if (currentStatus === "ENTREGUE") {
    progressColor = "#16a34a";
    progressPercentage = 100;
    statusLabel = "Entregue com sucesso";
  } else if (currentStatus === "EM_TRANSITO") {
    progressColor = "#b45309";
    progressPercentage = 75;
    statusLabel = "Em trânsito";
  } else if (currentStatus === "SAIDA_CONFIRMADA") {
    progressColor = "#0284c7";
    progressPercentage = 50;
    statusLabel = "Saída confirmada";
  } else if (currentStatus === "EMITIDO") {
    progressColor = "#6b7280";
    progressPercentage = 25;
    statusLabel = "Aguardando saída";
  }

  const config = statusConfig[currentStatus] || statusConfig["EMITIDO"];

  let html = `
    <div style="padding: 20px;">
      <!-- Informação do status atual -->
      <div style="
        margin-bottom: 20px;
        padding: 12px 16px;
        background-color: ${config.color};
        border: 2px solid ${config.borderColor};
        border-radius: 8px;
        text-align: center;
      ">
        <div style="
          font-size: 28px;
          margin-bottom: 6px;
        ">
          ${config.icon}
        </div>
        <div style="
          font-size: 14px;
          font-weight: 700;
          color: ${config.textColor};
          margin-bottom: 4px;
        ">
          ${config.name}
        </div>
        <div style="
          font-size: 12px;
          color: ${config.textColor};
          opacity: 0.8;
        ">
          ${statusLabel}
        </div>
      </div>

      <!-- Barra de progresso -->
      <div style="margin-bottom: 20px;">
        <div style="
          height: 8px;
          background-color: #e2e8f0;
          border-radius: 4px;
          overflow: hidden;
        ">
          <div style="
            height: 100%;
            background-color: ${progressColor};
            width: ${progressPercentage}%;
            transition: width 0.3s ease;
          "></div>
        </div>
        <div style="
          margin-top: 8px;
          font-size: 12px;
          color: #94a3b8;
          text-align: right;
        ">
          ${progressPercentage}% concluído
        </div>
      </div>

      <!-- Timeline dos status possíveis -->
      <div style="
        display: flex;
        gap: 8px;
        overflow-x: auto;
        padding: 8px 0;
      ">
  `;

  const orderedStatuses = [
    "EMITIDO",
    "SAIDA_CONFIRMADA",
    "EM_TRANSITO",
    "ENTREGUE",
    "NAO_ENTREGUE",
    "CANCELADO",
  ];

  orderedStatuses.forEach((st) => {
    const cfg = statusConfig[st];
    const isCurrentStatus = st === currentStatus;

    html += `
      <div style="
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 4px;
        flex-shrink: 0;
      ">
        <div style="
          width: 48px;
          height: 48px;
          border-radius: 50%;
          background-color: ${isCurrentStatus ? cfg.color : "#f1f5f9"};
          border: 2px solid ${isCurrentStatus ? cfg.borderColor : "#e2e8f0"};
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 20px;
          cursor: pointer;
          transition: all 0.2s ease;
          ${isCurrentStatus ? `box-shadow: 0 0 0 3px ${cfg.borderColor}33;` : ""}
        ">
          ${cfg.icon}
        </div>
        <div style="
          font-size: 10px;
          text-align: center;
          color: ${isCurrentStatus ? cfg.textColor : "#94a3b8"};
          font-weight: ${isCurrentStatus ? "700" : "500"};
          width: 50px;
          line-height: 1.2;
          word-break: break-word;
        ">
          ${cfg.name.split(" ")[0]}
        </div>
      </div>
    `;
  });

  html += `
      </div>
    </div>
  `;

  timelineEl.innerHTML = html;
});