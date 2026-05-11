INSERT INTO cliente (razao_social, nome_fantasia, tipo_documento, documento, inscricao_estadual, tipo, logradouro, numero, complemento, bairro, municipio, uf, cep, telefone, email, status) VALUES
('Cliente Inativo Teste',  'Inativo Teste', 'CNPJ', '44555666000154', '111222333', 'AMBOS',        'Rua Teste',     '1', NULL, 'Centro',     'Recife',      'PE', '52010001', '8130000001', 'inativo@teste.com.br',  'INATIVO'),
('Cliente Sem Frete',      'SemFrete',      'CNPJ', '55666777000145', '222333444', 'REMETENTE',    'Rua Sem Frete', '2', NULL, 'Centro',     'João Pessoa', 'PB', '58010001', '8330000002', 'semfrete@teste.com.br', 'ATIVO'),
('Cliente Pessoa Física',  'PF Teste',      'CPF',  '52998224725',   NULL,        'DESTINATARIO', 'Rua PF',        '3', NULL, 'Boa Viagem', 'Recife',      'PE', '51020001', '8130000003', 'pf@teste.com.br',       'ATIVO');

INSERT INTO motorista (nome, cpf, data_nascimento, telefone, logradouro, numero, complemento, bairro, municipio, uf, cep, cnh_numero, cnh_categoria, cnh_validade, tipo_vinculo, status) VALUES
('Motorista CNH Vencida', '12345678909', '1980-01-01', '81988880004', 'Rua D', '400', NULL, 'Casa Forte', 'Recife', 'PE', '52060000', '99999999901', 'D', '2024-01-01', 'FUNCIONARIO', 'ATIVO'),
('Motorista Inativo',     '98765432100', '1982-05-10', '81988880005', 'Rua E', '500', NULL, 'Espinheiro', 'Recife', 'PE', '52020000', '99999999902', 'C', '2027-12-31', 'AGREGADO',    'INATIVO'),
('Motorista Suspenso',    '11122233396', '1979-09-15', '81988880006', 'Rua F', '600', NULL, 'Graças',     'Recife', 'PE', '52011000', '99999999903', 'E', '2028-06-30', 'TERCEIRO',    'SUSPENSO');

INSERT INTO veiculo (placa, rntrc, ano_fabricacao, tipo, tara_kg, capacidade_kg, volume_m3, status) VALUES
('TST1A11', '99999991', 2018, 'TRUCK',    7500.00, 10000.00, 40.00, 'EM_MANUTENCAO'),
('TST2B22', '99999992', 2017, 'VAN',      2200.00,  3000.00, 12.00, 'DISPONIVEL'),
('TST3C33', '99999993', 2015, 'CARRETA', 13000.00, 25000.00, 85.00, 'EM_VIAGEM');

INSERT INTO frete (numero, id_remetente, id_destinatario, id_motorista, id_veiculo, municipio_origem, uf_origem, municipio_destino, uf_destino, descricao_carga, peso_kg, volumes, valor_frete, aliquota_icms, valor_icms, valor_total, status, data_emissao, data_previsao_entrega) VALUES
('FRT-2026-09001', 1, 2, 1, 1, 'Recife', 'PE', 'João Pessoa', 'PB', 'Carga de teste para fluxo completo', 5000.00, 25, 1800.00, 12.00, 216.00, 2016.00, 'EMITIDO', '2026-04-26', '2026-05-03'),
('FRT-2026-09002', 1, 3, 1, 1, 'Recife', 'PE', 'Maceió',      'AL', 'Carga teste atraso',                 4000.00, 20, 1500.00, 12.00, 180.00, 1680.00, 'EMITIDO', '2026-04-15', '2026-04-22');