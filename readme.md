# Libras API - Sistema Avançado de Gerenciamento de Sessões de Interpretação

## 📋 Visão Geral

**Libras API** é uma aplicação Spring Boot completa e moderna para gerenciar sessões de interpretação de Libras (Língua Brasileira de Sinais) e feedbacks associados. A aplicação implementa todos os requisitos avançados especificados, incluindo autenticação JWT com dois tipos de usuário, Feign clients para integração síncrona, RabbitMQ para mensageria assíncrona, e fluxos de negócio complexos (não-CRUD).

### 🎯 Requisitos Implementados

- ✅ **Clientes Feign (50 pontos)** - Integração síncrona com chamadas via Feign client
- ✅ **Spring Security (30 pontos)**
  - Dois tipos de usuário: INTERPRETER e REQUESTER com permissões diferentes
  - Proteção de rotas com @PreAuthorize baseada em perfis
- ✅ **Funcionalidades Completas (20 pontos)**
  - Dois fluxos complexos não-CRUD: Agendamento de Sessões e Geração de Relatórios
  - Validações avançadas em criação e atualização
- ✅ **Mensageria (RabbitMQ)** - Comunicação assíncrona entre sistemas
- ✅ **Princípios SOLID e Clean Code** - Arquitetura limpa e legível

## 🏗️ Arquitetura e Estrutura

```
libras-api/
├── config/              # Configurações (Security, RabbitMQ, Feign)
│   ├── SegurityConfig.java
│   ├── RabbitMqConfig.java
│   └── FeignConfig.java
├── controller/          # Endpoints REST
│   ├── AuthController.java
│   ├── SessionController.java
│   ├── FeedbackController.java
│   ├── SessionIntegrationController.java (Feign)
│   ├── ReportController.java (Fluxo 1)
│   └── SchedulingController.java (Fluxo 2)
├── service/             # Lógica de Negócio
│   ├── AuthService.java
|   ├── SessionService.java
│   ├── FeedbackService.java
│   ├── SessionIntegrationService.java
│   ├── ReportService.java (Fluxo 1)
│   └── SchedulingService.java (Fluxo 2)
├── entity/              # Entidades JPA
│   ├── User.java (com Role enum)
│   ├── Session.java
│   ├── Feedback.java
│   ├── InterpretationReport.java
│   └── SessionSchedule.java
├── dto/                 # Data Transfer Objects
│   ├── AuthDtos...
│   ├── SessionDtos...
│   └── ReportDtos...
├── security/            # JWT e Autenticação
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
├── feign/               # Cliente Feign
│   └── SessionServiceClient.java
└── messaging/           # RabbitMQ
    ├── EventPublisher.java
    └── EventConsumer.java
```

## 🔐 Autenticação e Autorização

### Dois Tipos de Usuário com Permissões Distintas

#### INTERPRETER (Intérprete de Libras)
Permissões:
- ✅ Iniciar e finalizar sessões (`/sessions/{id}/start`, `/sessions/{id}/finish`)
- ✅ Confirmar agendamentos (`/scheduling/{id}/confirm`)
- ✅ Receber e visualizar feedbacks
- ✅ Visualizar relatórios de sua performance
- ✅ Listar seus agendamentos

#### REQUESTER (Solicitante de Interpretação)
Permissões:
- ✅ Criar sessões de interpretação
- ✅ Agendar sessões com intérpretes (`/scheduling/schedule`)
- ✅ Deixar feedbacks em sessões (~`/feedbacks`)
- ✅ Visualizar relatórios
- ✅ Cancelar agendamentos

### Autenticação via JWT

**1. Workflow de Autenticação**

```
Client → POST /auth/register → Server
         │
         └─ Cria User com hash de senha (BCrypt)
            └─ Salva no banco
            └─ Retorna JWT token

Client → POST /auth/login → Server
         │
         └─ Valida credenciais
            └─ Gera JWT token
            └─ Retorna token + role

Client → GET /sessions (com Bearer token) → Server
         │
         └─ JwtAuthenticationFilter intercepta
            └─ Valida token
            └─ Extrai user do token
            └─ Carrega authorities
            └─ @PreAuthorize verifica role
            └─ Processa requisição se autorizado
```

**2. Exemplos de Uso**

```bash
# Registrar novo usuário
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "interpreter_01",
    "email": "interpreter@example.com",
    "password": "SecurePass123!",
    "role": "INTERPRETER"
  }'

Response:
{
  "accessToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "username": "interpreter_01",
  "role": "INTERPRETER"
}

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "interpreter_01",
    "password": "SecurePass123!"
  }'

# Usar token em requisições
curl -X GET http://localhost:8080/sessions \
  -H "Authorization: Bearer {accessToken}"
```

## 🚀 50 Pontos: Clientes Feign - Integração Síncrona

### Implementação de Feign Client

**Arquivo: `feign/SessionServiceClient.java`**

```java
@FeignClient(
    name = "sessionServiceFeignClient",
    url = "${feign.session-service.url:http://localhost:8080}"
)
public interface SessionServiceClient {
    @GetMapping("/sessions/{id}")
    SessionResponseDto getSession(@PathVariable Long id);

    @GetMapping("/sessions")
    List<SessionResponseDto> getAllSessions();
}
```

**Arquivo: `service/SessionIntegrationService.java`** (Usa o Feign Client)

```java
@Service
@RequiredArgsConstructor
public class SessionIntegrationService {
    private final SessionServiceClient sessionServiceClient;

    public SessionResponseDto getSessionViaFeign(Long sessionId) {
        log.info("Chamando Feign cliente para obter sessão: {}", sessionId);
        return sessionServiceClient.getSession(sessionId);
    }

    public List<SessionResponseDto> getAllSessionsViaFeign() {
        log.info("Chamando Feign cliente para listar sessões");
        return sessionServiceClient.getAllSessions();
    }
}
```

### Endpoints de Integração Feign

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| **GET** | `/api/sessions/external/{id}` | Obtém sessão via Feign (chamada síncrona) |
| **GET** | `/api/sessions/external` | Lista sessões via Feign (chamada síncrona) |

**Exemplo de Uso:**

```bash
curl -X GET http://localhost:8080/api/sessions/external/1 \
  -H "Authorization: Bearer {token}"

Response:
{
  "id": 1,
  "requesterId": 10,
  "interpreterId": 20,
  "status": "CONECTADO",
  "startedAt": "2026-04-08T10:30:00",
  "createdAt": "2026-04-08T10:00:00"
}
```

## 📨 Mensageria com RabbitMQ

### Configuração de Filas e Exchanges

**Arquivo: `config/RabbitMqConfig.java`**

```java
@Configuration
public class RabbitMqConfig {
    
    public static final String SESSION_EXCHANGE = "libras.sessions.exchange";
    public static final String SESSION_CREATED_QUEUE = "libras.sessions.created.queue";
    public static final String SESSION_FINISHED_QUEUE = "libras.sessions.finished.queue";
    public static final String FEEDBACK_CREATED_QUEUE = "libras.feedbacks.created.queue";
    
    // Beans para criar exchange, queues e bindings
    @Bean
    public TopicExchange sessionExchange() { ... }
    
    @Bean
    public Binding sessionCreatedBinding(...) { ... }
}
```

### Publisher de Eventos

**Arquivo: `messaging/EventPublisher.java`**

```java
@Service
@RequiredArgsConstructor
public class EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishSessionCreatedEvent(SessionEventDto event) {
        rabbitTemplate.convertAndSend(
            RabbitMqConfig.SESSION_EXCHANGE,
            RabbitMqConfig.SESSION_CREATED_ROUTING_KEY,
            event
        );
    }

    public void publishSessionFinishedEvent(SessionEventDto event) { ... }
    public void publishFeedbackCreatedEvent(FeedbackEventDto event) { ... }
}
```

### Consumer de Eventos

**Arquivo: `messaging/EventConsumer.java`**

```java
@Service
public class EventConsumer {
    
    @RabbitListener(queues = RabbitMqConfig.SESSION_CREATED_QUEUE)
    public void handleSessionCreatedEvent(SessionEventDto event) {
        log.info("Evento de sessão criada recebido: sessionId={}", event.getSessionId());
        // Lógica: enviar notificação, atualizar cache, etc
    }

    @RabbitListener(queues = RabbitMqConfig.SESSION_FINISHED_QUEUE)
    public void handleSessionFinishedEvent(SessionEventDto event) { ... }

    @RabbitListener(queues = RabbitMqConfig.FEEDBACK_CREATED_QUEUE)
    public void handleFeedbackCreatedEvent(FeedbackEventDto event) { ... }
}
```

### Integração nos Serviços

Os eventos são publicados automaticamente quando:
- Uma sessão é criada → `publishSessionCreatedEvent()`
- Uma sessão é finalizada → `publishSessionFinishedEvent()`
- Um feedback é deixado → `publishFeedbackCreatedEvent()`

## 🎯 20 Pontos: Funcionalidades Completas - Dois Fluxos de Negócio

### Fluxo 1️⃣: Agendamento de Sessões (Scheduling)

**Caso de Uso:** REQUESTER agenda uma sessão com INTERPRETER com validação de disponibilidade

**Service: `SchedulingService`** - Implementa lógica complexa não-CRUD

```java
public ScheduleSessionResponseDto scheduleSession(ScheduleSessionRequestDto requestDto) {
    // 1. Validar dados de entrada
    validateScheduleRequest(requestDto);
    
    // 2. Buscar usuários (REQUESTER e INTERPRETER)
    User requester = userRepository.findById(requestDto.getRequesterId());
    User interpreter = userRepository.findById(requestDto.getInterpreterId());
    
    // 3. Validar se são os roles corretos
    validateRoles(requester, interpreter);
    
    // 4. Verificar disponibilidade do intérprete no período
    checkInterpreterAvailability(interpreter, requestDto.getScheduledFor(), 
                                 requestDto.getDurationMinutes());
    
    // 5. Validar se a data é futura
    validateFutureDate(requestDto.getScheduledFor());
    
    // 6. Criar agendamento
    SessionSchedule schedule = createSchedule(requester, interpreter, requestDto);
    SessionSchedule saved = scheduleRepository.save(schedule);
    
    // 7. Retornar resposta
    return mapToResponseDto(saved);
}

public ScheduleSessionResponseDto confirmSchedule(Long scheduleId) {
    // Intérprete confirma o agendamento
    SessionSchedule schedule = scheduleRepository.findById(scheduleId);
    schedule.setStatus(CONFIRMED);
    return mapToResponseDto(scheduleRepository.save(schedule));
}

public void cancelSchedule(Long scheduleId) {
    // Cancelar agendamento
    SessionSchedule schedule = scheduleRepository.findById(scheduleId);
    schedule.setStatus(CANCELLED);
    scheduleRepository.save(schedule);
}
```

**Endpoints:**

| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| **POST** | `/scheduling/schedule` | REQUESTER | Criar novo agendamento |
| **POST** | `/scheduling/{id}/confirm` | INTERPRETER | Confirmar agendamento |
| **DELETE** | `/scheduling/{id}/cancel` | REQUESTER/INTERPRETER | Cancelar agendamento |
| **GET** | `/scheduling/interpreter/{id}` | Ambos | Listar agendamentos do intérprete |
| **GET** | `/scheduling/requester/{id}` | Ambos | Listar agendamentos do solicitante |

**Exemplo de Fluxo Completo:**

```bash
# 1. REQUESTER agenda sessão para 15/04/2026 às 14:00 por 60 minutos
curl -X POST http://localhost:8080/scheduling/schedule \
  -H "Authorization: Bearer {requester_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "requesterId": 1,
    "interpreterId": 2,
    "scheduledFor": "2026-04-15T14:00:00",
    "durationMinutes": 60,
    "description": "Reunião importante"
  }'

Response: { "scheduleId": 101, "status": "SCHEDULED", ... }

# 2. INTERPRETER confirma o agendamento
curl -X POST http://localhost:8080/scheduling/101/confirm \
  -H "Authorization: Bearer {interpreter_token}"

Response: { "scheduleId": 101, "status": "CONFIRMED", ... }

# 3.
 REQUESTER ou INTERPRETER pode cancelar se necessário
curl -X DELETE http://localhost:8080/scheduling/101/cancel \
  -H "Authorization: Bearer {token}"
```

### Fluxo 2️⃣: Geração de Relatórios de Interpretação

**Caso de Uso:** Gerar relatório detalhado de performance de um INTERPRETER em um período

**Service: `ReportService`** - Fluxo complexo de análise de dados

```java
public ReportResponseDto generateInterpretationReport(ReportRequestDto requestDto) {
    // 1. Validar dados de entrada
    validateReportRequest(requestDto);
    
    // 2. Buscar usuário intérprete
    User interpreter = userRepository.findById(requestDto.getInterpreterId());
    
    // 3. Validar se é realmente um INTERPRETER
    if (!interpreter.getRole().equals(Role.INTERPRETER))
        throw new IllegalArgumentException("Usuário não é intérprete");
    
    // 4. Buscar todas as sessões do intérprete no período
    List<Session> sessions = findSessionsInPeriod(
        interpreter.getId(),
        requestDto.getPeriodStart(),
        requestDto.getPeriodEnd()
    );
    
    // 5. Coletar todos os feedbacks dessas sessões
    List<Feedback> feedbacks = collectFeedbacksFromSessions(sessions);
    
    // 6. Calcular estatísticas
    int totalSessions = sessions.size();
    int totalFeedbacks = feedbacks.size();
    double averageRating = calculateAverageRating(feedbacks);
    
    // 7. Verificar se já existe relatório para este período
    Optional<InterpretationReport> existing = reportRepository
        .findByInterpreterAndPeriodStartAndPeriodEnd(...);
    
    // 8. Criar ou atualizar relatório
    InterpretationReport report = existing.isPresent() 
        ? updateExisting(existing.get(), ...) 
        : createNew(interpreter, ...);
    
    report.setStatus(COMPLETED);
    InterpretationReport saved = reportRepository.save(report);
    
    // 9. Retornar resposta
    return mapToResponseDto(saved);
}
```

**Endpoints:**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| **POST** | `/reports/generate` | Gerar novo relatório |
| **GET** | `/reports/{reportId}` | Obter relatório específico |
| **GET** | `/reports/interpreter/{interpreterId}` | Listar relatórios do intérprete |

**Exemplo de Uso:**

```bash
# Gerar relatório do intérprete 2 para março/2026
curl -X POST http://localhost:8080/reports/generate \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "interpreterId": 2,
    "periodStart": "2026-03-01T00:00:00",
    "periodEnd": "2026-03-31T23:59:59"
  }'

Response:
{
  "reportId": 1,
  "interpreterId": 2,
  "interpreterName": "jose_interpreter",
  "totalSessions": 15,
  "totalFeedbacks": 12,
  "averageRating": 4.75,
  "periodStart": "2026-03-01T00:00:00",
  "periodEnd": "2026-03-31T23:59:59",
  "status": "COMPLETED",
  "generatedAt": "2026-04-08T14:30:00"
}
```

## 📚 Endpoints Completos

### 🔑 Autenticação

| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| POST | `/auth/register` | Pública | Registrar novo usuário |
| POST | `/auth/login` | Pública | Login e obter JWT |

### 💬 Sessões

| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| POST | `/sessions` | INTERPRETER, REQUESTER | Criar sessão |
| GET | `/sessions` | INTERPRETER, REQUESTER | Listar sessões |
| GET | `/sessions/{id}` | INTERPRETER, REQUESTER | Obter sessão |
| POST | `/sessions/{id}/start` | INTERPRETER | Iniciar sessão |
| POST | `/sessions/{id}/finish` | INTERPRETER | Finalizar sessão |

### ⭐ Feedbacks

| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| POST | `/feedbacks` | REQUESTER | Criar feedback |
| GET | `/feedbacks` | INTERPRETER, REQUESTER | Listar feedbacks |
| GET | `/feedbacks/session/{sessionId}` | INTERPRETER, REQUESTER | Feedback de uma sessão |

### 📅 Agendamento (Fluxo 1)

| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| POST | `/scheduling/schedule` | REQUESTER | Agendar sessão |
| POST | `/scheduling/{id}/confirm` | INTERPRETER | Confirmar agendamento |
| DELETE | `/scheduling/{id}/cancel` | INTERPRETER, REQUESTER | Cancelar agendamento |
| GET | `/scheduling/interpreter/{id}` | INTERPRETER, REQUESTER | Agendamentos do intérprete |
| GET | `/scheduling/requester/{id}` | INTERPRETER, REQUESTER | Agendamentos do solicitante |

### 📊 Relatórios (Fluxo 2)

| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| POST | `/reports/generate` | INTERPRETER, REQUESTER | Gerar relatório |
| GET | `/reports/{reportId}` | INTERPRETER, REQUESTER | Obter relatório |
| GET | `/reports/interpreter/{id}` | INTERPRETER, REQUESTER | Relatórios do intérprete |

### 🔗 Integração Feign

| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| GET | `/api/sessions/external/{id}` | INTERPRETER, REQUESTER | Sessão via Feign |
| GET | `/api/sessions/external` | INTERPRETER, REQUESTER | Listar via Feign |

## 🛠️ Instalação e Execução

### Pré-requisitos

- **Java 17+**
- **Maven 3.8+**
- **Oracle Database** (ou MySQL/PostgreSQL - ajustar datasource)
- **RabbitMQ 3.x**
- **Git**

### Passo a Passo

#### 1. Clone o Repositório

```bash
git clone https://github.com/letprado/libras-api.git
cd libras-api/libras-api
```

#### 2. Configure o Banco de Dados

Edite `src/main/resources/application.properties`:

```properties
# Oracle
spring.datasource.url=jdbc:oracle:thin:@//seu-host:1521/orcl
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# Ou MySQL (exemplo alternativo)
spring.datasource.url=jdbc:mysql://localhost:3306/libras_db
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### 3. Configure o RabbitMQ

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
spring.rabbitmq.virtual-host=/
```

**Para iniciar RabbitMQ com Docker:**

```bash
docker run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

Acesse Management UI: http://localhost:15672 (usuario: guest, senha: guest)

#### 4. Compile e Execute

```bash
# Compilar e baixar dependências
mvn clean install

# Executar aplicação
mvn spring-boot:run

# Ou diretamente
java -jar target/libras_api-1.0.0.jar
```

A aplicação estará disponível em: **http://localhost:8080**

#### 5. Acesse os Recursos

- 📖 **API Swagger**: http://localhost:8080/swagger-ui.html
- 📋 **OpenAPI Docs**: http://localhost:8080/v3/api-docs
- 🐰 **RabbitMQ Admin**: http://localhost:15672

## ✅ Validações Implementadas

### Em SessionService
```java
private void validateSession(Session session) {
    if (session.getRequesterId() == null) {
        throw new IllegalArgumentException("Requester ID é obrigatório");
    }
    if (session.getInterpreterId() == null) {
        throw new IllegalArgumentException("Interpreter ID é obrigatório");
    }
}
```

### Em FeedbackService
```java
private void validateFeedback(Feedback feedback) {
    if (feedback.getRating() == null || feedback.getRating() < 1 || feedback.getRating() > 5) {
        throw new IllegalArgumentException("Rating deve estar entre 1 e 5");
    }
    if (feedback.getComentario() == null || feedback.getComentario().trim().isEmpty()) {
        throw new IllegalArgumentException("Comentário é obrigatório");
    }
    if (feedback.getComentario().length() > 1000) {
        throw new IllegalArgumentException("Comentário não pode exceder 1000 caracteres");
    }
}
```

### Em SchedulingService
```java
private void checkInterpreterAvailability(User interpreter, LocalDateTime scheduledFor, ...) {
    List<SessionSchedule> conflicts = findConflictingSchedules(...);
    if (!conflicts.isEmpty()) {
        throw new IllegalArgumentException("Intérprete não está disponível");
    }
}
```

## 🏛️ Arquitetura Limpa - SOLID e Clean Code

### Single Responsibility
- **SessionService**: Apenas lógica de sessões
- **FeedbackService**: Apenas lógica de feedbacks  
- **ReportService**: Apenas lógica de relatórios
- **SchedulingService**: Apenas lógica de agendamentos

### Open/Closed
- Interfaces para extensibilidade
- Novos event types podem ser adicionados sem modificar EventPublisher

### Liskov Substitution
- UserDetails implementação padrão
- Componentes Spring intercambiáveis

### Interface Segregation
- JpaRepository genérico
- DTOs específicos por funcionalidade

### Dependency Injection
- Todas as dependências via constructor
- Uso de @RequiredArgsConstructor do Lombok

### Clean Code
- Nomes descritivos: `generateInterpretationReport()` vs `gen()`
- Métodos pequenos (<30 linhas)
- Logging apropriado
- Tratamento de erro explícito
- Sem duplicação (DRY)

## 📊 Diagrama de Dados

```
┌─────────────────┐
│  USER           │
├─────────────────┤
│ id (PK)         │
│ username (UNIQUE)
│ email (UNIQUE)  │
│ password        │
│ role (ENUM)     │
│ active          │
└────────┬────────┘
         │
         ├─────────────────┬──────────────────┬────────────────┐
         │                 │                  │                │
  ┌──────▼──────┐  ┌──────▼──────┐  ┌───────▼────┐  ┌─────────▼─┐
  │  SESSION    │  │ FEEDBACK    │  │ SCHEDULE   │  │  REPORT   │
  │             │  │             │  │            │  │           │
  │ id (PK)     │  │ id (PK)     │  │ id (PK)    │  │ id (PK)   │
  │ req_id (FK) │  │ session (FK)│  │ req_id(FK) │  │ interp(FK)│
  │ interp_id   │  │ rating      │  │ interp(FK) │  │ total_sess│
  │ status      │  │ comment     │  │ scheduled  │  │ avg_rating│
  │ started_at  │  │ created_at  │  │ duration   │  │ period    │
  │ ended_at    │  │             │  │ status     │  │           │
  └─────────────┘  └─────────────┘  └────────────┘  └───────────┘
```

## 📊 Requisitos Cumpridos - Pontuação

| Item | Pontos | Status |
|------|--------|--------|
| **Clientes Feign** | 50 | ✅ COMPLETO |
| **Mensageria (RabbitMQ)** | - | ✅ IMPLEMENTADO |
| **Spring Security (JWT + 2 roles)** | 30 | ✅ COMPLETO |
| **Proteção de rotas** | - | ✅ COM @PreAuthorize |
| **Funcionalidades completas** | 20 | ✅ COMPLETO |
| **Fluxo 1: Agendamento** | - | ✅ IMPLEMENTADO |
| **Fluxo 2: Relatórios** | - | ✅ IMPLEMENTADO |
| **Validações** | - | ✅ IMPLEMENTADAS |
| **SOLID e Clean Code** | - | ✅ APLICADOS |

**Total: 100+ pontos** ✅

## 🎬 Demonstração

Vídeo de 5 minutos demonstrando:
1. Autenticação com JWT
2. Fluxos de negócio (agendamento e relatórios)
3. Proteção de rotas por role
4. Mensageria em ação
5. Integração Feign

[Inserir link do vídeo após publicação]

## 🤖 Uso de IA

Esta solução foi desenvolvida com auxílio de IA (GitHub Copilot) para:
- Geração de estrutura de projeto
- Sugestões de padrões de design
- Implementação de boilerplate
- Testes e validações

**Decisões manuais importantes:**
- Arquitetura em camadas
- Escolha de RabbitMQ
- Design dos fluxos de negócio
- Estratégia de autenticação JWT
- DTOs e mappers

## 📝 Notas de Desenvolvimento

- Código não-legado: Moderno, testável, seguindo best practices atuais
- Sem comentários desnecessários: Código é autoexplicativo
- Logging adequado: DEBUG para operações, INFO para eventos
- Tratamento de erros: Exceções customizadas e mensagens claras
- Preparado para avaliação oral: Código bem estruturado para explicar

## 📄 Licença

MIT License - Desenvolvido como trabalho acadêmico FIAP

## 👨‍💼 Integrantes

- **Letícia Sousa Prado** - RM 559258 (Java, Banco de Dados)
- **Jennyfer Lee** - RM 561020 (.NET, IoT)
- **Ivanildo Alfredo** - RM 560049 (Mobile, QA, DevOps)

---

**Repositório GitHub:** https://github.com/letprado/libras-api
