# LibrasJá - API Backend (DevOps & Cloud Computing)

Este repositório contém o código-fonte do backend do projeto **LibrasJá** (desenvolvido em Java 17 + Maven), bem como as configurações da esteira de Integração e Entrega Contínuas (CI/CD) desenvolvidas para a disciplina de DevOps.

## ⚙️ Como Executar a Pipeline (CI/CD)

A pipeline foi construída utilizando o **Azure DevOps** com a abordagem de configuração via código (*Configuration as Code*). Toda a infraestrutura em nuvem roda no ecossistema da Microsoft Azure, conectada a um banco de dados **Oracle**.

1. **O Código da Pipeline:** Toda a configuração de Integração Contínua está localizada no arquivo `azure-pipelines.yml` na raiz deste repositório.
2. **Integração Contínua (CI):** Para reproduzir, basta realizar um *Push* para a branch `main` ou disparar a pipeline manualmente no painel do Azure DevOps. A esteira provisionará uma máquina Ubuntu, configurará o Java 17, executará o *Build* via Maven e publicará o artefato (`.jar`) em um contêiner de *Drop*.
3. **Entrega Contínua (CD):** Após o sucesso do CI, a *Release Pipeline* (configurada no portal do Azure DevOps) realiza o download do artefato gerado e faz o *Deploy* automático no **Azure App Service** (Linux), disponibilizando a aplicação na nuvem.

## 🧪 Como Testar a Aplicação (Scripts JSON para CRUD)

O professor pode reproduzir os testes e validar o funcionamento da aplicação e a persistência no banco de dados utilizando a documentação interativa (Swagger) que já está em produção na nuvem:

**🔗 Acesse o Swagger:** `https://librasja-api-fahccebwdkeje0eg.eastus-01.azurewebsites.net//swagger-ui/index.html`

Utilize os *bodies* (cargas úteis) abaixo nas rotas de `POST` (Criação) dentro do Swagger para testar as inserções no banco:

### 1. Cadastro de Usuário Padrão (Tabela `users`)
```json
{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "password": "123456"
}
```

#### 2. Cadastro de Intérprete
```json
{
  "nome": "João Intérprete",
  "email": "joao@email.com",
  "password": "123456",
  "especialidades": "Saúde, Jurídico",
  "descricaoCurta": "Intérprete com 5 anos de experiência",
  "disponivel": "SEG, TER, QUA"
}
```
