# Sistema de Login em Java (SENAC)

Aplicação de autenticação e gerenciamento de usuários desenvolvida em Java 17 com persistência em banco de dados SQLite, criptografia de senhas via **PBKDF2WithHmacSHA256** e interface de linha de comando (CLI).

## Arquitetura do Projeto

    src/main/java/br/com/senac/
    ├── App.java                     # Ponto de entrada e injeção de dependências
    ├── database/
    │   └── Database.java            # Conexão SQLite e criação automática da tabela users
    ├── models/
    │   ├── User.java                # Modelo público de usuário autenticado
    │   └── StoredAccount.java       # Modelo interno com hash e salt persistidos
    ├── repositories/
    │   └── UserRepository.java      # Operações CRUD e consultas no SQLite
    ├── services/
    │   ├── AuthService.java         # Regras de negócio de cadastro e login
    │   └── PasswordService.java     # Derivação de hash PBKDF2 e verificação em tempo constante
    ├── ui/
    │   └── ConsoleUI.java           # Menu interativo no terminal (cadastro, login, perfil e logout)
    └── validators/
        └── InputValidator.java      # Validação e normalização de username, e-mail e senha

## Requisitos

- **Java JDK 17** ou superior
- **Apache Maven 3.8+**

## Como Compilar e Executar

    # Compilar o projeto
    mvn clean compile

    # Executar os testes automatizados
    mvn test

    # Executar a aplicação via terminal
    mvn exec:java -Dexec.mainClass="br.com.senac.App"

## Segurança e Validações

- **Criptografia:** PBKDF2WithHmacSHA256 com 210.000 iterações, chave derivada de 256 bits e salt aleatório de 16 bytes por usuário (`SecureRandom`), utilizando comparação em tempo constante (`MessageDigest.isEqual`).
- **Prevenção contra SQL Injection:** Todas as consultas utilizam `PreparedStatement` parametrizado.
- **Regras de Entrada:**
  - **Username:** 3 a 20 caracteres alfanuméricos, `.`, `_` ou `-`.
  - **E-mail:** formato válido normalizado em minúsculas.
  - **Senha:** mínimo de 8 caracteres contendo letra maiúscula, letra minúscula e número.

## Documentação Complementar

Consulte [docs/ARQUITETURA.md](docs/ARQUITETURA.md) para detalhes do esquema do banco de dados, fluxos de autenticação e divisão de módulos da equipe.
