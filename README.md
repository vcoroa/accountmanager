# Account Manager

Este é um projeto para implementar uma API REST para um sistema de gerenciamento de contas. O sistema permite realizar operações de CRUD em uma conta, alterar seu status quando um pagamento é efetuado, obter informações sobre as contas cadastradas no banco de dados e importar um lote de contas a partir de um arquivo CSV.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.3.0
- PostgreSQL
- Docker e Docker Compose
- Autenticação com Spring Security e JWT
- Domain Driven Design
- Flyway
- OpenAPI
- JPA

## Funcionalidades

- Cadastro de contas
- Atualização de contas
- Alteração do status da conta
- Consulta de contas com filtro por data de vencimento e descrição
- Consulta de conta por ID
- Consulta do valor total pago por período
- Importação de contas a partir de um arquivo CSV

## Como Executar o Projeto

1. Clone o repositório para a sua máquina local.
2. Navegue até a pasta do projeto.
3. Execute o comando docker-compose up para iniciar a aplicação e o banco de dados.
4. A aplicação estará disponível no endereço http://localhost:8080.

## Como Executar os Testes

1. Navegue até a pasta do projeto.
2. Execute o comando mvn test para executar os testes unitários.

## OpenAPI/Swagger

Este projeto utiliza o OpenAPI/Swagger para documentação da API. A documentação da API pode ser acessada navegando até `http://localhost:8080/swagger-ui/index.html` quando a aplicação está em execução.

## API Endpoints

### Autenticação

Primeiro, você precisa se registrar na API usando o endpoint de registro. Após o registro, você pode fazer login na API usando o endpoint de login. O token retornado pelo endpoint de login será necessário para autenticar as solicitações subsequentes.

- Registrar
  ```curl
  curl --request POST \
    --url http://localhost:8080/api/autenticacao/registrar \
    --header 'Content-Type: application/json' \
    --data '{ "username": "<username>", "password": "<password>" }'
  ```

- Login
  ```curl
  curl --request POST \
    --url http://localhost:8080/api/autenticacao/login \
    --header 'Content-Type: application/json' \
    --data '{ "username": "<username>", "password": "<password>" }'
  ```

### Contas

- Criar Conta
  ```curl
  curl --request POST \
    --url http://localhost:8080/api/contas \
    --header 'Content-Type: application/json' \
    --header 'Authentication: Bearer <token>' \
    --data '{ "dataVencimento": "<date>", "dataPagamento": "<date>", "valor": <value>, "descricao": "<description>", "situacao": "<status>", "tipo": <type> }'
  ```

- Atualizar Conta
  ```curl
  curl --request PUT \
    --url http://localhost:8080/api/contas/<id> \
    --header 'Content-Type: application/json' \
    --data '{ "dataVencimento": "<date>", "dataPagamento": "<date>", "valor": <value>, "descricao": "<description>", "situacao": "<status>", "tipo": <type> }'
  ```

- Alterar Situação da Conta
  ```curl
  curl --request PATCH \
    --url http://localhost:8080/api/contas/<id>/situacao \
    --header 'Content-Type: application/json' \
    --data '"<status>"'
  ```

- Obter Lista de Contas
  ```curl
  curl --request GET \
    --url 'http://localhost:8080/api/contas?page=<page>&size=<size>' \
    --header 'Authentication: Bearer <token>'
  ```

- Obter Lista de Contas com Filtro
  ```curl
  curl --request GET \
    --url 'http://localhost:8080/api/contas?dataVencimento=<date>' \
    --header 'Authentication: Bearer <token>'
  ```

- Obter Conta por ID
  ```curl
  curl --request GET \
    --url http://localhost:8080/api/contas/<id>
  ```

- Obter Valor Total Pago por Período
  ```curl
  curl --request GET \
    --url 'http://localhost:8080/api/contas/valor-total-pago?inicio=<start_date>&fim=<end_date>' \
    --header 'Authentication: Bearer <token>'
  ```

- Importar CSV
  ```curl
  curl --request POST \
  --url http://localhost:8080/api/contas/importar \
  --header 'Authentication: Bearer <token>' \
  --form 'file=@<file_path>'
  ```