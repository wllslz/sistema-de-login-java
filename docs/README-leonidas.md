# Pessoa 5 — Cadastro de Usuários

**Integrante:** Leonidas  
**Branch:** `feature/leonidas`

## 1. Minha responsabilidade

Fiquei responsável por desenvolver o cadastro de usuários do sistema de login em Java, implementando o método `register` na classe `AuthService`.

## 2. O que desenvolvi

Implementei as seguintes validações:

- **Nome de usuário:** deve ter entre 3 e 20 caracteres, permitindo letras, números e underline.
- **E-mail:** verifica se o endereço possui um formato básico válido.
- **Senha:** exige pelo menos 8 caracteres.
- **Confirmação de senha:** verifica se as duas senhas são iguais.
- **Cadastro duplicado:** consulta o repositório para verificar se o nome de usuário ou e-mail já está cadastrado.

Também preparei as chamadas para gerar hash e salt da senha, salvar o usuário no banco e limpar os vetores de senha da memória após a execução.

## 3. Como funciona

O método `register` recebe o nome de usuário, e-mail, senha e confirmação da senha.

Primeiro, verifica se as informações atendem às regras do cadastro. Se alguma estiver incorreta, lança uma exceção com uma mensagem explicando o problema.

Se as informações forem aceitas, o método deverá utilizar o `PasswordService` para proteger a senha e o `UserRepository` para salvar os dados.

## 4. Conceitos de POO utilizados

Utilizei classes, métodos, encapsulamento e injeção de dependências por construtor.

A classe `AuthService` fica responsável pelas regras de cadastro, enquanto as outras classes terão responsabilidades específicas relacionadas à proteção das senhas e ao acesso ao banco.

## 5. Situação atual

A lógica de validação e as chamadas de integração foram implementadas e enviadas ao GitHub.

Ainda é necessário integrar as classes `PasswordService` e `UserRepository`, confirmar a compatibilidade dos métodos e realizar os testes de funcionamento do cadastro.

## 6. Arquivo desenvolvido

`src/main/java/br/com/senac/services/AuthService.java`

O código foi enviado para a branch `feature/leonidas`.