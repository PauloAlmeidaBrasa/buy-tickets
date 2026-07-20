# Buy Tickets API

API REST para compra  de ingressos com autenticacao via JWT.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 4.1
- Spring Web (REST)
- Spring Security (autenticacao com Bearer Token)
- JWT (JJWT)
- Spring Data JPA
- Flyway (migracoes de banco)
- MySQL
- AWS SDK SQS (publicacao/consumo de mensagens)
- OpenAPI/Swagger (springdoc)
- Maven
- Docker (multi-stage build)

## Endpoints Principais

Base path (por padrao): `/api/v1`

- `POST /api/v1/auth/login` (publico)
- `GET /api/v1/ticket/list` (protegido por JWT)
- `GET /api/v1/ticket/user/{userId}` (protegido por JWT)
- `POST /api/v1/ticket/buy` (protegido por JWT)

Swagger UI:

- `http://localhost:8082/v1/swagger`
- `http://localhost:8082/v1/swagger-ui/index.html`

## Variaveis de Ambiente

A aplicacao usa variaveis para banco, JWT e AWS.
Os nomes esperados estao em `.env` e em `src/main/resources/application.properties`.

Exemplo minimo para rodar localmente:

```env
API_VERSION=v1
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/buy_tickets
SPRING_DATASOURCE_USERNAME=buy_tickets
SPRING_DATASOURCE_PASSWORD=buy_tickets_pwd
JWT_SECRET=coloque_uma_chave_forte_de_pelo_menos_32_bytes
JWT_EXPIRATION_MS=3600000
AWS_REGION=us-east-1
AWS_SQS_MESSAGE_GROUP_ID=ticket-reservation-group
```

## Como Rodar Localmente (Maven)

Prerequisitos:

- Java 21
- Maven (ou `./mvnw`)
- MySQL em execucao

Passos:

```bash
./mvnw spring-boot:run
```

A API sobe na porta `8082`.

## Como Rodar com Docker

O projeto tem `Dockerfile.dev` com estagios `dev`, `build` e `runtime`.

### 1) Subir MySQL em container

```bash
docker network create buy-tickets-net

docker run -d \
  --name db \
  --network buy-tickets-net \
  -e MYSQL_DATABASE=buy_tickets \
  -e MYSQL_USER=buy_tickets \
  -e MYSQL_PASSWORD=buy_tickets_pwd \
  -e MYSQL_ROOT_PASSWORD=root_pwd \
  -p 3306:3306 \
  mysql:8.4
```

### 2) Build da aplicacao

```bash
docker build -f Dockerfile.dev --target runtime -t buy-tickets:latest .
```

### 3) Rodar API com env file

```bash
docker run --rm \
  --name buy-tickets-api \
  --network buy-tickets-net \
  --env-file .env \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/buy_tickets \
  -p 8082:8082 \
  buy-tickets:latest
```

Depois acesse:

- API: `http://localhost:8082`
- Swagger: `http://localhost:8082/v1/swagger`

## Como Acessar a Documentacao (Swagger)

Com a API em execucao, voce pode acessar a documentacao de duas formas:

- Interface Swagger UI: `http://localhost:8082/v1/swagger`
- Especificacao OpenAPI (JSON): `http://localhost:8082/v1/api-docs`

Para checar via terminal se a documentacao JSON esta disponivel:

```bash
curl -s http://localhost:8082/v1/api-docs | head
```

Se `API_VERSION` for alterada (por exemplo para `v2`), as rotas da documentacao mudam para:

- Swagger UI: `/v2/swagger`
- OpenAPI JSON: `/v2/api-docs`

## JWT para Consumir a API

Todos os endpoints de ticket exigem header:

```http
Authorization: Bearer <seu_token_jwt>
```

### 1) Obter token

```bash
curl -X POST 'http://localhost:8082/api/v1/auth/login' \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "userteste@gmail.com",
    "password": "<senha_do_usuario_seedado>"
  }'
```

Resposta esperada (exemplo):

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1,
  "username": "userteste",
  "email": "userteste@gmail.com",
  "whatsapp": "+5511999999999"
}
```

### 2) Consumir endpoint protegido

```bash
curl -X GET 'http://localhost:8082/api/v1/ticket/list' \
  -H 'Authorization: Bearer SEU_TOKEN_AQUI'
```

## Observacoes Importantes

- O token e assinado com `JWT_SECRET` e expira conforme `JWT_EXPIRATION_MS`.
- Nao commitar segredos reais (AWS/JWT) em repositos publicos.
- Se alterar `API_VERSION`, os caminhos dos endpoints e Swagger mudam.
