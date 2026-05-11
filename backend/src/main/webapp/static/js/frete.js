(function () {
    const ctx = document.querySelector('meta[name="ctx"]')?.content ?? '';

    function field(name) {
        return document.querySelector(`[name="${name}"]`);
    }

    function val(name) {
        return field(name)?.value?.trim() ?? '';
    }

    function set(name, value) {
        const el = field(name);
        if (el) el.value = value;
    }

    function hint(show) {
        const anchor = field('valorFrete');
        if (!anchor) return;
        let el = document.getElementById('valorFrete-hint');
        if (!el) {
            el = document.createElement('span');
            el.id = 'valorFrete-hint';
            el.style.cssText = 'font-size:.8rem;color:#6b7280;display:block;margin-top:2px';
            anchor.insertAdjacentElement('afterend', el);
        }
        el.textContent = show ? 'Valor sugerido com base na tabela de rotas. Você pode alterar.' : '';
    }

    async function fetchCliente(id) {
        const res = await fetch(`${ctx}/fretes/api/cliente/${id}`);
        if (!res.ok) return null;
        return res.json();
    }

    async function fetchRota(municipioOrigem, ufOrigem, municipioDestino, ufDestino) {
        const params = new URLSearchParams({ municipioOrigem, ufOrigem, municipioDestino, ufDestino });
        const res = await fetch(`${ctx}/fretes/api/rota?${params}`);
        if (!res.ok) return null;
        return res.json();
    }

    async function consultarRota() {
        const municipioOrigem  = val('municipioOrigem');
        const ufOrigem         = val('ufOrigem');
        const municipioDestino = val('municipioDestino');
        const ufDestino        = val('ufDestino');

        if (!municipioOrigem || !ufOrigem || !municipioDestino || !ufDestino) return;

        const rota = await fetchRota(municipioOrigem, ufOrigem, municipioDestino, ufDestino);
        if (!rota || rota.valorBase == null) { hint(false); return; }

        const pesoKg   = parseFloat(val('pesoKg')) || 0;
        const sugerido = parseFloat(rota.valorBase) + parseFloat(rota.valorPorKg) * pesoKg;
        set('valorFrete', sugerido.toFixed(2));
        hint(true);
    }

    async function onRemetenteChange(e) {
        const id = e.target.value;
        if (!id) return;
        try {
            const cliente = await fetchCliente(id);
            if (!cliente) return;
            set('municipioOrigem', cliente.municipio);
            set('ufOrigem', cliente.uf);
            await consultarRota();
        } catch (_) {}
    }

    async function onDestinatarioChange(e) {
        const id = e.target.value;
        if (!id) return;
        try {
            const cliente = await fetchCliente(id);
            if (!cliente) return;
            set('municipioDestino', cliente.municipio);
            set('ufDestino', cliente.uf);
            await consultarRota();
        } catch (_) {}
    }

    field('idRemetente')?.addEventListener('change', onRemetenteChange);
    field('idDestinatario')?.addEventListener('change', onDestinatarioChange);
})();