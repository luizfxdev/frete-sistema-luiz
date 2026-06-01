INSERT INTO orcamento_manutencao (id_manutencao, numero, observacao, valor_total)
VALUES (1, 'ORC-001', 'Orçamento preventiva regular', 850.00);

INSERT INTO orcamento_manutencao_item (id_orcamento, descricao, quantidade, preco_unitario)
VALUES 
  (1, 'Óleo sintetizado 10W30 5L', 1, 280.00),
  (1, 'Filtro de óleo', 1, 45.00),
  (1, 'Filtro de ar', 1, 120.00),
  (1, 'Mão de obra instalação', 4, 101.25);


INSERT INTO orcamento_manutencao (id_manutencao, numero, observacao, valor_total)
VALUES (2, 'ORC-002', 'Orçamento reparo sistema de freios', 3200.00);

INSERT INTO orcamento_manutencao_item (id_orcamento, descricao, quantidade, preco_unitario)
VALUES 
  (2, 'Pastilhas de freio (jogo)', 4, 350.00),
  (2, 'Disco de freio traseiro', 2, 450.00),
  (2, 'Líquido de freio', 2, 75.00),
  (2, 'Mão de obra diagnóstico e reparo', 12, 45.83);


UPDATE veiculo 
SET marca = 'Scania', modelo = 'P410', cor = 'Branco' 
WHERE id = 1 AND marca = '';

UPDATE veiculo 
SET marca = 'Volvo', modelo = 'FH 540', cor = 'Vermelho' 
WHERE id = 2 AND marca = '';

UPDATE veiculo 
SET marca = 'Mercedes', modelo = 'Sprinter', cor = 'Branco' 
WHERE id = 3 AND marca = '';


UPDATE manutencao_veiculo 
SET km_atual = 45250 
WHERE id = 1 AND km_atual = 0;

UPDATE manutencao_veiculo 
SET km_atual = 78900 
WHERE id = 2 AND km_atual = 0;