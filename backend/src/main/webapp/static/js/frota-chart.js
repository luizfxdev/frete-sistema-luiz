document.addEventListener('DOMContentLoaded', function () {
  var el = document.getElementById('frota-chart');
  if (!el) return;

  var disp   = parseInt(el.getAttribute('data-disp'),   10) || 0;
  var viagem = parseInt(el.getAttribute('data-viagem'), 10) || 0;
  var manut  = parseInt(el.getAttribute('data-manut'),  10) || 0;

  new ApexCharts(el, {
    series: [disp, viagem, manut],
    labels: ['Disponível', 'Em Viagem', 'Manutenção'],
    colors: ['#22c55e', '#005eff', '#f59e0b'],
    chart: {
      type: 'donut',
      width: 180,
      height: 160,
      toolbar: { show: false },
      background: 'transparent'
    },
    plotOptions: {
      pie: {
        donut: {
          size: '68%',
          labels: {
            show: true,
            total: {
              show: true,
              label: 'Total',
              fontSize: '11px',
              fontFamily: 'Avenir Next, DM Sans, sans-serif',
              fontWeight: 700,
              color: '#0a2540',
              formatter: function (w) {
                return w.globals.seriesTotals.reduce(function (a, b) { return a + b; }, 0);
              }
            }
          }
        }
      }
    },
    dataLabels: { enabled: false },
    legend: { show: false },
    stroke: { width: 2, colors: ['#fff'] },
    tooltip: { style: { fontFamily: 'Avenir Next, DM Sans, sans-serif', fontSize: '12px' } }
  }).render();
});