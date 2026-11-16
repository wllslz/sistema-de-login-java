package br.com.senac.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository {
    public void save(String username, String email,
                     String passwordHash, String salt, String role)
            throws SQLException {

        String sql = "INSERT INTO users (username, email, password_hash, salt, role)\n" +
                     "VALUES (?, ?, ?, ?, ?)\n";

        try (Connection conn = Database.conectar();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.setString(4, salt);
            statement.setString(5, role);

            statement.executeUpdate();
        }
    }
    public Optional<StoredAccount> findByUsernameOrEmail(String login)
            throws SQLException {

        String sql = "SELECT id, username, email, password_hash, salt, role " +
                     "FROM users WHERE username = ? OR email = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, login);
            statement.setString(2, login);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    StoredAccount account = new StoredAccount(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("salt"),
                            rs.getString("role")
                    );

                    return Optional.of(account);
                }
            }
        }

        return Optional.empty();
    }
    public boolean existsByUsernameOrEmail(String username, String email)
            throws SQLException {

        String sql = "SELECT 1 FROM users WHERE username = ? OR email = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, email);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }
}