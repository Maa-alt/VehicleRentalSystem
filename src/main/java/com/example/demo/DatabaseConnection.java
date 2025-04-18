package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// NOTE: For security, real username and password have been removed before uploading to GitHub.

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/vehicle_rental_system"; // Use your database name
    private static final String USER = "root"; // 
    private static final String PASSWORD = "123456"; 

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
