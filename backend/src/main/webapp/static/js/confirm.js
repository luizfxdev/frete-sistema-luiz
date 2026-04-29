document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('form[data-confirm]').forEach(form => {
    form.addEventListener('submit', e => {
      const msg = form.getAttribute('data-confirm') || 'Confirma esta operação?';
      if (!confirm(msg)) e.preventDefault();
    });
  });
});