package br.com.senac.validators;

import java.util.regex.Pattern;

public final class InputValidator {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]{3,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private InputValidator() {
    }

    public static String validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("O nome de usuário não pode ser vazio.");
        }
        String trimmed = username.trim();
        if (!USERNAME_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(
                "Nome de usuário inválido: use de 3 a 20 caracteres (letras, números, '.', '_' ou '-')."
            );
        }
        return trimmed;
    }

    public static String validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail não pode ser vazio.");
        }
        String normalized = email.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
        return normalized;
    }

    public static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("A senha não pode ser vazia.");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres.");
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        if (!hasUpper || !hasLower || !hasDigit) {
            throw new IllegalArgumentException(
                "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número."
            );
        }
    }

    public static String validateLoginIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Informe seu nome de usuário ou e-mail.");
        }
        return identifier.trim();
    }
}
