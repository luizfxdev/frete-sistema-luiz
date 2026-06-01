document.addEventListener('DOMContentLoaded', function () {
  var el = document.getElementById('report-bar-chart');
  if (!el) return;

  var resumo     = (el.getAttribute('data-resumo') || '').split('|');
  var meses      = JSON.parse(resumo[0] || '[]');
  var concluidos = JSON.parse(resumo[1] || '[]');
  var cancelados = JSON.parse(resumo[2] || '[]');

  if (meses.length === 0) {
    meses = ['Nov', 'Dez', 'Jan', 'Fev', 'Mar', 'Abr', 'Mai'];
    concluidos = [0, 0, 0, 0, 0, 0, 0];
    cancelados = [0, 0, 0, 0, 0, 0, 0];
  }

  var totalConcluidos = concluidos.reduce((a, b) => a + b, 0);
  var totalCancelados = cancelados.reduce((a, b) => a + b, 0);
  var grandTotal = totalConcluidos + totalCancelados;

  new ApexCharts(el, {
    series: [
      { name: 'Concluídos', data: concluidos },
      { name: 'Cancelados', data: cancelados }
    ],
    chart: {
      type: 'bar',
      height: 350,
      stacked: false,
      toolbar: { show: false },
      background: 'transparent',
      fontFamily: 'Avenir Next, DM Sans, sans-serif'
    },
    plotOptions: {
      bar: {
        borderRadius: 6,
        columnWidth: '55%',
        borderRadiusApplication: 'end'
      }
    },
    colors: ['#10b981', '#ef4444'],
    dataLabels: {
      enabled: true,
      background: { enabled: false },
      style: {
        fontSize: '12px',
        fontWeight: 600,
        colors: ['#10b981', '#ef4444']
      },
      formatter: function (val) {
        return val > 0 ? val : '';
      }
    },
    stroke: {
      width: 1,
      colors: ['#ffffff']
    },
    grid: {
      borderColor: '#e2e8f0',
      strokeDashArray: 3,
      yaxis: { lines: { show: true } },
      xaxis: { lines: { show: false } }
    },
    xaxis: {
      categories: meses,
      axisBorder: { show: false },
      axisTicks: { show: false },
      labels: {
        style: {
          colors: '#64748b',
          fontSize: '12px',
          fontWeight: 500,
          fontFamily: 'Avenir Next, DM Sans, sans-serif'
        }
      }
    },
    yaxis: {
      title: {
        text: 'Quantidade de Fretes',
        style: {
          color: '#64748b',
          fontSize: '12px',
          fontWeight: 600
        }
      },
      labels: {
        style: {
          colors: '#64748b',
          fontSize: '12px',
          fontFamily: 'Avenir Next, DM Sans, sans-serif'
        }
      }
    },
    legend: {
      position: 'top',
      horizontalAlign: 'right',
      fontSize: '13px',
      fontFamily: 'Avenir Next, DM Sans, sans-serif',
      markers: { radius: 4, width: 12, height: 12 }
    },
    tooltip: {
      style: { fontFamily: 'Avenir Next, DM Sans, sans-serif', fontSize: '13px' },
      theme: 'light',
      y: {
        formatter: function (val) {
          return val + ' fretes';
        }
      }
    },
    responsive: [
      {
        breakpoint: 1024,
        options: { chart: { height: 320 } }
      },
      {
        breakpoint: 768,
        options: { chart: { height: 280 } }
      }
    ]
  }).render();

  addSummaryStats(el, totalConcluidos, totalCancelados, grandTotal);
});

function addSummaryStats(chartEl, concluidos, cancelados, total) {
  var taxaConclussao = total > 0 ? ((concluidos / total) * 100).toFixed(1) : 0;

  var summaryHtml = `
    <div style="
      display: grid; 
      grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); 
      gap: 1rem; 
      margin-top: 1.5rem;
      padding-top: 1rem;
      border-top: 1px solid #e2e8f0;
    ">
      <div style="text-align: center; padding: 0.75rem;">
        <div style="font-size: 0.85rem; color: #64748b; margin-bottom: 0.5rem;">Total de Fretes</div>
        <div style="font-size: 1.5rem; font-weight: 700; color: #0f172a;">${total}</div>
      </div>
      <div style="text-align: center; padding: 0.75rem;">
        <div style="font-size: 0.85rem; color: #10b981; margin-bottom: 0.5rem;">Concluídos</div>
        <div style="font-size: 1.5rem; font-weight: 700; color: #10b981;">${concluidos}</div>
      </div>
      <div style="text-align: center; padding: 0.75rem;">
        <div style="font-size: 0.85rem; color: #ef4444; margin-bottom: 0.5rem;">Cancelados</div>
        <div style="font-size: 1.5rem; font-weight: 700; color: #ef4444;">${cancelados}</div>
      </div>
      <div style="text-align: center; padding: 0.75rem;">
        <div style="font-size: 0.85rem; color: #3b82f6; margin-bottom: 0.5rem;">Taxa de Conclusão</div>
        <div style="font-size: 1.5rem; font-weight: 700; color: #3b82f6;">${taxaConclussao}%</div>
      </div>
    </div>
  `;

  chartEl.insertAdjacentHTML('afterend', summaryHtml);
}