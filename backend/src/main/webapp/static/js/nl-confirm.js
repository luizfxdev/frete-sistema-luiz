document.addEventListener('DOMContentLoaded', function () {
  document.querySelectorAll('[data-confirm]').forEach(function (btn) {
    btn.addEventListener('click', function (e) {
      if (!window.confirm(btn.getAttribute('data-confirm'))) {
        e.preventDefault();
      }
    });
  });
});