package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// NOTE: For security, real username and password have been removed before uploading to GitHub.

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/vehicle_rental_system"; // Use your database name
    private static final String USER = "your_username_here"; // Replace before running
    private static final String PASSWORD = "your_password_here"; // Replace before running

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
