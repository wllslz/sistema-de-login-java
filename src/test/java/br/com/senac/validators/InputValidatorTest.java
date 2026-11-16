package br.com.senac.validators;

import org.junit.Test;
import static org.junit.Assert.*;

public class InputValidatorTest {
    @Test public void deveRemoverEspacosDoUsername() {
        assertEquals("ana.silva", InputValidator.validateUsername("  ana.silva  "));
    }
    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarUsernameComSqlInjection() {
        InputValidator.validateUsername("' OR '1'='1");
    }
    @Test public void deveNormalizarEmail() {
        assertEquals("ana@example.com", InputValidator.validateEmail(" ANA@EXAMPLE.COM "));
    }
    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarEmailInvalido() {
        InputValidator.validateEmail("ana@@example");
    }
    @Test public void deveAceitarSenhaComRequisitosMinimos() {
        InputValidator.validatePassword("Senha123");
    }
    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarSenhaSemNumero() {
        InputValidator.validatePassword("SenhaForte");
    }
    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarSenhaCurta() {
        InputValidator.validatePassword("Ab1");
    }
    @Test public void deveRemoverEspacosDoIdentificadorDeLogin() {
        assertEquals("ana", InputValidator.validateLoginIdentifier(" ana "));
    }
    @Test(expected = IllegalArgumentException.class)
    public void deveRejeitarIdentificadorVazio() {
        InputValidator.validateLoginIdentifier("   ");
    }
}
