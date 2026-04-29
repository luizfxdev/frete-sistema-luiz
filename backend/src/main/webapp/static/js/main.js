document.addEventListener('DOMContentLoaded', () => {
  const dataAtual = document.querySelectorAll('[data-default-today]');
  const hoje = new Date().toISOString().split('T')[0];
  dataAtual.forEach(el => { if (!el.value) el.value = hoje; });
});