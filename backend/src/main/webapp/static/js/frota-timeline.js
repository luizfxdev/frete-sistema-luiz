document.addEventListener('DOMContentLoaded', function() {
  const timelineEl = document.getElementById('status-timeline');
  
  if (!timelineEl) return;
  
  const status = timelineEl.getAttribute('data-status') || 'EMITIDO';
  
  const stages = [
    { 
      name: 'Emitido', 
      completed: ['EMITIDO', 'SAIDA_CONFIRMADA', 'EM_TRANSITO', 'ENTREGUE', 'NAO_ENTREGUE'].includes(status),
      color: ['EMITIDO', 'SAIDA_CONFIRMADA', 'EM_TRANSITO', 'ENTREGUE', 'NAO_ENTREGUE'].includes(status) ? '#16a34a' : '#cbd5e1'
    },
    { 
      name: 'Saída Confirmada', 
      completed: ['SAIDA_CONFIRMADA', 'EM_TRANSITO', 'ENTREGUE', 'NAO_ENTREGUE'].includes(status),
      color: ['SAIDA_CONFIRMADA', 'EM_TRANSITO', 'ENTREGUE', 'NAO_ENTREGUE'].includes(status) ? '#2563eb' : '#cbd5e1'
    },
    { 
      name: 'Em Trânsito', 
      completed: ['EM_TRANSITO', 'ENTREGUE', 'NAO_ENTREGUE'].includes(status),
      color: ['EM_TRANSITO', 'ENTREGUE', 'NAO_ENTREGUE'].includes(status) ? '#f59e0b' : '#cbd5e1'
    },
    { 
      name: 'Entregue', 
      completed: status === 'ENTREGUE',
      color: status === 'ENTREGUE' ? '#16a34a' : (status === 'NAO_ENTREGUE' ? '#ef4444' : '#cbd5e1')
    }
  ];

  const options = {
    chart: {
      type: 'bar',
      height: 280,
      toolbar: { show: false },
      sparkline: { enabled: false }
    },
    plotOptions: {
      bar: {
        horizontal: true,
        barHeight: '50%',
        dataLabels: {
          position: 'right'
        }
      }
    },
    xaxis: {
      categories: stages.map(s => s.name),
      labels: {
        style: {
          fontSize: '0.85rem',
          fontWeight: 500,
          colors: '#64748b'
        }
      }
    },
    series: [{
      name: 'Status',
      data: stages.map(s => s.completed ? 1 : 0)
    }],
    colors: stages.map(s => s.color),
    states: {
      hover: {
        filter: {
          type: 'none'
        }
      },
      active: {
        filter: {
          type: 'none'
        }
      }
    },
    legend: { show: false },
    grid: {
      padding: {
        left: 0,
        right: 20
      }
    },
    dataLabels: {
      enabled: true,
      textAnchor: 'middle',
      offsetX: 0,
      style: {
        fontSize: '0.75rem',
        fontWeight: 600,
        colors: ['#fff']
      },
      formatter: function(val, opts) {
        return stages[opts.dataPointIndex].completed ? '✓' : '';
      }
    }
  };

  new ApexCharts(timelineEl, options).render();
});