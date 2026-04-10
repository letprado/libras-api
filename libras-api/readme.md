# Libras API

Este projeto é uma API para gerenciar sessões de interpretação em Libras, feedbacks, agendamentos e relatórios.

## O que a solução entrega

- cadastro e login de usuários;
- autenticação com JWT;
- criação, listagem, início e finalização de sessões;
- criação e consulta de feedbacks;
- agendamento de sessões futuras;
- geração de relatórios;
- integração com RabbitMQ e Feign;
- documentação em português no Swagger.

## Como rodar

1. Configure o Oracle em `src/main/resources/application.properties`.
2. Rode o build:

```bash
mvn clean install
```

3. Execute a aplicação:

```bash
mvn spring-boot:run
```

4. Acesse o Swagger em `http://localhost:8081/swagger-ui/index.html`.

## Endpoints principais

### Auth
- `POST /auth/register`
- `POST /auth/login`

### Sessões
- `POST /sessions`
- `GET /sessions`
- `GET /sessions/{id}`
- `POST /sessions/{id}/start`
- `POST /sessions/{id}/finish`

### Feedbacks
- `POST /feedbacks`
- `GET /feedbacks`
- `GET /feedbacks/session/{sessionId}`

### Agendamento
- `POST /scheduling/schedule`
- `POST /scheduling/{scheduleId}/confirm`
- `DELETE /scheduling/{scheduleId}/cancel`

### Relatórios
- `POST /reports/generate`
- `GET /reports/{reportId}`
- `GET /reports/interpreter/{interpreterId}`
