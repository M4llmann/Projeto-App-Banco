# 🏦 Aplicação Bancária - Sistema de Gestão Financeira

Este projeto é uma **aplicação bancária completa** desenvolvida com tecnologias modernas, permitindo gerenciar usuários, contas bancárias e realizar transações financeiras. O sistema foi construído com o objetivo de aprender e explorar as melhores práticas de desenvolvimento full-stack.

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Documentação da API](#-documentação-da-api)
- [Endpoints Disponíveis](#-endpoints-disponíveis)
- [Acesso à Aplicação](#-acesso-à-aplicação)

## 💻 Sobre o Projeto

Este **APP Bancário** é uma aplicação full-stack que simula funcionalidades básicas de um sistema bancário, incluindo:

- Gerenciamento de usuários com autenticação JWT
- Criação e gestão de contas bancárias
- Realização de depósitos e saques
- Consulta de saldo e extrato de transações
- Interface web moderna e responsiva

## 🚀 Funcionalidades

### Autenticação e Usuários
- ✅ **Criação de Usuário**: Registro de novos usuários no sistema com validação de dados
- ✅ **Login com JWT**: Autenticação segura usando tokens JWT
- ✅ **Hash de Senhas**: Senhas criptografadas com BCrypt para máxima segurança
- ✅ **Busca de Usuário**: Consulta de usuários por e-mail

### Gestão de Contas
- ✅ **Criação de Conta**: Criação de contas bancárias associadas a usuários
- ✅ **Listagem de Contas**: Visualização de todas as contas do usuário
- ✅ **Consulta de Conta**: Visualização de detalhes da conta
- ✅ **Consulta de Saldo**: Verificação do saldo atual em tempo real

### Transações Financeiras
- ✅ **Depósito**: Adição de valores ao saldo da conta com validações
- ✅ **Saque**: Retirada de valores (com validação de saldo suficiente)
- ✅ **Extrato**: Visualização completa do histórico de transações
- ✅ **Validações de Negócio**: Verificações de saldo, valores positivos e status da conta

### Interface do Usuário
- ✅ **Design Moderno**: Interface com gradientes e animações suaves
- ✅ **Responsivo**: Totalmente adaptável para dispositivos móveis e desktop
- ✅ **Loading States**: Feedback visual durante operações assíncronas
- ✅ **Tratamento de Erros**: Mensagens de erro claras e informativas
- ✅ **Validações em Tempo Real**: Validação de formulários com feedback imediato

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21**: Linguagem de programação
- **Spring Boot 3.3.6**: Framework para desenvolvimento de APIs REST
- **Spring Security**: Autenticação e autorização
- **BCrypt**: Criptografia de senhas
- **JWT (JSON Web Tokens)**: Autenticação stateless
- **Spring Data JPA**: Persistência de dados
- **Hibernate Validator**: Validação de dados
- **Spring Validation**: Validação de requisições
- **Lombok**: Redução de boilerplate code
- **SpringDoc OpenAPI**: Documentação automática da API (Swagger)
- **SLF4J/Logback**: Sistema de logging profissional
- **Global Exception Handler**: Tratamento centralizado de exceções

### Frontend
- **Angular 19**: Framework para desenvolvimento web
- **TypeScript**: Superset do JavaScript
- **RxJS**: Programação reativa
- **Angular Router**: Navegação e roteamento
- **CSS3 Moderno**: Gradientes, animações e design responsivo
- **Validação de Formulários**: Validação em tempo real com feedback visual

### Banco de Dados
- **PostgreSQL 14**: Sistema de gerenciamento de banco de dados relacional

### Infraestrutura
- **Docker**: Containerização da aplicação
- **Docker Compose**: Orquestração de containers
- **Maven**: Gerenciamento de dependências (Backend)
- **npm**: Gerenciamento de dependências (Frontend)

## 📁 Estrutura do Projeto

```
Projeto-Aplicação-Bancária/
├── AppBanco/                    # Backend (Spring Boot)
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/Backend/AppBanco/
│   │       │       ├── controller/      # Controllers REST
│   │       │       ├── service/         # Lógica de negócio
│   │       │       ├── repository/      # Camada de acesso a dados
│   │       │       ├── entity/          # Entidades JPA
│   │       │       ├── dto/             # Data Transfer Objects
│   │       │       └── security/       # Configurações de segurança
│   │       └── resources/
│   │           └── application.properties
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                    # Frontend (Angular)
│   ├── src/
│   │   └── app/
│   │       ├── components/      # Componentes Angular
│   │       ├── services/        # Serviços HTTP
│   │       └── models/          # Modelos TypeScript
│   ├── Dockerfile
│   └── package.json
├── Docker/                      # Configurações Docker
│   ├── docker-compose.yml      # Orquestração dos containers
│   └── dataPostgresql/         # Dados persistentes do PostgreSQL
└── README.md
```

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [Docker Desktop](https://www.docker.com/products/docker-desktop) (versão mais recente)
- [Git](https://git-scm.com/downloads) (para clonar o repositório)
- **Opcional**: Java 21 e Maven (se quiser rodar o backend localmente)
- **Opcional**: Node.js 22+ e Angular CLI (se quiser rodar o frontend localmente)

## 🚀 Instalação e Execução

### 1. Clonar o Repositório

```bash
git clone https://github.com/M4llmann/TesteFull.git
cd TesteFull
```

### 2. Subir os Containers com Docker Compose

Navegue até a pasta `Docker` e execute os seguintes comandos:

```bash
cd Docker

# Primeiro, suba apenas o banco de dados
docker compose up db

# Aguarde alguns segundos para o banco inicializar completamente
# Em outro terminal, pare os containers
docker compose down

# Agora suba todos os serviços
docker compose up
```

**Nota**: É importante seguir essa sequência para garantir que o banco de dados esteja totalmente inicializado antes de iniciar a aplicação.

### 3. Verificar se os Containers Estão Rodando

```bash
docker compose ps
```

Você deve ver três containers em execução:
- `banco-db` (PostgreSQL)
- `app-banco` (Backend Spring Boot)
- `banco-frontend` (Frontend Angular)

### 4. Acessar a Aplicação

Após os containers iniciarem (pode levar alguns minutos na primeira execução), acesse:

- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Swagger/API Docs**: http://localhost:8080/api-docs

## 📚 Documentação da API

A documentação completa da API está disponível através do Swagger UI. Acesse:

```
http://localhost:8080/api-docs
```

O Swagger fornece:
- Lista completa de endpoints
- Descrição detalhada de cada operação
- Parâmetros de entrada e saída
- Exemplos de requisições e respostas
- Interface interativa para testar os endpoints

## 🔌 Endpoints Disponíveis

### Usuários (`/api/usuarios`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/usuarios` | Criar novo usuário |
| GET | `/api/usuarios/{email}` | Buscar usuário por e-mail |
| POST | `/api/usuarios/login` | Efetuar login (retorna JWT) |

### Contas (`/api/contas`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/contas/{idUsuario}` | Criar nova conta |
| GET | `/api/contas/usuario/{idUsuario}` | Listar contas por usuário |
| GET | `/api/contas/{idConta}` | Buscar conta por ID |
| GET | `/api/contas/{idConta}/saldo` | Consultar saldo |
| POST | `/api/contas/{idConta}/deposito?valor={valor}` | Realizar depósito |
| POST | `/api/contas/{idConta}/saque?valor={valor}` | Realizar saque |
| GET | `/api/contas/{idConta}/extrato` | Obter extrato de transações |

## 🌐 Acesso à Aplicação

### URLs Principais

- **Frontend Web**: http://localhost:4200
- **API Backend**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/api-docs
- **PostgreSQL**: localhost:54321 (porta exposta)

### Credenciais Padrão do Banco de Dados

- **Host**: localhost
- **Porta**: 54321
- **Database**: postgres
- **Usuário**: postgres
- **Senha**: postgres

## 🛑 Parar os Containers

Para parar todos os containers:

```bash
cd Docker
docker compose down
```

Para parar e remover volumes (apaga os dados do banco):

```bash
docker compose down -v
```

## 📸 Amostra

![Amostra da Aplicação](https://github.com/user-attachments/assets/6e7e1554-68ac-4c1e-a8d0-6c33b6834a4b)

## 🔒 Segurança

- **Autenticação JWT**: Tokens seguros para autenticação stateless
- **Criptografia de Senhas**: BCrypt para hash seguro de senhas
- **Spring Security**: Proteção de endpoints com filtros JWT
- **Validação de Dados**: Hibernate Validator e Spring Validation
- **Tratamento de Exceções**: Global Exception Handler para respostas padronizadas
- **CORS Configurado**: Comunicação segura entre frontend e backend
- **Validações de Negócio**: Verificações de saldo, valores e status de contas
- **Logging**: Sistema de logs para auditoria e debugging

## ✨ Melhorias Implementadas

### Backend
- ✅ **Exceções Customizadas**: Classes específicas para diferentes tipos de erros
- ✅ **Global Exception Handler**: Tratamento centralizado e padronizado de exceções
- ✅ **BCrypt para Senhas**: Criptografia segura de senhas antes de armazenar
- ✅ **Logging Profissional**: Sistema de logs com SLF4J para rastreamento
- ✅ **Validações Robustas**: Validação de dados de entrada e regras de negócio
- ✅ **Transações**: Uso de `@Transactional` para garantir consistência de dados
- ✅ **DTOs Melhorados**: Separação clara entre entidades e objetos de transferência
- ✅ **Documentação Swagger**: API documentada automaticamente

### Frontend
- ✅ **Design Moderno**: Interface com gradientes, sombras e animações suaves
- ✅ **Loading States**: Feedback visual durante operações assíncronas
- ✅ **Tratamento de Erros**: Mensagens de erro claras e informativas
- ✅ **Validações de Formulário**: Validação em tempo real com feedback
- ✅ **Responsividade**: Totalmente adaptável para mobile e desktop
- ✅ **UX Melhorada**: Animações, transições e feedback visual
- ✅ **Acessibilidade**: Labels, placeholders e estados de botões desabilitados

## 📝 Notas Importantes

- Na primeira execução, o banco de dados pode levar alguns minutos para inicializar completamente
- O backend aguarda automaticamente o banco de dados estar pronto antes de iniciar
- Os dados do PostgreSQL são persistidos na pasta `Docker/dataPostgresql`
- Para desenvolvimento local sem Docker, ajuste as configurações de conexão em `application.properties`

## 🤝 Contribuindo

Este é um projeto de aprendizado. Sinta-se à vontade para fazer fork, sugerir melhorias ou reportar problemas!

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.

---

**Desenvolvido com ❤️ para aprendizado e exploração de tecnologias modernas**
