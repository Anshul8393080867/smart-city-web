package com.city.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    // Default connection parameters for local MySQL instance
    private static final String URL = "jdbc:mysql://localhost:3306/smart_city?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Singh086@";

    static {
        try {
            // Kept for explicit registration; redundant on Java 6+ where ServiceLoader handles it, but makes the dependency obvious to a reader.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Halts class loading early if the connector jar is missing from lib/
            throw new ExceptionInInitializerError("MySQL JDBC Driver not found in classpath: " + e.getMessage());
        }
    }

    // Utility class: private constructor prevents instantiation from outside
    private DBConnection() {
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
