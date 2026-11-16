# Pessoa 3 — Repositório JDBC

## Arquivos implementados

- `UserRepository.java`: responsável pelas operações de banco de dados relacionadas aos usuários.
- `StoredAccount.java`: representa os dados de uma conta recuperada do banco, incluindo hash da senha e salt.

## Métodos implementados

**save(String username, String email, String passwordHash, String salt, String role)**

Salva um usuário na tabela `users` utilizando `PreparedStatement`.

**findByUsernameOrEmail(String login)**

Busca um usuário pelo nome de usuário ou e-mail. Retorna `Optional<StoredAccount>`, permitindo identificar quando a conta não existe.

**existsByUsernameOrEmail(String username, String email)**

Verifica se já existe um usuário com o nome ou e-mail informado. Retorna `true` quando encontra uma correspondência e `false` quando não encontra.

## Estrutura esperada do banco

O repositório foi implementado considerando as seguintes colunas:

- `id`
- `username`
- `email`
- `password_hash`
- `salt`
- `role`

A tabela deverá possuir as restrições adequadas de unicidade para username e email.

## Integração com o projeto

O repositório utiliza `Database.conectar()` para abrir as conexões JDBC.

Para funcionar, a classe `Database` precisa estar disponível em um pacote acessível pelo `UserRepository`, com o import correspondente.

O `AuthService` poderá utilizar o método `findByUsernameOrEmail()` para localizar contas durante a autenticação.

O tipo `StoredAccount` possui os métodos `id()`, `username()`, `email()`, `passwordHash()`, `salt()` e `role()`.

## Segurança

Todas as consultas utilizam `PreparedStatement` com parâmetros `?`, evitando a concatenação direta de valores nos comandos SQL.

O repositório recebe o hash e o salt já preparados. A geração e a verificação das senhas ficam sob responsabilidade do serviço de senhas.

## Pendências

- Integrar a versão atualizada do `Database.java`.
- Confirmar a estrutura definitiva da tabela `users`.
- Integrar o repositório ao `AuthService`.
- Compilar e executar os testes de cadastro, busca e duplicidade.

**Status:** implementação inicial concluída; integração e testes pendentes.