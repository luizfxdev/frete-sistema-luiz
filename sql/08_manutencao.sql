
UPDATE manutencao_veiculo 
SET km_atual = COALESCE(km_atual, 0) 
WHERE km_atual IS NULL OR km_atual = 0;
 

DELETE FROM orcamento_manutencao_item WHERE id_orcamento IN (
    SELECT id FROM orcamento_manutencao WHERE id_manutencao IN (
        SELECT id FROM manutencao_veiculo
    )
);
 
DELETE FROM orcamento_manutencao WHERE id_manutencao IN (
    SELECT id FROM manutencao_veiculo
);
 

INSERT INTO orcamento_manutencao (id_manutencao, numero, observacao, valor_total)
SELECT 
    m.id,
    'ORC-' || LPAD(m.id::text, 3, '0'),
    CASE 
        WHEN m.tipo = 'PREVENTIVA' THEN 'Manutenção preventiva - ' || m.descricao
        WHEN m.tipo = 'CORRETIVA' THEN 'Manutenção corretiva - ' || m.descricao
        ELSE 'Manutenção - ' || m.descricao
    END,
    COALESCE(m.custo, 0)
FROM manutencao_veiculo m
WHERE NOT EXISTS (
    SELECT 1 FROM orcamento_manutencao WHERE id_manutencao = m.id
);
 

INSERT INTO orcamento_manutencao_item (id_orcamento, descricao, quantidade, preco_unitario)
SELECT 
    o.id,
    CASE 
        WHEN mv.tipo = 'PREVENTIVA' THEN 'Serviço de manutenção preventiva'
        ELSE 'Serviço de reparo'
    END,
    1,
    o.valor_total
FROM orcamento_manutencao o
JOIN manutencao_veiculo mv ON o.id_manutencao = mv.id
WHERE NOT EXISTS (
    SELECT 1 FROM orcamento_manutencao_item WHERE id_orcamento = o.id
);
 

SELECT 
    m.id,
    m.placa,
    m.tipo,
    m.status_manutencao,
    m.km_atual,
    m.custo,
    COUNT(omi.id) as qtd_itens_orcamento
FROM manutencao_veiculo m
LEFT JOIN orcamento_manutencao om ON m.id = om.id_manutencao
LEFT JOIN orcamento_manutencao_item omi ON om.id = omi.id_orcamento
GROUP BY m.id, m.placa, m.tipo, m.status_manutencao, m.km_atual, m.custo
ORDER BY m.id;
