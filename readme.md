# LibrasJá — Sistema de Gestão de Sessões de Interpretação em Libras

> Plataforma que conecta pessoas surdas a intérpretes de Libras, permitindo agendar, gerenciar e avaliar sessões de interpretação de forma digital e acessível.

---

## 🔗 Links da Aplicação

| Recurso | URL |
|---|---|
| **API em produção (Railway)** | https://libras-api-production.up.railway.app |
| **Documentação Swagger** | https://libras-api-production.up.railway.app/swagger-ui/index.html |
| **Vídeo da sprint (até 5 min)** | em andamento |
| **Vídeo completo de demonstração** | em andamento |

---

## 👥 Integrantes do Grupo

| Nome | RM | Responsabilidade |
|---|---|---|
| **Letícia Sousa Prado** | 559258 | Java Advanced — API REST, banco de dados, deploy |
| **Jennyfer Lee** | 561020 | .NET e IoT |
| **Ivanildo Alfredo** | 560049 | Mobile (frontend), QA e DevOps |

---

## 🎯 Problema e Solução

**Problema:** Pessoas surdas enfrentam barreiras de comunicação no acesso a serviços de saúde, educação e atendimentos em geral por falta de intérpretes de Libras disponíveis de forma ágil e organizada.

**Solução:** O LibrasJá é uma plataforma que conecta usuários surdos (solicitantes) a intérpretes profissionais de Libras. Permite agendar sessões futuras, gerenciar atendimentos em andamento, registrar feedbacks e gerar relatórios de desempenho — tudo via API REST segura com autenticação JWT.

---

## 🏗️ Arquitetura da Solução

```
                        ┌──────────────────────┐     ┌──────────────┐
┌─────────────────┐     │  Java API (Railway)  │────▶│ Oracle DB    │
│  Mobile         │────▶│  Spring Boot 3       │     │ (FIAP)       │
│  (Ivanildo)     │     │  Spring Security JWT │     └──────────────┘
│                 │     │  OpenFeign           │
│  também consome │     │                      │     ┌──────────────┐
│  .NET e IoT     │     └──────────────────────┘     │  RabbitMQ    │
│  (Jennyfer)     │                                  │  (Railway)   │
└─────────────────┘                                  └──────────────┘
```

> O frontend mobile consome tanto esta API Java quanto os serviços .NET e IoT desenvolvidos pela Jennyfer. Os três módulos são independentes entre si — a integração acontece no app mobile.

---

## 🛠️ Tecnologias Utilizadas

### Backend Java (este repositório)
| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.3.4 | Framework web |
| Spring Security 6 | 6.3.x | Autenticação e autorização |
| JWT (jjwt) | 0.12.3 | Tokens de acesso |
| Spring Data JPA | — | Persistência de dados |
| Oracle Database | — | Banco de dados relacional |
| RabbitMQ | 3.x | Mensageria assíncrona |
| OpenFeign | — | Comunicação entre serviços |
| Swagger OpenAPI | — | Documentação da API |
| Docker | — | Containerização |
| Railway | — | Deploy em produção |

---

## 📁 Estrutura do Repositório

```
libras-api/
├── libras-api/          # Projeto Java Spring Boot
│   ├── src/
│   │   ├── main/java/   # Código fonte
│   │   └── resources/   # Configurações
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── railway.toml
│   └── readme.md        # Documentação detalhada da API
└── readme.md            # Este arquivo
```

---

## 🚀 Principais Funcionalidades

- **Autenticação JWT** — cadastro e login para solicitantes e intérpretes
- **Gestão de Sessões** — criar, iniciar, finalizar sessões de interpretação
- **Agendamentos** — agendar sessões futuras com data e hora
- **Feedbacks** — avaliação pós-sessão pelo solicitante
- **Relatórios** — métricas e histórico por intérprete
- **Mensageria** — eventos publicados via RabbitMQ (ex: sessão iniciada, feedback registrado)
- **Integração** — comunicação entre serviços via OpenFeign

---

## 🔐 Fluxo de Uso da API

1. `POST /auth/register/requester` — cadastrar usuário surdo
2. `POST /auth/register/interpreter` — cadastrar intérprete
3. `POST /auth/login` — obter token JWT
4. Usar o token no header: `Authorization: Bearer {token}`
5. `POST /sessions` — criar sessão
6. `PUT /sessions/{id}/start` — iniciar sessão
7. `PUT /sessions/{id}/end` — finalizar sessão
8. `POST /feedback` — registrar avaliação
9. `POST /scheduling` — agendar sessão futura
10. `GET /reports` — consultar relatório

---

## ⚙️ Como Executar Localmente

### Pré-requisitos
- Java 17+
- Maven
- Docker (para RabbitMQ)

### 1. Clone o repositório
```bash
git clone https://github.com/letprado/libras-api.git
cd libras-api/libras-api
```

### 2. Configure as variáveis de ambiente
Crie um arquivo `.env` na pasta `libras-api/`:
```
JWT_SECRET=seu-segredo-com-minimo-64-caracteres-para-hs512-aqui
JWT_EXPIRATION_MS=86400000
```

### 3. Suba com Docker Compose
```bash
docker-compose up
```

A API estará disponível em `http://localhost:8080`  
Swagger: `http://localhost:8080/swagger-ui/index.html`

---

> Para mais detalhes sobre as rotas, veja [libras-api/readme.md](libras-api/readme.md)
