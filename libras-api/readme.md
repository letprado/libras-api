# Libras API

## Sobre o projeto

Este projeto foi desenvolvido para a sprint com o objetivo de organizar o processo de atendimento com interpretação em Libras.

A API permite:
- cadastrar usuários com perfis diferentes;
- autenticar com JWT;
- proteger rotas com base no perfil do usuário;
- criar e acompanhar sessões de interpretação;
- registrar feedbacks sobre o atendimento;
- agendar sessões futuras;
- gerar relatórios com métricas;
- publicar eventos com RabbitMQ;
- realizar integração interna com OpenFeign.

## Tecnologias utilizadas

- Java 17
- Spring Boot 3
- Spring Security
- JWT
- Spring Data JPA
- Oracle Database
- RabbitMQ
- OpenFeign
- Swagger OpenAPI
- Maven

## Como executar

1. Configurar o banco Oracle no arquivo `src/main/resources/application.properties`.
2. Garantir que o RabbitMQ esteja ativo se quiser testar a mensageria.
3. Executar o projeto com Maven.

```bash
./mvnw spring-boot:run
```

No Windows também pode ser usado:

```bash
mvnw.cmd spring-boot:run
```

Por padrão a aplicação sobe na porta `8081`.

## Documentação Swagger

Depois de iniciar a aplicação, a documentação pode ser acessada em:

`http://localhost:8081/swagger-ui/index.html`

## Vídeos da entrega

Vídeo principal da sprint, dentro do limite de até 5 minutos:

https://youtu.be/oy8BbPAjFag

Vídeo mais completo, com demonstração maior do projeto:

https://youtu.be/ljchAsbrSTw

## Fluxo básico de uso

1. Registrar um usuário solicitante.
2. Registrar um usuário intérprete.
3. Fazer login para obter o token JWT.
4. Usar o token nas rotas protegidas.
5. Criar uma sessão.
6. Iniciar e finalizar a sessão.
7. Registrar feedback.
8. Criar agendamento quando for um atendimento futuro.
9. Gerar relatório para acompanhar os dados do intérprete.

## Autenticação

As rotas públicas principais são:
- `POST /auth/register`
- `POST /auth/login`

Depois do login, o token retornado deve ser enviado no header:

`Authorization: Bearer {token}`

Perfis usados no projeto:

- `REQUESTER`: cria sessão, registra feedback e gerencia seus agendamentos.
- `INTERPRETER`: inicia e finaliza sessões, confirma agendamentos e gera relatórios.

## Rotas da API

### 1. Autenticação

`POST /auth/register`

Faz o cadastro de um usuário.

Exemplo de body:

```json
{
	"username": "solicitante1",
	"email": "solicitante1@email.com",
	"password": "123456",
	"role": "REQUESTER"
}
```

`POST /auth/login`

Realiza o login e retorna token, id do usuário, nome e perfil.

Exemplo de body:

```json
{
	"username": "solicitante1",
	"password": "123456"
}
```

### 2. Sessões

`POST /sessions`

Cria uma sessão entre solicitante e intérprete. Acesso do perfil `REQUESTER`.

```json
{
	"requesterId": 1,
	"interpreterId": 2
}
```

`GET /sessions`

Lista todas as sessões cadastradas. Acesso para `REQUESTER` e `INTERPRETER`.

`GET /sessions/{id}`

Busca uma sessão pelo id. Acesso para `REQUESTER` e `INTERPRETER`.

`POST /sessions/{id}/start`

Marca a sessão como iniciada. Acesso do perfil `INTERPRETER`.

`POST /sessions/{id}/finish`

Marca a sessão como finalizada. Acesso do perfil `INTERPRETER`.

### 3. Feedbacks

`POST /feedbacks`

Registra uma avaliação da sessão. Acesso do perfil `REQUESTER`.

```json
{
	"session": {
		"id": 1
	},
	"rating": 5,
	"comentario": "Atendimento muito bom"
}
```

`GET /feedbacks`

Lista todos os feedbacks. Acesso para `REQUESTER` e `INTERPRETER`.

`GET /feedbacks/session/{sessionId}`

Lista os feedbacks de uma sessão específica. Acesso para `REQUESTER` e `INTERPRETER`.

### 4. Agendamento

`POST /scheduling/schedule`

Cria um agendamento futuro. Acesso do perfil `REQUESTER`.

```json
{
	"requesterId": 1,
	"interpreterId": 2,
	"scheduledFor": "2026-12-20T14:00:00",
	"durationMinutes": 60,
	"description": "Sessão para atendimento médico"
}
```

`POST /scheduling/{scheduleId}/confirm`

Confirma o agendamento. Acesso do perfil `INTERPRETER`.

`DELETE /scheduling/{scheduleId}/cancel`

Cancela o agendamento. Acesso para `REQUESTER` e `INTERPRETER`.

`GET /scheduling/interpreter/{interpreterId}`

Lista os agendamentos do intérprete. Acesso do perfil `INTERPRETER`.

`GET /scheduling/requester/{requesterId}`

Lista os agendamentos do solicitante. Acesso do perfil `REQUESTER`.

### 5. Relatórios

`POST /reports/generate`

Gera um relatório por intérprete e período. Acesso do perfil `INTERPRETER`.

```json
{
	"interpreterId": 2,
	"periodStart": "2026-04-10T00:00:00",
	"periodEnd": "2026-04-10T23:59:59"
}
```

`GET /reports/{reportId}`

Busca um relatório pelo id. Acesso do perfil `INTERPRETER`.

`GET /reports/interpreter/{interpreterId}`

Lista os relatórios de um intérprete. Acesso do perfil `INTERPRETER`.

### 6. Integração com Feign

`GET /api/sessions/external`

Lista sessões pela camada de integração com Feign. Acesso para `REQUESTER` e `INTERPRETER`.

`GET /api/sessions/external/{id}`

Busca uma sessão específica pela integração com Feign. Acesso para `REQUESTER` e `INTERPRETER`.

## Mensageria com RabbitMQ

O projeto também publica eventos com RabbitMQ em ações importantes, como:
- criação de sessão;
- finalização de sessão;
- criação de feedback.

Se o RabbitMQ não estiver disponível, essa parte da integração não será testada corretamente.

## Resumo da entrega

O projeto entrega uma API completa para o contexto proposto na sprint, com segurança, persistência, documentação e integração entre componentes. A ideia foi montar um backend funcional e organizado para facilitar o controle das sessões de interpretação em Libras.
