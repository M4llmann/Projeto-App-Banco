# 🧪 Guia de Teste - Aplicação Bancária

Este guia mostra como testar todas as funcionalidades da aplicação após as melhorias implementadas.

## 📋 Índice

- [Executar com Docker](#executar-com-docker-recomendado)
- [Executar Localmente](#executar-localmente)
- [Testar Funcionalidades](#testar-funcionalidades)
- [Testar API com Swagger](#testar-api-com-swagger)
- [Verificar Melhorias](#verificar-melhorias)

---

## 🐳 Executar com Docker (Recomendado)

### 1. Navegar até a pasta Docker

```bash
cd Docker
```

### 2. Subir os containers

```bash
# Primeiro, suba apenas o banco de dados
docker compose up db

# Aguarde 10-15 segundos para o banco inicializar
# Pressione Ctrl+C para parar

# Agora suba todos os serviços
docker compose up
```

### 3. Aguardar inicialização

Aguarde alguns minutos na primeira execução enquanto:
- O banco de dados inicializa
- O backend compila e inicia
- O frontend compila e inicia

Você verá mensagens como:
- `Started AppBancoApplication` (Backend pronto)
- `Compiled successfully` (Frontend pronto)

### 4. Acessar a aplicação

- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Swagger**: http://localhost:8080/api-docs

---

## 💻 Executar Localmente

### Pré-requisitos

- Java 21
- Maven 3.8+
- Node.js 22+
- Angular CLI: `npm install -g @angular/cli`
- PostgreSQL 14 (ou usar Docker apenas para o banco)

### 1. Banco de Dados

```bash
cd Docker
docker compose up db
```

### 2. Backend

```bash
cd AppBanco
./mvnw spring-boot:run
```

Ou no Windows:
```bash
.\mvnw.cmd spring-boot:run
```

### 3. Frontend

Em outro terminal:

```bash
cd frontend
npm install
ng serve
```

---

## 🧪 Testar Funcionalidades

### 1. Testar Cadastro de Usuário

1. Acesse http://localhost:4200
2. Preencha:
   - **E-mail**: teste@email.com
   - **Senha**: 123456 (mínimo 6 caracteres)
3. Clique em **"Criar Conta"**
4. ✅ Deve redirecionar para a página de contas
5. ✅ Deve mostrar mensagem de sucesso

**Teste de Validação:**
- Tente cadastrar com senha menor que 6 caracteres → Deve mostrar erro
- Tente cadastrar com e-mail já existente → Deve mostrar erro

### 2. Testar Login

1. Na tela inicial, preencha:
   - **E-mail**: teste@email.com
   - **Senha**: 123456
2. Clique em **"Entrar"**
3. ✅ Deve redirecionar para a página de contas
4. ✅ Token JWT deve ser salvo no localStorage

**Teste de Erro:**
- Tente fazer login com senha errada → Deve mostrar "E-mail ou senha incorretos"

### 3. Testar Criação de Conta Bancária

1. Após fazer login, se não tiver contas:
   - Preencha o **Nome do Titular**
   - Clique em **"Criar Conta"**
2. ✅ Deve criar a conta e selecioná-la automaticamente
3. ✅ Deve mostrar o saldo (R$ 0,00)

**Teste de Validação:**
- Tente criar conta sem nome → Deve mostrar erro

### 4. Testar Depósito

1. Selecione uma conta
2. No campo de valor, digite: `100.50`
3. Clique em **"💰 Depositar"**
4. ✅ Deve mostrar loading durante a operação
5. ✅ Deve atualizar o saldo
6. ✅ Deve mostrar mensagem de sucesso
7. ✅ O valor deve aparecer no extrato

**Teste de Validação:**
- Tente depositar valor negativo → Botão deve estar desabilitado
- Tente depositar 0 → Botão deve estar desabilitado

### 5. Testar Saque

1. Com saldo disponível, digite um valor menor que o saldo
2. Clique em **"💸 Sacar"**
3. ✅ Deve mostrar loading
4. ✅ Deve atualizar o saldo
5. ✅ Deve mostrar mensagem de sucesso

**Teste de Erro:**
- Tente sacar mais que o saldo disponível → Deve mostrar "Saldo insuficiente"

### 6. Testar Extrato

1. Selecione uma conta
2. Clique em **"Carregar Extrato"**
3. ✅ Deve mostrar loading
4. ✅ Deve listar todas as transações
5. ✅ Depósitos devem aparecer em verde com "+"
6. ✅ Saques devem aparecer em vermelho com "-"
7. ✅ Deve mostrar data e hora formatadas

### 7. Testar Múltiplas Contas

1. Crie outra conta com nome diferente
2. ✅ Deve aparecer na lista de contas
3. Selecione uma conta → ✅ Deve mostrar seus dados
4. Selecione outra → ✅ Deve trocar os dados

### 8. Testar Logout

1. Clique em **"🚪 Sair"**
2. ✅ Deve limpar localStorage
3. ✅ Deve redirecionar para a tela de login

---

## 🔍 Testar API com Swagger

### 1. Acessar Swagger

Acesse: http://localhost:8080/api-docs

### 2. Testar Endpoints

#### Criar Usuário
1. Expanda `POST /api/usuarios`
2. Clique em "Try it out"
3. Cole o JSON:
```json
{
  "email": "swagger@test.com",
  "senha": "123456"
}
```
4. Execute → ✅ Deve retornar 201 com o usuário criado

#### Login
1. Expanda `POST /api/usuarios/login`
2. Use o mesmo JSON acima
3. Execute → ✅ Deve retornar token JWT

#### Criar Conta (requer autenticação)
1. Copie o token do login
2. Clique em "Authorize" no topo
3. Cole: `Bearer {seu-token}`
4. Expanda `POST /api/contas/{idUsuario}`
5. Use o idUsuario retornado no login
6. Cole o JSON:
```json
{
  "nomeTitular": "Teste Swagger"
}
```
7. Execute → ✅ Deve criar a conta

---

## ✅ Verificar Melhorias Implementadas

### Backend

#### 1. Verificar Hash de Senhas
```bash
# Conecte ao banco PostgreSQL
docker exec -it banco-db psql -U postgres

# Execute:
SELECT email, senha FROM "Usuario" LIMIT 1;
```
✅ A senha deve estar criptografada (não em texto plano)

#### 2. Verificar Logs
Observe os logs do backend no terminal:
```bash
# Deve ver logs como:
INFO  - Criando novo usuário com email: teste@email.com
INFO  - Usuário criado com sucesso. ID: 1
```
✅ Deve mostrar logs informativos

#### 3. Verificar Tratamento de Erros
- Tente criar usuário com e-mail duplicado
- ✅ Deve retornar erro 400 com mensagem clara
- ✅ Resposta deve ter formato padronizado:
```json
{
  "status": 400,
  "message": "E-mail já cadastrado!",
  "timestamp": "2024-..."
}
```

### Frontend

#### 1. Verificar Loading States
- Ao fazer login → ✅ Deve mostrar "Entrando..."
- Ao criar conta → ✅ Deve mostrar "Criando..."
- Ao depositar → ✅ Deve mostrar loading no botão

#### 2. Verificar Mensagens de Erro
- Tente login com credenciais erradas
- ✅ Deve mostrar mensagem vermelha clara
- ✅ Não deve usar `alert()`

#### 3. Verificar Validações
- Tente criar conta sem nome
- ✅ Botão deve estar desabilitado
- ✅ Deve mostrar mensagem de erro

#### 4. Verificar Design
- ✅ Interface deve ter gradientes
- ✅ Animações suaves ao carregar
- ✅ Responsivo (teste redimensionando a janela)

#### 5. Verificar Formatação
- Saldo deve aparecer como: R$ 1.234,56
- ✅ Deve usar formatação brasileira

---

## 🐛 Troubleshooting

### Problema: Backend não inicia

**Solução:**
```bash
# Verifique os logs
docker logs app-banco

# Verifique se o banco está rodando
docker ps
```

### Problema: Frontend não carrega

**Solução:**
```bash
# Verifique os logs
docker logs banco-frontend

# Reconstrua os containers
cd Docker
docker compose down
docker compose up --build
```

### Problema: Erro de conexão com banco

**Solução:**
```bash
# Aguarde mais tempo para o banco inicializar
# Ou reinicie apenas o banco:
docker restart banco-db
```

### Problema: Token inválido

**Solução:**
- Limpe o localStorage do navegador
- Faça login novamente

---

## 📊 Checklist de Testes

- [ ] Cadastro de usuário funciona
- [ ] Login funciona e salva token
- [ ] Criação de conta funciona
- [ ] Depósito funciona e atualiza saldo
- [ ] Saque funciona e valida saldo
- [ ] Extrato carrega e mostra transações
- [ ] Múltiplas contas funcionam
- [ ] Logout limpa dados
- [ ] Validações de formulário funcionam
- [ ] Mensagens de erro são claras
- [ ] Loading states aparecem
- [ ] Design está responsivo
- [ ] Senhas estão criptografadas
- [ ] Logs aparecem no backend
- [ ] Swagger funciona

---

## 🎯 Próximos Passos

Após testar tudo:
1. Verifique se todas as funcionalidades estão funcionando
2. Teste em diferentes navegadores
3. Teste em dispositivos móveis
4. Verifique a performance
5. Teste casos extremos (valores muito altos, etc.)

---

**Boa sorte com os testes! 🚀**

