public class AuthService {

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

}
