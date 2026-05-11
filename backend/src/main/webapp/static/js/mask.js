(function () {
    const onlyDigits = v => (v || '').replace(/\D/g, '');

    const apply = (el, fn) => {
        el.addEventListener('input', () => { el.value = fn(el.value); });
    };

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

    const maskDocumento = v => {
        const d = onlyDigits(v);
        return d.length <= 11 ? maskCpf(d) : maskCnpj(d);
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

    const maskData = v => {
        v = onlyDigits(v).slice(0, 8);
        return v
            .replace(/(\d{2})(\d)/, '$1/$2')
            .replace(/(\d{2})\/(\d{2})(\d)/, '$1/$2/$3');
    };

    const isDataFutura = v => {
        const m = /^(\d{2})\/(\d{2})\/(\d{4})$/.exec(v);
        if (!m) return false;
        const d = new Date(parseInt(m[3], 10), parseInt(m[2], 10) - 1, parseInt(m[1], 10));
        if (isNaN(d.getTime())) return false;
        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0);
        return d > hoje;
    };

    const hojeFormatado = () => {
        const d = new Date();
        const dd = String(d.getDate()).padStart(2, '0');
        const mm = String(d.getMonth() + 1).padStart(2, '0');
        const yyyy = d.getFullYear();
        return dd + '/' + mm + '/' + yyyy;
    };

    const aplicarMaiusculoUf = el => {
        el.setAttribute('maxlength', '2');
        el.addEventListener('input', () => {
            el.value = el.value.replace(/[^A-Za-z]/g, '').toUpperCase().slice(0, 2);
        });
    };

    const aplicarMascaraInicial = (el, fn) => {
        const v = onlyDigits(el.value);
        if (v.length > 0 && v === el.value.replace(/\D/g, '') && el.value !== fn(el.value)) {
            el.value = fn(el.value);
        }
    };

    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('[data-mask="cnpj"]').forEach(el => {
            apply(el, maskCnpj);
            aplicarMascaraInicial(el, maskCnpj);
        });

        document.querySelectorAll('[data-mask="cpf"]').forEach(el => {
            apply(el, maskCpf);
            aplicarMascaraInicial(el, maskCpf);
        });

        document.querySelectorAll('[data-mask="documento"]').forEach(el => {
            apply(el, maskDocumento);
            aplicarMascaraInicial(el, maskDocumento);
        });

        document.querySelectorAll('[data-mask="cep"]').forEach(el => {
            apply(el, maskCep);
            aplicarMascaraInicial(el, maskCep);
        });

        document.querySelectorAll('[data-mask="telefone"]').forEach(el => {
            apply(el, maskTelefone);
            aplicarMascaraInicial(el, maskTelefone);
        });

        document.querySelectorAll('[data-mask="placa"]').forEach(el => {
            apply(el, maskPlaca);
        });

        document.querySelectorAll('[data-mask="data-nascimento"]').forEach(el => {
            apply(el, maskData);
            el.addEventListener('blur', () => {
                if (isDataFutura(el.value)) {
                    alert('Data de nascimento não pode ser futura.');
                    el.value = '';
                    el.focus();
                }
            });
        });

        document.querySelectorAll('[data-mask="data-emissao"]').forEach(el => {
            apply(el, maskData);
            el.addEventListener('blur', () => {
                if (!el.value || el.value.trim() === '') {
                    el.value = hojeFormatado();
                }
            });
        });

        document.querySelectorAll('input[name="uf"], input[name="ufOrigem"], input[name="ufDestino"]')
            .forEach(aplicarMaiusculoUf);
    });
})();