package br.com.senac.services;

import br.com.senac.models.User;
import br.com.senac.repositories.UserRepository;
public class AuthService {

    // Dependências utilizadas pelo serviço de autenticação
    private final UserRepository userRepository;
    private final PasswordService passwordService;


    public AuthService(UserRepository userRepository,
                       PasswordService passwordService) {

        this.userRepository = java.util.Objects.requireNonNull(
                userRepository, "UserRepository não pode ser nulo."
        );

        this.passwordService = java.util.Objects.requireNonNull(
                passwordService, "PasswordService não pode ser nulo."
        );
    }


    public User login(String loginInput, char[] password) {

        // 1. Verificar se login e senha foram preenchidos

        if (loginInput == null || loginInput.trim().isEmpty() || password == null ||
                password.length == 0) {
            throw new IllegalArgumentException("Login ou senha inválidos.");
        }
        String normalizedLogin = loginInput.trim();
        try {
// 2. Buscar pelo usuário ou e-mail no UserRepository
            var storedAccount = userRepository.findByUsernameOrEmail(normalizedLogin);
// 3. Rejeitar se a conta não existir (mensagem genérica)
            if (storedAccount.isEmpty()) {
                throw new IllegalArgumentException("Login ou senha inválidos.");
            }
// 4. Verificar o hash usando PasswordService (salt + PBKDF2)
            var account = storedAccount.get();
            boolean validPassword = passwordService.verifyPassword(
                    password,
                    account.passwordHash(),
                    account.salt()
            );
            if (!validPassword) {
                throw new IllegalArgumentException("Login ou senha inválidos.");
            }
// 5. Retornar o objeto User autenticado (sem dados sensíveis)
            return new User(account.id(), account.username(), account.email(),
                    account.role());
        } finally {
// Boas práticas: limpar a senha da memória após a verificação
            java.util.Arrays.fill(password, '\0');
        }
    }



public void register(String username, String email,
                     char[] password, char[] confirmPassword) {
    try {
    // 1. Validar o nome de usuário
    if (username == null ||
            !username.matches("[a-zA-Z0-9_]{3,20}")) {

        throw new IllegalArgumentException(
                "O usuário deve ter de 3 a 20 caracteres, " +
                        "utilizando letras, números ou _."
        );
    }

    // 2. Validar o e-mail
    if (email == null ||
            !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

        throw new IllegalArgumentException(
                "E-mail inválido."
        );
    }

    // 3. Validar o tamanho da senha
    if (password == null || password.length < 8) {
        throw new IllegalArgumentException(
                "A senha deve ter pelo menos 8 caracteres."
        );
    }

    // 4. Verificar se as senhas são iguais
    if (confirmPassword == null ||
            !java.util.Arrays.equals(password, confirmPassword)) {

        throw new IllegalArgumentException(
                "As senhas não coincidem."
        );
    }

    // 5. Impedir usuários ou e-mails duplicados
    if (userRepository.findByUsernameOrEmail(username).isPresent() ||
            userRepository.findByUsernameOrEmail(email).isPresent()) {

        throw new IllegalArgumentException(
                "Nome de usuário ou e-mail já cadastrado."
        );
    }

    // 6. Gerar hash e salt da senha
    var senhaProtegida = passwordService.hashPassword(password);

    // 7. Salvar o usuário no banco de dados
    userRepository.save(
            username,
            email,
            senhaProtegida.hash(),
            senhaProtegida.salt()
    );

    } finally {
        // Limpar as senhas da memória
        if (password != null) {
            java.util.Arrays.fill(password, '\0');
        }

        if (confirmPassword != null) {
            java.util.Arrays.fill(confirmPassword, '\0');
        }
    }

}

}
