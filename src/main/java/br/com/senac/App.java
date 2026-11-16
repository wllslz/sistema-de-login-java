package br.com.senac;

import br.com.senac.database.Database;
import br.com.senac.repositories.UserRepository;
import br.com.senac.services.AuthService;
import br.com.senac.services.PasswordService;
import br.com.senac.ui.ConsoleUI;

import java.sql.Connection;

public class App {

    public static void main(String[] args) {
        try (Connection connection = Database.conectar()) {
            UserRepository userRepository = new UserRepository(connection);
            PasswordService passwordService = new PasswordService();
            AuthService authService = new AuthService(userRepository, passwordService);

            ConsoleUI consoleUI = new ConsoleUI(authService);
            consoleUI.start();
        } catch (Exception e) {
            System.err.println("Erro crítico ao iniciar o sistema: " + e.getMessage());
        }
    }
}
