# 📝 Comandos para Atualizar no GitHub

Siga estes passos para atualizar seu repositório no GitHub:

## 1. Navegar até o diretório do projeto

Abra o terminal (PowerShell ou Git Bash) e navegue até a pasta do projeto:

```bash
cd "C:\Users\Mallmann\Desktop\Meus Projetos\Projeto-Aplicação-Bancária"
```

## 2. Verificar se já existe um repositório Git

```bash
git status
```

Se não existir, inicialize:

```bash
git init
```

## 3. Adicionar o repositório remoto (se ainda não estiver configurado)

```bash
git remote add origin https://github.com/M4llmann/TesteFull.git
```

Se já existir, atualize a URL:

```bash
git remote set-url origin https://github.com/M4llmann/TesteFull.git
```

Verificar o remote:

```bash
git remote -v
```

## 4. Adicionar todos os arquivos ao staging

```bash
git add .
```

## 5. Fazer commit das alterações

```bash
git commit -m "Atualização: README completo e .gitignore adicionado"
```

## 6. Verificar branch atual

```bash
git branch
```

Se necessário, criar/alterar para a branch main:

```bash
git branch -M main
```

## 7. Fazer push para o GitHub

**Primeira vez (se o repositório estiver vazio):**

```bash
git push -u origin main
```

**Atualizações subsequentes:**

```bash
git push origin main
```

Se houver conflitos ou se o repositório remoto tiver conteúdo diferente:

```bash
git pull origin main --allow-unrelated-histories
```

Depois:

```bash
git push origin main
```

## 8. Verificar no GitHub

Acesse https://github.com/M4llmann/TesteFull e verifique se as alterações foram enviadas corretamente.

---

## ⚠️ Notas Importantes

- Certifique-se de que o `.gitignore` está funcionando corretamente antes de fazer commit
- Não faça commit de arquivos sensíveis (senhas, tokens, etc.)
- A pasta `Docker/dataPostgresql/` está no `.gitignore` para não enviar dados do banco

## 🔍 Verificar o que será commitado

Antes de fazer commit, você pode verificar quais arquivos serão incluídos:

```bash
git status
```

Para ver as diferenças:

```bash
git diff
```

