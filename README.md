# sistema-de-login-java
Projeto desenvolvido em **Java** com gerenciamento de dependências via **Apache Maven**, estruturado no pacote `br.com.senac`. Este repositório contempla a implementação e organização da camada de serviços de autenticação (`AuthService`) na branch `pessoa-06-login`.

---

## 📋 Visão Geral

O sistema tem como objetivo estruturar a lógica de entrada e validação de usuários da aplicação, isolando a regra de negócio de autenticação na camada de serviços (`services`) e mantendo o ponto de entrada principal desacoplado.

### Funcionalidades Principais
* **Ponto de Entrada (`App.java`):** Inicialização da aplicação e fluxo principal de execução.
* **Serviço de Autenticação (`AuthService.java`):** Encapsulamento da lógica de verificação de credenciais e controle de acesso (login).
* **Suíte de Testes (`AppTest.java`):** Estrutura preparada para validação unitária das regras implementadas.

---

## Tecnologias Utilizadas

* **Linguagem:** Java
* **Gerenciador de Build e Dependências:** Apache Maven (`pom.xml` / `.mvn`)
* **Controle de Versão:** Git (Branch de desenvolvimento: `pessoa-06-login`)
* **IDE de Referência:** IntelliJ IDEA

---

## Estrutura de Diretórios

```text
trabalhin_Javinho/
├── .mvn/                                      # Configurações do Maven Wrapper
├── src/
│   ├── main/
│   │   └── java/
│   │       └── br/com/senac/
│   │           ├── services/
│   │           │   └── AuthService.java       # Lógica de negócio para autenticação/login
│   │           └── App.java                   # Classe principal (ponto de entrada)
│   └── test/
│       └── java/
│           └── br/com/senac/
│               └── AppTest.java               # Testes unitários da aplicação
├── .gitignore                                 # Arquivos ignorados pelo Git (target/, .idea/, etc.)
├── pom.xml                                    # Configurações, plugins e dependências do Maven
└── README.md                                  # Documentação do projeto
```[cite: 1]

---

