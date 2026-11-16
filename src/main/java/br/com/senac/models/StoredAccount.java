package br.com.senac.models;

public record StoredAccount(
        int id,
        String username,
        String email,
        String role,
        String passwordHash,
        String salt
) {
}
