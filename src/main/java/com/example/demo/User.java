package com.example.demo;

import java.sql.Connection;       // Import Connection class
import java.sql.PreparedStatement; // Import PreparedStatement class
import java.sql.SQLException;     // Import SQLException class

public class User {
    private String username;
    private String password;
    private String role;

    // Constructor with 3 parameters (username, password, role)
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and setters for all fields
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Method to save the user to the database
    public void saveToDatabase() {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();  // Use the DatabaseConnection class to get a connection
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, this.username);
            stmt.setString(2, this.password);
            stmt.setString(3, this.role);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User saved successfully.");
            } else {
                System.out.println("Error saving user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
