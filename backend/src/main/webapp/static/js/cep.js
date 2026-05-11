(function () {
    const onlyDigits = v => (v || '').replace(/\D/g, '');

    const findInForm = (form, name) => form.querySelector('[name="' + name + '"]');

    const setField = (input, value) => {
        if (!input) return;
        input.value = value;
        input.dispatchEvent(new Event('input', { bubbles: true }));
    };

    const limparCampos = (form) => {
        setField(findInForm(form, 'logradouro'), '');
        setField(findInForm(form, 'bairro'), '');
        setField(findInForm(form, 'municipio'), '');
        setField(findInForm(form, 'uf'), '');
    };

    const consultarCep = async (cep, form) => {
        try {
            const resp = await fetch('https://viacep.com.br/ws/' + cep + '/json/');
            if (!resp.ok) throw new Error('HTTP ' + resp.status);
            const data = await resp.json();
            if (data.erro === true) {
                alert('CEP não encontrado.');
                limparCampos(form);
                return;
            }
            setField(findInForm(form, 'logradouro'), data.logradouro || '');
            setField(findInForm(form, 'bairro'),     data.bairro    || '');
            setField(findInForm(form, 'municipio'),  data.localidade || '');
            setField(findInForm(form, 'uf'),         data.uf        || '');
        } catch (e) {
            alert('Não foi possível consultar o CEP. Verifique sua conexão.');
        }
    };

    document.addEventListener('blur', (ev) => {
        const el = ev.target;
        if (!el || el.name !== 'cep') return;
        const form = el.closest('form');
        if (!form) return;
        const cep = onlyDigits(el.value);
        if (cep.length !== 8) {
            limparCampos(form);
            return;
        }
        consultarCep(cep, form);
    }, true);
})();