package br.com.senac.services;

import br.com.senac.database.Database;
import br.com.senac.models.User;
import br.com.senac.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AuthServiceTest {
    private Connection connection;
    private UserRepository repository;
    private AuthService service;

    @Before public void setUp() throws Exception {
        connection = Database.conectar("jdbc:sqlite::memory:");
        repository = new UserRepository(connection);
        service = new AuthService(repository, new PasswordService());
    }

    @After public void tearDown() throws Exception {
        if (connection != null) connection.close();
    }

    @Test public void deveRegistrarEAutenticarUsuario() {
        char[] password = "SenhaForte123".toCharArray();
        char[] confirmation = "SenhaForte123".toCharArray();
        service.register("ana", "ana@example.com", password, confirmation);
        assertTrue(Arrays.equals(new char[password.length], password));
        assertTrue(Arrays.equals(new char[confirmation.length], confirmation));

        User user = service.login("ana", "SenhaForte123".toCharArray());
        assertEquals("ana", user.username());
        assertEquals("ana@example.com", user.email());
    }

    @Test public void devePermitirLoginPorEmail() {
        service.register("bruno", "bruno@example.com", "SenhaForte123".toCharArray(), "SenhaForte123".toCharArray());
        assertEquals("bruno", service.login("bruno@example.com", "SenhaForte123".toCharArray()).username());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarSenhaIncorretaEApagaArray() {
        service.register("carla", "carla@example.com", "SenhaForte123".toCharArray(), "SenhaForte123".toCharArray());
        char[] wrong = "SenhaErrada123".toCharArray();
        try { service.login("carla", wrong); }
        finally { for (char c : wrong) assertEquals('\0', c); }
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarLoginComTentativaDeSqlInjection() {
        service.login("' OR '1'='1' --", "SenhaForte123".toCharArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarCadastroComSenhasDiferentes() {
        service.register("diana", "diana@example.com", "SenhaForte123".toCharArray(), "OutraSenha123".toCharArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarUsuarioDuplicado() {
        service.register("edu", "edu@example.com", "SenhaForte123".toCharArray(), "SenhaForte123".toCharArray());
        service.register("edu", "edu2@example.com", "SenhaForte123".toCharArray(), "SenhaForte123".toCharArray());
    }
}
