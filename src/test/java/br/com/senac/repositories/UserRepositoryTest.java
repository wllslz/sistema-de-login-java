package br.com.senac.repositories;

import br.com.senac.database.Database;
import br.com.senac.models.StoredAccount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    private Connection connection;
    private UserRepository repository;

    @Before public void setUp() throws Exception {
        connection = Database.conectar("jdbc:sqlite::memory:");
        repository = new UserRepository(connection);
    }

    @After public void tearDown() throws Exception {
        if (connection != null) connection.close();
    }

    @Test public void deveSalvarEBuscarPorUsername() {
        repository.save("ana", "ana@example.com", "hash123", "salt123");
        Optional<StoredAccount> result = repository.findByUsernameOrEmail("ana");
        assertTrue(result.isPresent());
        assertEquals("ana", result.get().username());
        assertEquals("ana@example.com", result.get().email());
        assertEquals("USER", result.get().role());
    }

    @Test public void deveBuscarPorEmail() {
        repository.save("bruno", "bruno@example.com", "hash", "salt");
        assertTrue(repository.findByUsernameOrEmail("bruno@example.com").isPresent());
    }

    @Test public void deveRetornarVazioParaUsuarioInexistente() {
        assertFalse(repository.findByUsernameOrEmail("nao-existe").isPresent());
    }

    @Test public void entradaComTentativaDeSqlInjectionDeveSerTratadaComoTexto() {
        repository.save("ana", "ana@example.com", "hash", "salt");
        assertFalse(repository.findByUsernameOrEmail("' OR '1'='1' --").isPresent());
        assertTrue(repository.findByUsernameOrEmail("ana").isPresent());
    }

    @Test(expected = IllegalStateException.class)
    public void naoDevePermitirUsernameDuplicado() {
        repository.save("ana", "ana@example.com", "hash1", "salt1");
        repository.save("ana", "outra@example.com", "hash2", "salt2");
    }
}
