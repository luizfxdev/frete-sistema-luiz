INSERT INTO cliente (razao_social, nome_fantasia, tipo_documento, documento, inscricao_estadual, tipo, logradouro, numero, complemento, bairro, municipio, uf, cep, telefone, email, status) VALUES
('Indústria Alfa LTDA',     'Alfa', 'CNPJ', '11222333000181', '123456789', 'REMETENTE',    'Av. Industrial', '1000', 'Galpão 3', 'Distrito Industrial', 'Recife',     'PE', '52010000', '8130001000', 'contato@alfa.com.br', 'ATIVO'),
('Comércio Beta SA',        'Beta', 'CNPJ', '22333444000172', '987654321', 'DESTINATARIO', 'Rua das Flores', '250',  NULL,       'Centro',              'João Pessoa','PB', '58010000', '8330002000', 'contato@beta.com.br', 'ATIVO'),
('Distribuidora Gama LTDA', 'Gama', 'CNPJ', '33444555000163', '456789123', 'AMBOS',        'Rod. BR-101',    'KM 12',NULL,       'Rural',               'Maceió',     'AL', '57000000', '8230003000', 'contato@gama.com.br', 'ATIVO');

INSERT INTO motorista (nome, cpf, data_nascimento, telefone, logradouro, numero, complemento, bairro, municipio, uf, cep, cnh_numero, cnh_categoria, cnh_validade, tipo_vinculo, status) VALUES
('João da Silva',  '52998224725', '1985-04-12', '81988880001', 'Rua A', '100', NULL, 'Boa Viagem', 'Recife',      'PE', '51020000', '12345678901', 'E', '2027-12-31', 'FUNCIONARIO', 'ATIVO'),
('Carlos Pereira', '11144477735', '1990-08-23', '83988880002', 'Rua B', '200', NULL, 'Centro',     'João Pessoa', 'PB', '58010000', '22345678902', 'D', '2026-06-30', 'AGREGADO',    'ATIVO'),
('Roberto Souza',  '93541134780', '1978-01-05', '82988880003', 'Rua C', '300', NULL, 'Pajuçara',   'Maceió',      'AL', '57030000', '32345678903', 'E', '2028-03-15', 'TERCEIRO',    'ATIVO');

INSERT INTO veiculo (placa, rntrc, ano_fabricacao, tipo, tara_kg, capacidade_kg, volume_m3, status) VALUES
('ABC1D23', '12345678', 2020, 'TRUCK',    8000.00, 12000.00, 45.00, 'DISPONIVEL'),
('XYZ9K88', '23456789', 2019, 'CARRETA', 14000.00, 28000.00, 90.00, 'DISPONIVEL'),
('JKL4M56', '34567890', 2022, 'VAN',      2500.00,  3500.00, 15.00, 'DISPONIVEL');

INSERT INTO frete (numero, id_remetente, id_destinatario, id_motorista, id_veiculo, municipio_origem, uf_origem, municipio_destino, uf_destino, descricao_carga, peso_kg, volumes, valor_frete, aliquota_icms, valor_icms, valor_total, status, data_emissao, data_previsao_entrega) VALUES
('FRT-2026-00001', 1, 2, 1, 1, 'Recife', 'PE', 'João Pessoa', 'PB', 'Material de construção',         8000.00, 50, 2500.00, 12.00, 300.00, 2800.00, 'EMITIDO',          '2026-04-20', '2026-04-30'),
('FRT-2026-00002', 1, 3, 2, 2, 'Recife', 'PE', 'Maceió',      'AL', 'Produtos químicos industriais', 20000.00, 80, 5500.00, 12.00, 660.00, 6160.00, 'SAIDA_CONFIRMADA', '2026-04-22', '2026-05-02'),
('FRT-2026-00003', 3, 2, 3, 3, 'Maceió', 'AL', 'João Pessoa', 'PB', 'Eletroeletrônicos',              2000.00, 30, 1800.00, 12.00, 216.00, 2016.00, 'EM_TRANSITO',      '2026-04-25', '2026-04-29'),
('FRT-2026-00004', 1, 2, 1, 1, 'Recife', 'PE', 'João Pessoa', 'PB', 'Cimento',                        7500.00, 40, 2200.00, 12.00, 264.00, 2464.00, 'ENTREGUE',         '2026-04-10', '2026-04-15'),
('FRT-2026-00005', 1, 3, 2, 2, 'Recife', 'PE', 'Maceió',      'AL', 'Bebidas',                       15000.00, 60, 3800.00, 12.00, 456.00, 4256.00, 'CANCELADO',        '2026-04-15', '2026-04-25');

UPDATE frete SET data_saida = '2026-04-22 08:00:00' WHERE numero = 'FRT-2026-00002';
UPDATE frete SET data_saida = '2026-04-25 07:30:00' WHERE numero = 'FRT-2026-00003';
UPDATE frete SET data_saida = '2026-04-10 06:00:00', data_entrega = '2026-04-14 17:00:00' WHERE numero = 'FRT-2026-00004';

UPDATE veiculo SET status = 'EM_VIAGEM' WHERE id IN (2, 3);

INSERT INTO ocorrencia_frete (id_frete, tipo, data_hora, municipio, uf, descricao, nome_recebedor, documento_recebedor) VALUES
(2, 'SAIDA_PATIO',       '2026-04-22 08:00:00', 'Recife',         'PE', 'Saída do pátio confirmada',           NULL,          NULL),
(3, 'SAIDA_PATIO',       '2026-04-25 07:30:00', 'Maceió',         'AL', 'Saída do pátio',                      NULL,          NULL),
(3, 'EM_ROTA',           '2026-04-25 11:00:00', 'Palmares',       'PE', 'Veículo em rota - posição reportada', NULL,          NULL),
(4, 'SAIDA_PATIO',       '2026-04-10 06:00:00', 'Recife',         'PE', 'Saída do pátio',                      NULL,          NULL),
(4, 'EM_ROTA',           '2026-04-12 10:00:00', 'Campina Grande', 'PB', 'Em rota',                             NULL,          NULL),
(4, 'ENTREGA_REALIZADA', '2026-04-14 17:00:00', 'João Pessoa',    'PB', 'Entrega concluída',                   'Maria Souza', '12345678900');

INSERT INTO tabela_frete_rota (municipio_origem, uf_origem, municipio_destino, uf_destino, valor_base, valor_por_kg) VALUES
('Recife', 'PE', 'João Pessoa', 'PB', 1500.00, 0.15),
('Recife', 'PE', 'Maceió',      'AL', 1200.00, 0.12),
('Maceió', 'AL', 'João Pessoa', 'PB', 1300.00, 0.14);

INSERT INTO manutencao_veiculo (id_veiculo, tipo, descricao, data_inicio, data_fim, custo) VALUES
(1, 'PREVENTIVA', 'Troca de óleo e filtros',     '2026-03-15', '2026-03-15',  850.00),
(2, 'CORRETIVA',  'Reparo no sistema de freios', '2026-02-10', '2026-02-12', 3200.00);