package br.com.senac.repositories;

import br.com.senac.models.StoredAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/** Acesso JDBC à tabela users. Os valores recebidos são sempre parametrizados. */
public class UserRepository {

    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "Connection não pode ser nula.");
    }

    public Optional<StoredAccount> findByUsernameOrEmail(String loginInput) {
        if (loginInput == null || loginInput.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT id, username, email, role, password_hash, salt " +
                     "FROM users WHERE username = ? OR email = ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String login = loginInput.trim();
            statement.setString(1, login);
            statement.setString(2, login);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return Optional.empty();
                }
                StoredAccount account = new StoredAccount(
                        result.getInt("id"),
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("role"),
                        result.getString("password_hash"),
                        result.getString("salt")
                );
                return Optional.of(account);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao consultar usuário.", e);
        }
    }

    public void save(String username, String email, String passwordHash, String salt) {
        String sql = "INSERT INTO users (username, email, password_hash, salt) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.setString(4, salt);
            statement.executeUpdate();
        } catch (SQLException e) {
            // UNIQUE/NOT NULL e demais restrições do banco continuam sendo aplicadas.
            throw new IllegalStateException("Erro ao salvar usuário. Verifique os dados e possíveis duplicidades.", e);
        }
    }
}
