# PROJETO AVALIATIVO 1
## Sistema de Gestão de Fretes

[![Java](https://img.shields.io/badge/Java-8-ED8B00?style=flat-square&logo=openjdk)](https://www.java.com)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?style=flat-square&logo=postgresql)](https://www.postgresql.org)
[![JSP](https://img.shields.io/badge/JSP-JavaServer%20Pages-007396?style=flat-square&logo=java)](https://jakarta.ee)
[![JasperReports](https://img.shields.io/badge/JasperReports-6.20.6-darkgreen?style=flat-square)](https://community.jaspersoft.com)
[![Gradle](https://img.shields.io/badge/Gradle-7.6.4-02303A?style=flat-square&logo=gradle)](https://gradle.org)
[![Tomcat](https://img.shields.io/badge/Tomcat-9-F8DC75?style=flat-square&logo=apachetomcat&logoColor=black)](https://tomcat.apache.org)
[![Next.js](https://img.shields.io/badge/Next.js-15-000000?style=flat-square&logo=next.js)](https://nextjs.org)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?style=flat-square&logo=typescript)](https://www.typescriptlang.org)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind-CSS-06B6D4?style=flat-square&logo=tailwindcss)](https://tailwindcss.com)
[![shadcn/ui](https://img.shields.io/badge/shadcn%2Fui-latest-000000?style=flat-square)](https://ui.shadcn.com)
[![Recharts](https://img.shields.io/badge/Recharts-latest-22b5bf?style=flat-square)](https://recharts.org)
[![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)](LICENSE)

Sistema completo para gestão do ciclo de vida de fretes — do cadastro dos envolvidos até a conclusão da entrega com emissão de relatórios — desenvolvido como projeto avaliativo individual com duração de 4 semanas.

---

## 📖 Descrição

O **NextLog** controla o ciclo de vida completo de um frete: cadastro de clientes, motoristas e veículos, emissão de fretes com fluxo de status sequencial, registro de ocorrências de rastreamento e emissão de relatórios via JasperReports.

O projeto é composto por dois módulos desacoplados:

- **Sistema transacional** — Java 8 + JSP, responsável por toda a operação e regras de negócio
- **Dashboard analítico** — Next.js, responsável por visualização de dados e acompanhamento operacional em tempo real

---

## 🗂️ Diagrama de Entidade e Relacionamento

🔗 [Visualizar DER no dbdiagram.io](https://dbdiagram.io/d/Projeto-NextLog-69e8dd4a1bbca0331213959f)

---

## 🏗️ Arquitetura

O sistema transacional segue o padrão de camadas obrigatório:

```
JSP → Controlador → BO → DAO → PostgreSQL
```

| Camada | Responsabilidade |
|--------|-----------------|
| JSP | Exibir dados, renderizar formulários, mostrar mensagens de erro |
| Controlador | Receber parâmetros da requisição, chamar BO, redirecionar para JSP |
| BO | Todas as regras de negócio, validações, orquestração de transações JDBC |
| DAO | Único lugar onde SQL é escrito e ResultSet é mapeado para objetos Java |

O dashboard Next.js consome endpoints REST expostos pelo backend Java via `GET/POST /api/*`, sem acesso direto ao banco.

---

## ⚖️ Regras de Negócio

Todas as regras vivem exclusivamente na camada BO e são comunicadas ao Controlador via hierarquia de exceções (`NegocioException` → `FreteException` / `CadastroException`). Nenhuma regra é verificada apenas por JavaScript na JSP.

### Cadastros
- CNPJ validado por dígito verificador antes de salvar
- CPF do motorista validado por dígito verificador antes de salvar
- CNH vencida bloqueia o motorista para novos fretes, mas não impede cadastro ou edição
- Não é permitido inativar motorista com frete em status `EMITIDO`, `SAÍDA CONFIRMADA` ou `EM TRÂNSITO`
- Não é permitido alterar veículo para `DISPONÍVEL` manualmente se estiver associado a frete `EM TRÂNSITO`
- Não é permitido excluir cliente que possui fretes em qualquer status

### Fretes
- Veículo deve estar `DISPONÍVEL` para ser atribuído a novo frete
- Motorista deve estar `ATIVO` e sem frete em `SAÍDA CONFIRMADA` ou `EM TRÂNSITO`
- CNH do motorista deve estar válida na data de emissão do frete
- Peso da carga não pode exceder a capacidade do veículo
- Data prevista de entrega deve ser posterior à data de emissão
- Número do frete gerado no `FreteBO` — nunca pelo banco ou pelo Controlador
- Transição de status segue fluxo sequencial estrito — saltos lançam `FreteException`
- Ao confirmar saída: veículo atualizado para `EM VIAGEM` na mesma transação JDBC
- Ao registrar entrega ou não entrega: veículo retorna para `DISPONÍVEL` na mesma transação JDBC
- Frete `CANCELADO` não recebe novas ocorrências

### Ocorrências
- Proibido registrar ocorrência em frete `ENTREGUE`, `NÃO ENTREGUE` ou `CANCELADO`
- Data/hora da ocorrência não pode ser anterior à ocorrência mais recente do mesmo frete
- Tipo `ENTREGA REALIZADA` exige nome e documento do recebedor
- Registrar `ENTREGA REALIZADA` atualiza automaticamente o status do frete para `ENTREGUE`

### Transações JDBC
Operações que alteram múltiplas tabelas são executadas com transação manual:
`setAutoCommit(false)` → operações → `commit()` ou `rollback()`.
O DAO recebe a `Connection` como parâmetro quando participa de transação composta.

---

## ⭐ Diferenciais Implementados

| Diferencial | Descrição |
|-------------|-----------|
| **Controle de manutenção de veículos** | Registro de manutenções preventivas e corretivas com alertas visuais para manutenções vencidas |
| **Tabela de frete por rota** | Tarifas por par origem/destino com sugestão automática de valor ao emitir frete |
| **Pool de conexões DBCP** | Apache Commons DBCP configurado via `db.properties` e inicializado no `AppContextListener` |
| **Filtro de autenticação** | `AuthFilter` intercepta todas as requisições e valida sessão antes de prosseguir |
| **Dashboard analítico (Next.js)** | Painel com KPIs, gráficos de performance de entregas, ranking de motoristas e rotas mais movimentadas via Recharts + shadcn/ui |
| **Fluxo de status no dashboard** | Acionamento e acompanhamento do fluxo de status do frete em tempo real pelo painel Next.js |

---

## 🚀 Como Executar

### Pré-requisitos

- SDKMAN instalado
- Java 8 (`sdk install java 8.0.402-tem`)
- Gradle 7.6.4 (`sdk install gradle 7.6.4`)
- PostgreSQL 15+
- Node.js 20+

### Banco de Dados

```bash
# Criar o banco
psql -U postgres -c "CREATE DATABASE nextlog;"

# Executar os scripts na ordem
psql -U postgres -d nextlog -f sql/01_schema.sql
psql -U postgres -d nextlog -f sql/02_seed.sql
psql -U postgres -d nextlog -f sql/03_seed_testes.sql
```

### Backend (Java + Tomcat)

```bash
# Configurar credenciais do banco
cp backend/src/main/resources/db.properties.example backend/src/main/resources/db.properties
# Editar db.properties com suas credenciais

# Ativar Java 8
export JAVA_HOME="$HOME/.sdkman/candidates/java/8.0.402-tem"
export PATH="$JAVA_HOME/bin:$PATH"

# Entrar na pasta do backend e subir o servidor
cd backend
gradle wrapper --gradle-version 7.6.4
./gradlew appRun
```

Acesse: [http://localhost:8080/nextlog](http://localhost:8080/nextlog)

### Dashboard (Next.js)

```bash
cd frontend
npm install
npm run dev
```

Acesse: [http://localhost:3000](http://localhost:3000)

---

## 📁 Estrutura do Projeto

```
frete-sistema-luiz/
├── backend/
│   └── src/main/
│       ├── java/br/com/nextlog/
│       │   ├── auth/               # Login e autenticação
│       │   ├── cadastro/           # Cliente, Motorista, Veículo
│       │   ├── frete/              # Frete e Ocorrências
│       │   ├── manutencao/         # Controle de manutenção
│       │   ├── relatorio/          # JasperReports
│       │   ├── rota/               # Tabela de rotas
│       │   ├── dashboard/          # Endpoints REST para o Next.js
│       │   ├── enums/              # StatusFrete, StatusVeiculo, TipoOcorrencia...
│       │   ├── exception/          # NegocioException, FreteException, CadastroException
│       │   ├── filter/             # AuthFilter, CorsFilter
│       │   ├── listener/           # AppContextListener (pool DBCP)
│       │   └── util/               # CpfValidator, CnpjValidator, FreteNumberGenerator...
│       ├── resources/
│       │   ├── db.properties.example
│       │   └── reports/            # .jrxml dos relatórios
│       └── webapp/
│           ├── WEB-INF/views/      # JSPs por módulo
│           └── static/             # CSS, JS, imagens
├── frontend/
│   └── src/
│       ├── app/                    # App Router Next.js
│       ├── features/               # dashboard, entregas, motoristas, rotas, fretes
│       ├── shared/                 # Componentes e tipos compartilhados
│       └── core/                   # httpClient e utilitários
├── sql/
│   ├── 01_schema.sql               # Tabelas e constraints
│   ├── 02_seed.sql                 # Dados de exemplo
│   └── 03_seed_testes.sql          # Usuários de teste — não executar em produção
└── docs/
    ├── arquitetura.md
    └── decisoes-tecnicas.md
```

---

## 🔧 API REST

Endpoints expostos pelo backend Java para o dashboard Next.js:

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/dashboard/kpis` | Totais gerais |
| `GET` | `/api/dashboard/entregas` | Performance por período |
| `GET` | `/api/dashboard/motoristas` | Ranking por taxa de sucesso |
| `GET` | `/api/dashboard/rotas` | Rotas mais movimentadas |
| `GET` | `/api/fretes/{id}` | Detalhe do frete |
| `GET` | `/api/fretes/{id}/ocorrencias` | Histórico de ocorrências |
| `POST` | `/api/fretes/{id}/confirmar-saida` | Confirma saída do pátio |
| `POST` | `/api/fretes/{id}/registrar-ocorrencia` | Registra ocorrência em rota |
| `POST` | `/api/fretes/{id}/registrar-entrega` | Confirma entrega |
| `POST` | `/api/fretes/{id}/cancelar` | Cancela o frete |

---

## 📅 Cronograma de Entregas

| Semana | Foco | Entrega |
|--------|------|---------|
| 1 | Fundação | Repositório estruturado, SQL com todas as tabelas, hierarquia de exceções, tela de login funcionando |
| 2 | Cadastros | CRUDs completos com validação CPF/CNPJ, paginação, filtro e tratamento de erros |
| 3 | Fretes e Ocorrências | Emissão de frete, fluxo de status completo, ocorrências, transações JDBC |
| 4 | Relatórios e Diferencial | JasperReports, dashboard Next.js, README completo, revisão final |

---

## 👨‍💻 Autor

**Luiz Felipe de Oliveira**

Desenvolvedor Treinee da GW Sistemas — Time de Inovação


- 💼 **LinkedIn:** [in/luizfxdev](https://www.linkedin.com/in/luizfxdev)
- 🐙 **GitHub:** [@luizfxdev](https://github.com/luizfxdev)
