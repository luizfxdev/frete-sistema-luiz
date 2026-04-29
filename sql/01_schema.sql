DROP TABLE IF EXISTS ocorrencia_frete CASCADE;
DROP TABLE IF EXISTS frete CASCADE;
DROP TABLE IF EXISTS manutencao_veiculo CASCADE;
DROP TABLE IF EXISTS tabela_frete_rota CASCADE;
DROP TABLE IF EXISTS veiculo CASCADE;
DROP TABLE IF EXISTS motorista CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

CREATE TABLE usuario (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(120) NOT NULL,
    email           VARCHAR(120) NOT NULL UNIQUE,
    senha           VARCHAR(255) NOT NULL,
    ativo           BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cliente (
    id                  BIGSERIAL PRIMARY KEY,
    razao_social        VARCHAR(150) NOT NULL,
    nome_fantasia       VARCHAR(150),
    cnpj                VARCHAR(14)  NOT NULL UNIQUE,
    inscricao_estadual  VARCHAR(20),
    tipo                VARCHAR(20)  NOT NULL,
    logradouro          VARCHAR(150) NOT NULL,
    numero              VARCHAR(20)  NOT NULL,
    complemento         VARCHAR(80),
    bairro              VARCHAR(80)  NOT NULL,
    municipio           VARCHAR(80)  NOT NULL,
    uf                  CHAR(2)      NOT NULL,
    cep                 VARCHAR(8)   NOT NULL,
    telefone            VARCHAR(20),
    email               VARCHAR(120),
    status              VARCHAR(20)  NOT NULL DEFAULT 'ATIVO',
    CONSTRAINT chk_cliente_tipo   CHECK (tipo IN ('REMETENTE','DESTINATARIO','AMBOS')),
    CONSTRAINT chk_cliente_status CHECK (status IN ('ATIVO','INATIVO'))
);

CREATE TABLE motorista (
    id                BIGSERIAL PRIMARY KEY,
    nome              VARCHAR(150) NOT NULL,
    cpf               VARCHAR(11)  NOT NULL UNIQUE,
    data_nascimento   DATE         NOT NULL,
    telefone          VARCHAR(20),
    cnh_numero        VARCHAR(20)  NOT NULL,
    cnh_categoria     VARCHAR(2)   NOT NULL,
    cnh_validade      DATE         NOT NULL,
    tipo_vinculo      VARCHAR(20)  NOT NULL,
    status            VARCHAR(20)  NOT NULL DEFAULT 'ATIVO',
    CONSTRAINT chk_motorista_categoria CHECK (cnh_categoria IN ('A','B','C','D','E')),
    CONSTRAINT chk_motorista_vinculo   CHECK (tipo_vinculo  IN ('FUNCIONARIO','AGREGADO','TERCEIRO')),
    CONSTRAINT chk_motorista_status    CHECK (status        IN ('ATIVO','INATIVO','SUSPENSO'))
);

CREATE TABLE veiculo (
    id              BIGSERIAL PRIMARY KEY,
    placa           VARCHAR(8)   NOT NULL UNIQUE,
    rntrc           VARCHAR(20),
    ano_fabricacao  INTEGER      NOT NULL,
    tipo            VARCHAR(20)  NOT NULL,
    tara_kg         NUMERIC(10,2) NOT NULL,
    capacidade_kg   NUMERIC(10,2) NOT NULL,
    volume_m3       NUMERIC(10,2),
    status          VARCHAR(20)  NOT NULL DEFAULT 'DISPONIVEL',
    CONSTRAINT chk_veiculo_tipo   CHECK (tipo   IN ('TRUCK','CARRETA','VAN','UTILITARIO')),
    CONSTRAINT chk_veiculo_status CHECK (status IN ('DISPONIVEL','EM_VIAGEM','EM_MANUTENCAO'))
);

CREATE TABLE frete (
    id                      BIGSERIAL PRIMARY KEY,
    numero                  VARCHAR(20)  NOT NULL UNIQUE,
    id_remetente            BIGINT       NOT NULL REFERENCES cliente(id),
    id_destinatario         BIGINT       NOT NULL REFERENCES cliente(id),
    id_motorista            BIGINT       NOT NULL REFERENCES motorista(id),
    id_veiculo              BIGINT       NOT NULL REFERENCES veiculo(id),
    municipio_origem        VARCHAR(80)  NOT NULL,
    uf_origem               CHAR(2)      NOT NULL,
    municipio_destino       VARCHAR(80)  NOT NULL,
    uf_destino              CHAR(2)      NOT NULL,
    descricao_carga         VARCHAR(255) NOT NULL,
    peso_kg                 NUMERIC(10,2) NOT NULL,
    volumes                 INTEGER      NOT NULL,
    valor_frete             NUMERIC(12,2) NOT NULL,
    aliquota_icms           NUMERIC(5,2)  NOT NULL,
    valor_icms              NUMERIC(12,2) NOT NULL,
    valor_total             NUMERIC(12,2) NOT NULL,
    status                  VARCHAR(20)  NOT NULL DEFAULT 'EMITIDO',
    data_emissao            DATE         NOT NULL,
    data_previsao_entrega   DATE         NOT NULL,
    data_saida              TIMESTAMP,
    data_entrega            TIMESTAMP,
    CONSTRAINT chk_frete_status CHECK (status IN ('EMITIDO','SAIDA_CONFIRMADA','EM_TRANSITO','ENTREGUE','NAO_ENTREGUE','CANCELADO'))
);

CREATE INDEX idx_frete_status     ON frete(status);
CREATE INDEX idx_frete_motorista  ON frete(id_motorista);
CREATE INDEX idx_frete_veiculo    ON frete(id_veiculo);
CREATE INDEX idx_frete_emissao    ON frete(data_emissao);

CREATE TABLE ocorrencia_frete (
    id                  BIGSERIAL PRIMARY KEY,
    id_frete            BIGINT       NOT NULL REFERENCES frete(id) ON DELETE CASCADE,
    tipo                VARCHAR(30)  NOT NULL,
    data_hora           TIMESTAMP    NOT NULL,
    municipio           VARCHAR(80)  NOT NULL,
    uf                  CHAR(2)      NOT NULL,
    descricao           VARCHAR(500),
    nome_recebedor      VARCHAR(150),
    documento_recebedor VARCHAR(30),
    CONSTRAINT chk_ocorrencia_tipo CHECK (tipo IN ('SAIDA_PATIO','EM_ROTA','TENTATIVA_ENTREGA','ENTREGA_REALIZADA','AVARIA','EXTRAVIO','OUTROS'))
);

CREATE INDEX idx_ocorrencia_frete    ON ocorrencia_frete(id_frete);
CREATE INDEX idx_ocorrencia_datahora ON ocorrencia_frete(data_hora);

CREATE TABLE manutencao_veiculo (
    id           BIGSERIAL PRIMARY KEY,
    id_veiculo   BIGINT       NOT NULL REFERENCES veiculo(id),
    tipo         VARCHAR(20)  NOT NULL,
    descricao    VARCHAR(500) NOT NULL,
    data_inicio  DATE         NOT NULL,
    data_fim     DATE,
    custo        NUMERIC(12,2),
    CONSTRAINT chk_manutencao_tipo CHECK (tipo IN ('PREVENTIVA','CORRETIVA'))
);

CREATE TABLE tabela_frete_rota (
    id                BIGSERIAL PRIMARY KEY,
    municipio_origem  VARCHAR(80) NOT NULL,
    uf_origem         CHAR(2)     NOT NULL,
    municipio_destino VARCHAR(80) NOT NULL,
    uf_destino        CHAR(2)     NOT NULL,
    valor_base        NUMERIC(12,2) NOT NULL,
    valor_por_kg      NUMERIC(12,4) NOT NULL DEFAULT 0,
    UNIQUE (municipio_origem, uf_origem, municipio_destino, uf_destino)
);