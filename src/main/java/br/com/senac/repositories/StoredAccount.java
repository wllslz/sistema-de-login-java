package br.com.senac.repositories;

public class StoredAccount {

    private final long id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String salt;
    private final String role;

    public StoredAccount(long id, String username, String email,
                         String passwordHash, String salt, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
    }

    public long id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String email() {
        return email;
    }

    public String passwordHash() {
        return passwordHash;
    }

    public String salt() {
        return salt;
    }

    public String role() {
        return role;
    }
}