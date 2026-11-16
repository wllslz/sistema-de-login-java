# Arquitetura e Fluxos do Sistema

## 1. Esquema do Banco de Dados (smartweb.db)

A tabela `users` é inicializada automaticamente pela classe `Database`:

| Coluna | Tipo | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | Identificador único do usuário |
| `username` | `TEXT` | `NOT NULL UNIQUE` | Nome de usuário único |
| `email` | `TEXT` | `NOT NULL UNIQUE` | E-mail único normalizado |
| `role` | `TEXT` | `NOT NULL DEFAULT 'USER'` | Perfil de acesso (`USER`, `ADMIN`) |
| `password_hash` | `TEXT` | `NOT NULL` | Hash Base64 gerado via PBKDF2 |
| `salt` | `TEXT` | `NOT NULL` | Salt Base64 exclusivo da conta |

## 2. Fluxo de Cadastro (register)

1. `ConsoleUI` coleta `username`, `email` e `password` e valida os formatos em `InputValidator`.
2. `AuthService.register` verifica se `username` ou `email` já existem via `UserRepository`.
3. `PasswordService` gera um `salt` aleatório e calcula o `passwordHash` com PBKDF2WithHmacSHA256.
4. `UserRepository.save` persiste o registro e retorna o objeto `User` (sem expor `passwordHash` nem `salt`).

## 3. Fluxo de Autenticação (login)

1. `ConsoleUI` valida o identificador (username ou e-mail) e a senha não vazia.
2. `AuthService.login` busca a conta completa (`StoredAccount`) em `UserRepository`.
3. `PasswordService.verifyPassword` recalcula o hash com o `salt` armazenado e compara em tempo constante.
4. Em caso de sucesso, retorna `User` para a sessão ativa na `ConsoleUI`.

## 4. Organização dos Módulos por Branch

- `pessoa-01-modelo`: Modelos imutáveis (`User` e `StoredAccount`)
- `pessoa-02-database`: Conexão SQLite e DDL (`Database`)
- `pessoa-03-repositorio`: Persistência JDBC (`UserRepository`)
- `pessoa-04-senhas`: Criptografia e verificação (`PasswordService`)
- `pessoa-05-cadastro`: Fluxo de registro (`AuthService.register`)
- `pessoa-06-login`: Fluxo de autenticação (`AuthService.login`)
- `pessoa-07-interface`: Interface de linha de comando (`ConsoleUI`)
- `pessoa-08-validacoes`: Validador de entradas (`InputValidator`)
- `pessoa-09-testes-seguranca`: Testes unitários e de integração
- `pessoa-10-documentacao`: Documentação técnica (`README.md` e `docs/ARQUITETURA.md`)
- `pessoa-11-integracao`: Unificação completa e ponto de entrada (`App.java`)
