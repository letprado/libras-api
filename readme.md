# Libras API - Sistema de Gestão de Sessões de Interpretação

## Integrantes do Grupo

**[Letícia Sousa Prado]** - RM 559258  
**Responsabilidade:** Java e banco de dados - Implementação da API, HATEOAS, JPA/Hibernate, documentação

**[Jennyfer Lee]** - RM 561020  
**Responsabilidade:** .NET e IoT - Revisão e testes de integração

**[Ivanildo Alfredo]** - RM 560049  
**Responsabilidade:** Mobile, QA e DevOps - Validação dos testes e DevOps

---

## Público-Alvo

- **Pessoas surdas** que necessitam de serviços de interpretação
- **Intérpretes de Libras** profissionais

## Problemas que a Aplicação Resolve

1. **Falta de rastreabilidade**: Registra todas as sessões e seu histórico
2. **Ausência de feedback**: Permite avaliar a qualidade do serviço
3. **Gestão manual**: Automatiza o controle de sessões de interpretação
4. **Métricas inexistentes**: Gera dados para análise de qualidade

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA** + **Hibernate**
- **Spring HATEOAS** (Nível 3 REST)
- **Oracle Database**
- **Swagger/OpenAPI**
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
- API Base: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

---

## Vídeo de Apresentação

**Link:** https://youtu.be/ZGvI0K_um1k

No vídeo apresentamos:
- Proposta tecnológica e contexto social
- Público-alvo e problemas resolvidos

---

## Diagramas

### Arquitetura do Sistema
![Diagrama de Arquitetura](documentacao/diagrama-arquitetura.png)

### Diagrama Entidade-Relacionamento (DER)
![Diagrama DER](documentacao/diagrama-der.png)

### Diagrama de Classes das Entidades
![Diagrama de Classes](documentacao/diagrama-classes.png)

---

## Evolução Sprint 1 → Sprint 2

### Sprint 1
- API REST básica com Spring Boot
- Entidades JPA (Session e Feedback)
- Mapeamento ORM com @OneToMany, @ManyToOne
- Endpoints CRUD básicos
- Integração com Oracle Database

### Sprint 2
- **Implementação de HATEOAS nível 3** (Richardson Maturity Model)
- **Links hipermedia** em todas as respostas (_links, self, all-sessions, start, finish)
- **Refatoração** dos controllers para EntityModel e CollectionModel
- **Documentação completa** no repositório
- **Coleção de testes** Postman exportada
- **Cronograma detalhado** de desenvolvimento  

---

## Documentação da API

### Sessions

#### `POST /sessions`
Cria uma nova sessão de interpretação.

**Request Body:**
```json
{
  "requesterId": 1,
  "interpreterId": 2,
  "status": "PENDENTE"
}
```

**Response com HATEOAS:**
```json
{
  "id": 1,
  "requesterId": 1,
  "interpreterId": 2,
  "status": "PENDENTE",
  "_links": {
    "self": {"href": "http://localhost:8080/sessions/1"},
    "all-sessions": {"href": "http://localhost:8080/sessions"},
    "start": {"href": "http://localhost:8080/sessions/1/start"},
    "finish": {"href": "http://localhost:8080/sessions/1/finish"}
  }
}
```

#### `GET /sessions`
Lista todas as sessões com links HATEOAS.

#### `GET /sessions/{id}`
Busca uma sessão específica por ID.

#### `POST /sessions/{id}/start`
Inicia uma sessão (status: CONECTADO).

#### `POST /sessions/{id}/finish`
Finaliza uma sessão (status: FINALIZADO).

---

### Feedbacks

#### `POST /feedbacks`
Cria um novo feedback para uma sessão.

**Request Body:**
```json
{
  "session": {
    "id": 1
  },
  "rating": 5,
  "comentario": "Excelente atendimento!"
}
```

**Validações:**
- `rating` deve estar entre 1 e 5 (usando @Min e @Max)

#### `GET /feedbacks`
Lista todos os feedbacks com links HATEOAS.

---

## Mapeamento JPA/Hibernate

### Entidade Session
- `@Entity` + `@Table(name = "sessions")`
- Chave primária: `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- Relacionamento: `@OneToMany(mappedBy = "session")` com Feedback
- Campos obrigatórios: requesterId, interpreterId, status

### Entidade Feedback
- `@Entity` + `@Table(name = "FEEDBACKS")`
- Chave primária: `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- Relacionamento: `@ManyToOne` + `@JoinColumn(name = "session_id")` com Session
- Validação Bean Validation: `@Min(1)` + `@Max(5)` para rating

---

## Constraints e Relacionamentos

- **Relacionamento:** Session (1) ↔ Feedback (N)
- **Chave Estrangeira:** Feedback.session_id → Session.id (NOT NULL)
- **Constraints:** Todos os campos marcados com nullable = false
- **Validações:** Rating entre 1 e 5 estrelas
- **Timestamps:** createdAt automático

---

## HATEOAS (Nível 3 REST)

A API implementa **HATEOAS (Hypermedia as the Engine of Application State)** nível 3 do Richardson Maturity Model:

- Todas as respostas incluem links para navegação
- Cliente descobre as ações disponíveis através dos links
- Links incluem: self, all-sessions, start, finish, all-feedbacks
- CollectionModel para listagens com links embutidos

---

## Cronograma de Desenvolvimento

Veja o cronograma detalhado da Sprint 2: [CRONOGRAMA_SPRINT_2.md](documentacao/CRONOGRAMA_SPRINT_2.md)

---

## Testes

Coleção completa de testes do Postman disponível em:
**`documentacao/Libras_API_Collection.json`**

Os testes incluem:
- Validação de status codes
- Verificação de HATEOAS links
- Validação de persistência de dados
- Testes de rating (1-5)

### Swagger UI

A API possui documentação interativa via Swagger UI:

![Swagger UI - Libras API](documentacao/imagem_swagger_libras_api.png)

### Demonstração de Testes no Postman

#### GET /sessions
![GET Sessions](documentacao/imagem_postman_GET_SESSIONS.png)

#### GET /sessions/{id}
![GET Session por ID](documentacao/imagem_postman_GET_SESSIONS_ID.png)

#### POST /sessions/{id}/start
![Start Session](documentacao/imagem_postman_POST_SESSIONS_ID_START.png)

#### GET /feedbacks
![GET Feedbacks](documentacao/imagem_postman_GET_FEEDBACKS.png)

#### POST /feedbacks
![POST Feedback](documentacao/imagem_postman_POST_FEEDBACKS.png)

---

## Documentação Adicional

- [Cronograma Sprint 2](documentacao/CRONOGRAMA_SPRINT_2.md)
- [Coleção Postman](documentacao/Libras_API_Collection.json)
- [Diagramas](documentacao/)

---

## Licença

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina **Java Advanced - FIAP**.

---

## Repositório

**GitHub:** https://github.com/letprado/libras-api.git
