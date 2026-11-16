package br.com.senac.services;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordServiceTest {
    private final PasswordService service = new PasswordService();

    @Test public void deveGerarHashESaltNaoVazios() {
        var result = service.hashPassword("SenhaForte123".toCharArray());
        assertNotNull(result.hash());
        assertFalse(result.hash().isEmpty());
        assertNotNull(result.salt());
        assertFalse(result.salt().isEmpty());
    }

    @Test public void mesmaSenhaDeveGerarSaltsEDigestsDiferentes() {
        var first = service.hashPassword("SenhaForte123".toCharArray());
        var second = service.hashPassword("SenhaForte123".toCharArray());
        assertNotEquals(first.salt(), second.salt());
        assertNotEquals(first.hash(), second.hash());
    }

    @Test public void deveValidarSenhaCorreta() {
        var result = service.hashPassword("SenhaForte123".toCharArray());
        assertTrue(service.verifyPassword("SenhaForte123".toCharArray(), result.hash(), result.salt()));
    }

    @Test public void deveRejeitarSenhaIncorreta() {
        var result = service.hashPassword("SenhaForte123".toCharArray());
        assertFalse(service.verifyPassword("SenhaErrada123".toCharArray(), result.hash(), result.salt()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarSaltEmBase64Invalido() {
        service.verifyPassword("SenhaForte123".toCharArray(), "aGFzaA==", "###");
    }
}
