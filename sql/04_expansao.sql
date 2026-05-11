CREATE TABLE motorista_documento (
    id              BIGSERIAL    PRIMARY KEY,
    id_motorista    BIGINT       NOT NULL REFERENCES motorista(id) ON DELETE CASCADE,
    tipo            VARCHAR(10)  NOT NULL,
    caminho_arquivo VARCHAR(500) NOT NULL,
    criado_em       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_motorista_documento_tipo CHECK (tipo IN ('FOTO', 'CNH'))
);

CREATE INDEX idx_motorista_documento_motorista ON motorista_documento(id_motorista);

CREATE TABLE orcamento_manutencao (
    id            BIGSERIAL     PRIMARY KEY,
    id_manutencao BIGINT        NOT NULL REFERENCES manutencao_veiculo(id) ON DELETE CASCADE,
    numero        VARCHAR(20)   NOT NULL UNIQUE,
    observacao    TEXT,
    valor_total   NUMERIC(12,2) NOT NULL DEFAULT 0,
    criado_em     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orcamento_manutencao ON orcamento_manutencao(id_manutencao);

CREATE TABLE orcamento_manutencao_item (
    id             BIGSERIAL     PRIMARY KEY,
    id_orcamento   BIGINT        NOT NULL REFERENCES orcamento_manutencao(id) ON DELETE CASCADE,
    descricao      VARCHAR(300)  NOT NULL,
    quantidade     NUMERIC(8,2)  NOT NULL,
    preco_unitario NUMERIC(12,2) NOT NULL,
    valor_total    NUMERIC(12,2) GENERATED ALWAYS AS (quantidade * preco_unitario) STORED,
    criado_em      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orcamento_item_orcamento ON orcamento_manutencao_item(id_orcamento);

CREATE TABLE candidatura_motorista (
    id              BIGSERIAL    PRIMARY KEY,
    nome            VARCHAR(150) NOT NULL,
    cpf             VARCHAR(11)  NOT NULL,
    data_nascimento DATE         NOT NULL,
    telefone        VARCHAR(20)  NOT NULL,
    email           VARCHAR(120) NOT NULL,
    cnh_numero      VARCHAR(20)  NOT NULL,
    cnh_categoria   VARCHAR(2)   NOT NULL,
    cnh_validade    DATE         NOT NULL,
    tipo_vinculo    VARCHAR(15)  NOT NULL,
    municipio       VARCHAR(100) NOT NULL,
    uf              CHAR(2)      NOT NULL,
    mensagem        TEXT,
    status          VARCHAR(15)  NOT NULL DEFAULT 'PENDENTE',
    criado_em       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_candidatura_status    CHECK (status        IN ('PENDENTE', 'APROVADA', 'RECUSADA')),
    CONSTRAINT chk_candidatura_categoria CHECK (cnh_categoria IN ('A', 'B', 'C', 'D', 'E')),
    CONSTRAINT chk_candidatura_vinculo   CHECK (tipo_vinculo  IN ('FUNCIONARIO', 'AGREGADO', 'TERCEIRO'))
);

CREATE INDEX idx_candidatura_cpf ON candidatura_motorista(cpf);

CREATE TABLE solicitacao_transporte (
    id                 BIGSERIAL    PRIMARY KEY,
    id_motorista       BIGINT       NOT NULL REFERENCES motorista(id),
    municipio_coleta   VARCHAR(100) NOT NULL,
    uf_coleta          CHAR(2)      NOT NULL,
    logradouro_coleta  VARCHAR(150) NOT NULL,
    municipio_destino  VARCHAR(100) NOT NULL,
    uf_destino         CHAR(2)      NOT NULL,
    logradouro_destino VARCHAR(150) NOT NULL,
    descricao_carga    TEXT,
    status             VARCHAR(10)  NOT NULL DEFAULT 'PENDENTE',
    resposta_motorista TEXT,
    criado_em          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    respondido_em      TIMESTAMP,
    CONSTRAINT chk_solicitacao_status CHECK (status IN ('PENDENTE', 'ACEITA', 'RECUSADA'))
);

CREATE INDEX idx_solicitacao_motorista ON solicitacao_transporte(id_motorista);
CREATE INDEX idx_solicitacao_status    ON solicitacao_transporte(status);