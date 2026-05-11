document.addEventListener('DOMContentLoaded', function () {
  var el = document.getElementById('report-bar-chart');
  if (!el) return;

  var resumo     = (el.getAttribute('data-resumo') || '').split('|');
  var meses      = JSON.parse(resumo[0] || '[]');
  var concluidos = JSON.parse(resumo[1] || '[]');
  var cancelados = JSON.parse(resumo[2] || '[]');

  new ApexCharts(el, {
    series: [
      { name: 'Concluídos', data: concluidos },
      { name: 'Cancelados', data: cancelados }
    ],
    chart: {
      type: 'bar',
      height: 220,
      toolbar: { show: false },
      background: 'transparent',
      fontFamily: 'Avenir Next, DM Sans, sans-serif'
    },
    plotOptions: { bar: { borderRadius: 6, columnWidth: '48%', borderRadiusApplication: 'end' } },
    colors: ['#005eff', '#f59e0b'],
    dataLabels: { enabled: false },
    grid: {
      borderColor: '#f1f5f9',
      strokeDashArray: 4,
      yaxis: { lines: { show: true } },
      xaxis: { lines: { show: false } }
    },
    xaxis: {
      categories: meses,
      axisBorder: { show: false },
      axisTicks: { show: false },
      labels: { style: { colors: '#94a3b8', fontSize: '12px', fontFamily: 'Avenir Next, DM Sans, sans-serif' } }
    },
    yaxis: {
      labels: { style: { colors: '#94a3b8', fontSize: '12px', fontFamily: 'Avenir Next, DM Sans, sans-serif' } }
    },
    legend: {
      position: 'top',
      horizontalAlign: 'right',
      fontSize: '12px',
      fontFamily: 'Avenir Next, DM Sans, sans-serif',
      markers: { radius: 4 }
    },
    tooltip: { style: { fontFamily: 'Avenir Next, DM Sans, sans-serif', fontSize: '13px' } }
  }).render();
});