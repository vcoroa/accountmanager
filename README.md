# Account Manager

Este é um projeto para implementar uma API REST para um sistema de gerenciamento de contas. O sistema permite realizar operações de CRUD em uma conta, alterar seu status quando um pagamento é efetuado, obter informações sobre as contas cadastradas no banco de dados e importar um lote de contas a partir de um arquivo CSV.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- PostgreSQL
- Docker e Docker Compose
- Autenticação com Spring Security (jwt)
- Domain Driven Design
- Flyway
- JPA

## Funcionalidades

- Cadastro de contas a pagar
- Atualização de contas a pagar
- Alteração do status da conta a pagar
- Consulta de contas a pagar com filtro por data de vencimento e descrição
- Consulta de conta a pagar por ID
- Consulta do valor total pago por período
- Importação de contas a pagar a partir de um arquivo CSV

## Como Executar o Projeto

1. Clone o repositório para a sua máquina local.
2. Navegue até a pasta do projeto.
3. Execute o comando `docker-compose up -d` para iniciar a aplicação e o banco de dados.
4. A aplicação estará disponível no endereço `http://localhost:8080`.

## Como Executar os Testes

1. Navegue até a pasta do projeto.
2. Execute o comando `mvn test` para executar os testes unitários.

## Como Contribuir

1. Faça um fork do projeto.
2. Crie uma nova branch com as suas alterações.
3. Faça um commit das suas alterações.
4. Faça um push para a sua branch.
5. Envie um pull request a partir da sua branch.

## API Endpoints

### Autenticação

- **Login**
  - POST `/api/autenticacao/login`
  - Headers: `Content-Type: application/json`
  - Body: `{ "username": "<username>", "password": "<password>" }`

- **Registrar**
  - POST `/api/autenticacao/registrar`
  - Headers: `Content-Type: application/json`
  - Body: `{ "username": "<username>", "password": "<password>" }`

### Contas

- **Criar Conta**
  - POST `/api/contas`
  - Headers: `Content-Type: application/json`, `Authentication: Bearer <token>`
  - Body: `{ "dataVencimento": "<date>", "dataPagamento": "<date>", "valor": <value>, "descricao": "<description>", "situacao": "<status>", "tipo": <type> }`

- **Atualizar Conta**
  - PUT `/api/contas/<id>`
  - Headers: `Content-Type: application/json`
  - Body: `{ "dataVencimento": "<date>", "dataPagamento": "<date>", "valor": <value>, "descricao": "<description>", "situacao": "<status>", "tipo": <type> }`

- **Alterar Situação da Conta**
  - PATCH `/api/contas/<id>/situacao`
  - Headers: `Content-Type: application/json`
  - Body: `"<status>"`

- **Obter Lista de Contas**
  - GET `/api/contas?page=<page>&size=<size>`
  - Headers: `Authentication: Bearer <token>`

- **Obter Lista de Contas com Filtro**
  - GET `/api/contas?dataVencimento=<date>`
  - Headers: `Authentication: Bearer <token>`

- **Obter Conta por ID**
  - GET `/api/contas/<id>`

- **Obter Valor Total Pago por Período**
  - GET `/api/contas/valor-total-pago?inicio=<start_date>&fim=<end_date>`
  - Headers: `Authentication: Bearer <token>`

- **Importar CSV**
  - POST `/api/contas/importar`
  - Headers: `Authentication: Bearer <token>`
  - Body: `form-data: { "file": "<file_path>" }`