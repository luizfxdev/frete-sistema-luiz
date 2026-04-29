(function () {
  const apply = (el, fn) => { el.addEventListener('input', () => { el.value = fn(el.value); }); };

  const onlyDigits = v => v.replace(/\D/g, '');

  const maskCnpj = v => {
    v = onlyDigits(v).slice(0, 14);
    return v
      .replace(/^(\d{2})(\d)/, '$1.$2')
      .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
      .replace(/\.(\d{3})(\d)/, '.$1/$2')
      .replace(/(\d{4})(\d)/, '$1-$2');
  };

  const maskCpf = v => {
    v = onlyDigits(v).slice(0, 11);
    return v
      .replace(/(\d{3})(\d)/, '$1.$2')
      .replace(/(\d{3})(\d)/, '$1.$2')
      .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
  };

  const maskCep = v => {
    v = onlyDigits(v).slice(0, 8);
    return v.replace(/(\d{5})(\d)/, '$1-$2');
  };

  const maskTelefone = v => {
    v = onlyDigits(v).slice(0, 11);
    if (v.length <= 10) return v.replace(/(\d{2})(\d{4})(\d)/, '($1) $2-$3');
    return v.replace(/(\d{2})(\d{5})(\d)/, '($1) $2-$3');
  };

  const maskPlaca = v => v.replace(/[^A-Za-z0-9]/g, '').toUpperCase().slice(0, 7);

  document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-mask="cnpj"]').forEach(el => apply(el, maskCnpj));
    document.querySelectorAll('[data-mask="cpf"]').forEach(el => apply(el, maskCpf));
    document.querySelectorAll('[data-mask="cep"]').forEach(el => apply(el, maskCep));
    document.querySelectorAll('[data-mask="telefone"]').forEach(el => apply(el, maskTelefone));
    document.querySelectorAll('[data-mask="placa"]').forEach(el => apply(el, maskPlaca));
  });
})();