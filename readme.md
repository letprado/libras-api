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
- **Instituições de ensino** e empresas que oferecem acessibilidade

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
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Docs: http://localhost:8080/v3/api-docs

---

## Vídeo de Apresentação

**[Link para o vídeo no YouTube]** (inserir link após gravação)

No vídeo apresentamos:
- Proposta tecnológica e contexto social
- Público-alvo e problemas resolvidos
- Arquitetura e diagramas do sistema
- Demonstração ao vivo dos endpoints
- Testes de persistência no banco Oracle

---

## Documentação da API (Endpoints)

### Sessions

#### `POST /sessions`
Cria uma nova sessão de interpretação
```json
{
  "requesterId": 1,
  "interpreterId": 2,
  "status": "PENDENTE"
}
```

#### `GET /sessions`
Lista todas as sessões

#### `GET /sessions/{id}`
Busca uma sessão específica

#### `POST /sessions/{id}/start`
Inicia uma sessão (muda status para CONECTADO)

#### `POST /sessions/{id}/finish`
Finaliza uma sessão (muda status para FINALIZADO)

---

### Feedbacks

#### `POST /feedbacks`
Cria um novo feedback para uma sessão
```json
{
  "session": {
    "id": 1
  },
  "rating": 5,
  "comentario": "Excelente atendimento!"
}
```

#### `GET /feedbacks`
Lista todos os feedbacks

---

## Diagramas

### Diagrama de Classes
![Diagrama de Classes](docs/diagrama-classes.png)

### DER (Diagrama Entidade-Relacionamento)
![DER](docs/der.png)

### Arquitetura da Aplicação
![Arquitetura](docs/arquitetura.png)

---

## Cronograma de Desenvolvimento

### Sprint 1 (12/10/2025) - CONCLUÍDO
**Responsável:** Letícia Sousa Prado

**Atividades realizadas:**
- Implementação da API REST em Java com Spring Boot
- Criação das entidades JPA: Session e Feedback
- Mapeamento correto das relações entre entidades (@ManyToOne, @JoinColumn)
- Criação de endpoints REST para inserir sessões e feedbacks
- Testes iniciais dos endpoints via Postman
- Garantia de persistência e recuperação de dados no banco

**Status:** Funcionalidades básicas implementadas, API integrada com o banco de dados, endpoints testados e funcionando

### Sprint 2 (Próxima) 


**Atividades planejadas:**
- Implementar validações mais robustas nos endpoints (rating entre 1 e 5, sessões válidas)
- Melhorar tratamento de erros e respostas da API
- Integração completa com app móvel para CRUD de sessões e feedbacks

---

## Arquitetura e Classes de Domínio

### CLASSE SESSION - ENTIDADE PRINCIPAL

A classe Session representa uma sessão de interpretação em Libras e demonstra o mapeamento objeto-relacional com JPA/Hibernate:

```java
package com.librasja.libras_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "interpreter_id", nullable = false)
    private Long interpreterId;

    @Column(nullable = false)
    private String status; // PENDENTE, CONECTADO, FINALIZADO, CANCELADO

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Feedback> feedbacks = new ArrayList<>();

    // Getters e Setters...
}
```

**PRINCIPAIS ANOTAÇÕES JPA IMPLEMENTADAS:**
- `@Entity`: Marca a classe como entidade JPA que será persistida no banco
- `@Table(name = "sessions")`: Define o nome da tabela no banco Oracle
- `@Id + @GeneratedValue(strategy = GenerationType.IDENTITY)`: Configura chave primária com auto-incremento
- `@Column`: Define propriedades das colunas (nullable, name, etc.)
- `@OneToMany(mappedBy = "session", cascade = CascadeType.ALL)`: Estabelece relacionamento um-para-muitos com Feedback
- `@JsonIgnore`: Evita serialização recursiva infinita no JSON

### CLASSE FEEDBACK - ENTIDADE COM RELACIONAMENTO

A classe Feedback representa uma avaliação de sessão e demonstra relacionamentos JPA e validações Bean Validation:

```java
package com.librasja.libras_api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "FEEDBACKS")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Min(1)
    @Max(5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Getters e Setters...
}
```

**PRINCIPAIS ANOTAÇÕES JPA IMPLEMENTADAS:**
- `@ManyToOne(fetch = FetchType.EAGER)`: Define relacionamento muitos-para-um com Session
- `@JoinColumn(name = "session_id", nullable = false)`: Cria chave estrangeira obrigatória
- `@Min(1) @Max(5)`: Validação Bean Validation que garante rating entre 1 e 5 estrelas
- `@Column(insertable = false, updatable = false)`: Campo created_at gerenciado automaticamente pelo banco

---

## Explicação do Relacionamento e Constraints

### RELACIONAMENTO PRINCIPAL: Session ↔ Feedback

**Tipo:** Um-para-Muitos (1:N)
- Uma sessão pode ter vários feedbacks
- Um feedback pertence a apenas uma sessão

**Como funciona no código:**
- Na classe Session: `@OneToMany(mappedBy = "session")`
- Na classe Feedback: `@ManyToOne` com `@JoinColumn(name = "session_id")`

### CONSTRAINTS (REGRAS) IMPLEMENTADAS

1. **Chaves Primárias (PK):**
   - Session.id = chave primária (identificador único)
   - Feedback.id = chave primária (identificador único)

2. **Chave Estrangeira (FK):**
   - Feedback.session_id → referencia Session.id
   - Constraint: nullable = false (feedback DEVE ter uma sessão)

3. **Campos Obrigatórios (NOT NULL):**
   - Session.requesterId = obrigatório (deve ter solicitante)
   - Session.interpreterId = obrigatório (deve ter intérprete)
   - Session.status = obrigatório (deve ter status)
   - Feedback.rating = obrigatório (deve ter avaliação)

4. **Validação de Rating:**
   - @Min(1) e @Max(5) = rating deve estar entre 1 e 5 estrelas

5. **Timestamps Automáticos:**
   - Session.createdAt = preenchido automaticamente pela aplicação
   - Feedback.createdAt = preenchido automaticamente pelo banco

---

## Licença

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina Java Advanced - FIAP.

---

## Repositório

**GitHub:** https://github.com/letprado/libras-api.git