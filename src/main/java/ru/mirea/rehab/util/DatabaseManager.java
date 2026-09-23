package ru.mirea.rehab.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {
    private DatabaseManager() {}
    public static Connection open() throws SQLException {
        String url = System.getenv().getOrDefault("REHAB_DB_URL", "jdbc:postgresql://localhost:5432/rehabilitation_center");
        String user = System.getenv().getOrDefault("REHAB_DB_USER", "postgres");
        String password = System.getenv().getOrDefault("REHAB_DB_PASSWORD", "postgres");
        return DriverManager.getConnection(url, user, password);
    }
}
