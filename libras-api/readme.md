# Libras API - Sistema de Gestão de Sessões de Interpretação

## Integrantes do Grupo
[Letícia Sousa Prado] - RM 559258  
Responsabilidade: Java e banco de dados  

[Jennyfer Lee] - RM 561020  
Responsabilidade: .Net e Iot  

[Ivanildo Alfredo] - RM 560049  
Responsabilidade: Mobile, QA e DevOps. 

### Público-Alvo
- **Pessoas surdas** que necessitam de serviços de interpretação
- **Intérpretes de Libras** profissionais

### Problemas que a Aplicação Resolve
1. **Falta de rastreabilidade**: Registra todas as sessões e seu histórico
2. **Ausência de feedback**: Permite avaliar a qualidade do serviço
3. **Gestão manual**: Automatiza o controle de sessões de interpretação
4. **Métricas inexistentes**: Gera dados para análise de qualidade

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA**
- **Hibernate**
- **Oracle Database**
- **Swagger/OpenAPI 2.5.0**
- **Maven**

---

## Como Rodar a Aplicação

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+ (ou usar o wrapper mvnw incluído)
- Acesso ao banco de dados Oracle

### Passo a Passo

1. **Clone o repositório**
```bash
git clone https://github.com/letprado/libras-api.git
cd libras-api/libras-api
```

2. **Configure o banco de dados**
Edite o arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:oracle:thin:@//SEU_HOST:1521/orcl
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

3. **Compile e rode a aplicação**
```bash
mvn clean install
mvn spring-boot:run
```

4. **Acesse a aplicação**
- API: http://localhost:8080

---

## Vídeo de Apresentação
https://youtu.be/ZGvI0K_um1k / https://www.youtube.com/watch?v=ZGvI0K_um1k
(são o mesmo video, caso não consiga no link tentar o outro )


No vídeo apresentamos:
- Proposta tecnológica e contexto social

# Libras API

Este projeto é uma API para gerenciar sessões de interpretação em Libras (Língua Brasileira de Sinais) e os feedbacks dessas sessões. O objetivo é facilitar o agendamento, acompanhamento e avaliação do serviço de interpretação.

## Como rodar o projeto

**Pré-requisitos:**
- Java 17 ou superior
- Maven 3.6+ (ou usar o mvnw)
- Oracle Database (ou H2 para testes)

**Passos:**
1. Clone o repositório:
   ```
   git clone https://github.com/letprado/libras-api.git
   cd libras-api/libras-api
   ```
2. Configure o banco de dados em `src/main/resources/application.properties`:
   ```
   spring.datasource.url=jdbc:oracle:thin:@//SEU_HOST:1521/orcl
   spring.datasource.username=SEU_USUARIO
   spring.datasource.password=SUA_SENHA
   ```
3. Compile e rode:
   ```
   mvn clean install
   mvn spring-boot:run
   ```
4. Acesse a API em: http://localhost:8080

## O que a API faz

- Permite criar, listar, buscar, iniciar e finalizar sessões de interpretação.
- Permite criar e listar feedbacks para as sessões.
- Possui autenticação com JWT (login e registro).
- Usa mensageria (RabbitMQ) para eventos de sessão e feedback.
- Tem integração com Feign Client para comunicação entre serviços.

## Endpoints principais

### Sessões

- `POST /sessions` — Cria uma nova sessão
  - Exemplo de body:
    ```json
    {
      "requesterId": 1,
      "interpreterId": 2,
      "status": "PENDENTE"
    }
    ```
- `GET /sessions` — Lista todas as sessões
- `GET /sessions/{id}` — Busca uma sessão específica
- `POST /sessions/{id}/start` — Inicia uma sessão
- `POST /sessions/{id}/finish` — Finaliza uma sessão

### Feedbacks

- `POST /feedbacks` — Cria um feedback para uma sessão
  - Exemplo de body:
    ```json
    {
      "session": { "id": 1 },
      "rating": 5,
      "comentario": "Muito bom!"
    }
    ```
- `GET /feedbacks` — Lista todos os feedbacks

## Como funciona cada parte

### Entidades principais

- **User**: representa o usuário do sistema (solicitante ou intérprete). Tem login, senha, e um campo "role" para diferenciar o tipo.
- **Session**: representa uma sessão de interpretação. Tem quem pediu, quem vai interpretar, status (PENDENTE, CONECTADO, FINALIZADO), horários e lista de feedbacks.
- **Feedback**: avaliação de uma sessão, com nota (1 a 5) e comentário.

### Serviços

- **SessionService**: regras para criar, buscar, iniciar e finalizar sessões.
- **FeedbackService**: regras para criar e listar feedbacks.
- **ReportService**: gera relatórios de sessões e feedbacks (usado para estatísticas).
- **SchedulingService**: agenda sessões futuras.

### Segurança

- Usa Spring Security com JWT.
- Para acessar os endpoints, é preciso estar autenticado (exceto login e registro).
- Roles: "REQUESTER" (quem pede a sessão) e "INTERPRETER" (quem interpreta).

### Mensageria (RabbitMQ)

- Quando uma sessão é criada ou finalizada, ou um feedback é criado, um evento é enviado para o RabbitMQ.
- O EventPublisher envia os eventos.
- O EventConsumer recebe e processa os eventos (pode ser usado para notificações, estatísticas, etc).

### Feign Client

- O SessionServiceClient é um Feign Client para buscar sessões em outro serviço HTTP, facilitando a comunicação entre microsserviços.

## Como explicar se perguntarem

- O projeto foi feito para facilitar o controle de sessões de interpretação em Libras.
- Tem autenticação, controle de acesso, e permite avaliar o serviço.
- Usa eventos para integrar com outros sistemas (RabbitMQ).
- O código está simples, sem comentários, para facilitar a leitura.
- O README explica o que faz cada parte, para ajudar a entender.


