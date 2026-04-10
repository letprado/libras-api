# Cronograma de Desenvolvimento - Sprint 2

## Período: Data de Início a Data de Término

---

## 1. Análise e Planejamento (Data: 01/11/2025)
**Responsável:** Letícia Sousa Prado  
**Atividades:**
- Revisão dos requisitos da Sprint 2
- Identificação das melhorias necessárias baseado no feedback da Sprint 1
- Planejamento da implementação de HATEOAS
- Definição das tarefas e distribuição entre membros da equipe

**Status:** CONCLUÍDO

---

## 2. Implementação de HATEOAS (Data: 01/11/2025)
**Responsável:** Letícia Sousa Prado  
**Atividades:**
- Adicionar dependência do Spring HATEOAS no pom.xml
- Refatorar SessionController para retornar EntityModel com links HATEOAS
- Refatorar FeedbackController para retornar EntityModel com links HATEOAS
- Implementar CollectionModel para listagens
- Adicionar links self, all-sessions, start, finish conforme necessário
- Testar endpoints com Postman para validar estrutura HATEOAS

**Status:** CONCLUÍDO

---

## 3. Documentação no Repositório (Data: 01/11/2025)
**Responsável:** Letícia Sousa Prado  
**Atividades:**
- Criar pasta documentacao/ no repositório
- Adicionar imagens dos diagramas (arquitetura, DER, classes)
- Criar arquivo CRONOGRAMA_SPRINT_2.md detalhado
- Atualizar README.md com referências às imagens
- Garantir que vídeo do YouTube esteja linkado corretamente

**Status:** CONCLUÍDO

---

## 4. Testes dos Endpoints (Data: 01/11/2025)
**Responsável:** Letícia Sousa Prado  
**Atividades:**
- Criar coleção completa de testes no Postman
- Exportar collection para JSON
- Adicionar testes automatizados validando HATEOAS
- Validar persistência e recuperação de dados
- Garantir que rating está entre 1 e 5
- Salvar collection na pasta documentacao/

**Status:** CONCLUÍDO

---

## 5. Melhorias na Documentação (Data: 01/11/2025)
**Responsável:** Letícia Sousa Prado  
**Atividades:**
- Revisar README.md removendo código excessivo
- Focar documentação nos pontos solicitados pelo professor
- Adicionar seção explicando evolução da Sprint 1 para Sprint 2
- Adicionar explicação sobre HATEOAS nível 3
- Garantir que diagramas estejam visíveis no README

**Status:** CONCLUÍDO

---

## 6. Validação Final (Data: 01/11/2025)
**Responsável:** Letícia Sousa Prado  
**Atividades:**
- Testar todos os endpoints novamente
- Verificar se HATEOAS está funcionando corretamente
- Validar que todas as imagens estão no repositório
- Conferir se vídeo está linkado
- Garantir que código está limpo sem erros de lint
- Commit e push de todas as alterações

**Status:** CONCLUÍDO

---

## 7. Responsabilidade dos Outros Membros

### Jennyfer Lee - RM 561020
**Responsabilidade:** .NET e IoT  
**Atividades Sprint 2:**
- Revisão e feedback sobre a documentação
- Suporte em testes de integração

### Ivanildo Alfredo - RM 560049
**Responsabilidade:** Mobile, QA e DevOps  
**Atividades Sprint 2:**
- QA dos endpoints da API
- Validação dos testes automatizados
- Verificação de DevOps e CI/CD

---

## Evolução da Sprint 1 para Sprint 2

### Sprint 1 - Funcionalidades Básicas
- API REST com Spring Boot
- Entidades JPA (Session e Feedback)
- Mapeamento ORM correto
- Endpoints CRUD básicos
- Integração com Oracle Database
- Testes iniciais com Postman

### Sprint 2 - Melhorias e Maturidade REST
- Implementação de HATEOAS nível 3 (Richardson Maturity Model)
- Links hipermedia em todas as respostas
- Documentation completa no repositório
- Coleção de testes exportada
- Cronograma detalhado
- Refatoração de código para melhor qualidade

---

## Distribuição de Tempo

| Fase | Tempo Estimado | Tempo Real |
|------|----------------|------------|
| Análise e Planejamento | 2h | 1.5h |
| Implementação HATEOAS | 4h | 3h |
| Documentação | 3h | 3h |
| Testes | 2h | 1h |
| Validação | 1h | 1h |
| **TOTAL** | **12h** | **9.5h** |

---

## Critérios de Aceite

- HATEOAS implementado em todos os controllers
- Links self, all-sessions, start, finish funcionando
- Testes automatizados validando HATEOAS
- Documentação completa no repositório
- Imagens dos diagramas disponíveis
- Vídeo linkado corretamente
- Cronograma detalhado criado
- Coleção Postman exportada

---

## Notas Importantes

- Todas as alterações devem ser commitadas no GitHub
- Profesores devem ter acesso ao repositório
- Código deve estar sem erros de lint
- Documentação deve focar no solicitado, sem excesso de código
- Evolução entre Sprint 1 e 2 deve estar claramente documentada

