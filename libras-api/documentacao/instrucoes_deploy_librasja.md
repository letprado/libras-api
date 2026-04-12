## Deploy e Execução do Backend — LibrasJá

Este guia descreve como realizar o **deploy** e os **testes** do backend do **LibrasJá** utilizando **Docker** em uma **máquina virtual Linux (Azure)**.

---

### Pré-requisitos

Antes de iniciar o deploy, certifique-se de que:

- Você possui uma **máquina virtual Linux** provisionada no **Microsoft Azure** (ex.: AlmaLinux 10 ou Ubuntu 22.04);
- O **Docker** e o **Docker Compose** estão instalados e configurados corretamente;
- As **portas 22 (SSH)** e **8080 (HTTP)** estão liberadas nas **regras de entrada de rede (NSG)** da VM;
- O repositório foi clonado dentro da VM.

---

### Passos para Deploy

#### 1️- Acesse a máquina virtual via SSH

```bash
ssh azureadmin@158.158.43.154
```


---

#### 2️ - Navegue até o diretório do projeto

```bash
cd ~/libras-api/libras-api
```

Verifique se o arquivo `docker-compose.yml` está presente:
```bash
ls
```

---

#### 3️ - Verifique se o Docker está ativo

```bash
sudo systemctl status docker
```

Se o status for `inactive`, inicie-o com:
```bash
sudo systemctl start docker
```

---

#### 4️ - Realize o build e o deploy do container

```bash
sudo docker compose up -d
```

Este comando:
- Constrói a imagem Docker a partir do `Dockerfile`;
- Executa o backend em modo **background**;
- Expõe a porta `8080` para acesso externo.

---

#### 5️ - Verifique se o container está ativo

```bash
sudo docker ps
```

Saída esperada:

```
CONTAINER ID   IMAGE               STATUS          PORTS
a12b3c4d5e6f   librasja-backend    Up 2 minutes    0.0.0.0:8080->8080/tcp
```

---

#### 6️ - Teste a aplicação dentro da VM

```bash
curl http://localhost:8080
```

Se o backend estiver rodando corretamente, você receberá uma resposta HTTP (JSON, HTML ou erro 404 padrão do Spring Boot).

---

#### 7️ - Teste a aplicação pelo navegador

No seu computador, acesse:

```
http://158.158.43.154:8080
```

> ⚠️ Caso não carregue, confirme se a **porta 8080** está liberada nas **regras de rede (NSG)** da VM.

---

### Comandos úteis

**Encerrar a aplicação:**
```bash
sudo docker compose down
```

**Reiniciar a aplicação (rebuild):**
```bash
sudo docker compose up -d --build
```

**Ver logs em tempo real:**
```bash
sudo docker logs -f librasja-backend
```

---

### Evidências recomendadas para entrega

- Print da **VM criada no Azure** (tela de Overview);
- Print do **Docker Compose rodando** (`sudo docker ps`);
- Print da **aplicação acessível** via `curl` ou navegador;
- Print da **deleção do Resource Group** no portal do Azure (requisito da sprint).

---

### Estrutura do Deploy

```
libras-api/
 ├── Dockerfile
 ├── docker-compose.yml
 ├── pom.xml
 ├── src/
 └── README.md
```

---

### Observações

- O container utiliza uma imagem **leve**, baseada em `eclipse-temurin:17-jdk-alpine`;
- O processo roda em modo **não root**, conforme boas práticas de segurança;
- A aplicação é executada automaticamente em segundo plano (`-d`);
- O deploy foi configurado para garantir portabilidade e fácil reexecução na nuvem.

---
