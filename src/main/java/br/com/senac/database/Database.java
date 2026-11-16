package br.com.senac.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DEFAULT_URL = "jdbc:sqlite:smartweb.db";

    public static Connection conectar() throws SQLException {
        return conectar(DEFAULT_URL);
    }

    public static Connection conectar(String url) throws SQLException {
        Connection conn = DriverManager.getConnection(url);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    email TEXT NOT NULL UNIQUE,
                    role TEXT NOT NULL DEFAULT 'USER',
                    password_hash TEXT NOT NULL,
                    salt TEXT NOT NULL
                )
            """);
        }

        return conn;
    }
}
