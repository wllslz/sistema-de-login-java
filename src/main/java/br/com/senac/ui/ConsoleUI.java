package br.com.senac.ui;

import br.com.senac.models.User;
import br.com.senac.services.AuthService;
import br.com.senac.validators.InputValidator;

import java.util.Scanner;

public class ConsoleUI {

    private final AuthService authService;
    private final Scanner scanner;
    private User loggedUser;

    public ConsoleUI(AuthService authService) {
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            if (loggedUser == null) {
                running = showGuestMenu();
            } else {
                showAuthenticatedMenu();
            }
        }

        System.out.println("\nSistema encerrado. Até logo!");
    }

    private boolean showGuestMenu() {
        System.out.println("\n=========================================");
        System.out.println("       SISTEMA DE LOGIN - SENAC");
        System.out.println("=========================================");
        System.out.println("1 - Cadastrar novo usuário");
        System.out.println("2 - Entrar (Login)");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");

        String option = scanner.nextLine().trim();

        switch (option) {
            case "1" -> handleRegister();
            case "2" -> handleLogin();
            case "0" -> {
                return false;
            }
            default -> System.out.println("\n[!] Opção inválida. Tente novamente.");
        }
        return true;
    }

    private void showAuthenticatedMenu() {
        System.out.println("\n-----------------------------------------");
        System.out.println(" Bem-vindo(a), " + loggedUser.username() + " [" + loggedUser.role() + "]");
        System.out.println("-----------------------------------------");
        System.out.println("1 - Exibir dados do perfil");
        System.out.println("2 - Sair da conta (Logout)");
        System.out.print("Escolha uma opção: ");

        String option = scanner.nextLine().trim();

        switch (option) {
            case "1" -> {
                System.out.println("\n--- Dados do Perfil ---");
                System.out.println("ID:       " + loggedUser.id());
                System.out.println("Usuário:  " + loggedUser.username());
                System.out.println("E-mail:   " + loggedUser.email());
                System.out.println("Perfil:   " + loggedUser.role());
            }
            case "2" -> {
                System.out.println("\n[OK] Logout realizado com sucesso.");
                loggedUser = null;
            }
            default -> System.out.println("\n[!] Opção inválida. Tente novamente.");
        }
    }

    private void handleRegister() {
        System.out.println("\n--- Cadastro de Usuário ---");
        try {
            System.out.print("Nome de usuário (3-20 caracteres): ");
            String username = InputValidator.validateUsername(scanner.nextLine());

            System.out.print("E-mail: ");
            String email = InputValidator.validateEmail(scanner.nextLine());

            System.out.print("Senha (mín. 8 caracteres, maiúscula, minúscula e número): ");
            String password = scanner.nextLine();
            InputValidator.validatePassword(password);

            System.out.print("Confirme a senha: ");
            String confirmPassword = scanner.nextLine();

            authService.register(username, email, password.toCharArray(), confirmPassword.toCharArray());
            System.out.println("\n[OK] Usuário '" + username + "' cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("\n[Erro de Validação] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[Erro no Cadastro] " + e.getMessage());
        }
    }

    private void handleLogin() {
        System.out.println("\n--- Login ---");
        try {
            System.out.print("Usuário ou E-mail: ");
            String identifier = InputValidator.validateLoginIdentifier(scanner.nextLine());

            System.out.print("Senha: ");
            String password = scanner.nextLine();
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("A senha não pode ser vazia.");
            }

            User user = authService.login(identifier, password.toCharArray());
            loggedUser = user;
            System.out.println("\n[OK] Autenticado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("\n[Erro de Validação] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[Erro ao Autenticar] " + e.getMessage());
        }
    }
}
