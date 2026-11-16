package br.com.senac.models;

public record User(
        int id,
        String username,
        String email,
        String role
) {
    public User {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username não pode ser vazio.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser vazio.");
        }
        if (role == null || role.isBlank()) {
            role = "USER";
        }
    }
}
