package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> cmbRole;
    @FXML
    private Button btnLogin;
    @FXML
    private Hyperlink linkSignUp;
    @FXML
    private Label lblError;

    // Initialize ComboBox
    @FXML
    public void initialize() {
        cmbRole.getItems().addAll("Admin", "Employee");
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        try {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String role = cmbRole.getValue();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                lblError.setText("Username, Password, and Role are required.");
                return;
            }

            boolean isValidUser = validateUserInDatabase(username, password, role);

            if (isValidUser) {
                redirectToDashboard(role);
            } else {
                lblError.setText("Invalid username or password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("An error occurred during login.");
        }
    }

    private boolean validateUserInDatabase(String username, String password, String role) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void redirectToDashboard(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Main.fxml"));
            AnchorPane dashboard = loader.load();

            // Pass the role to MainController
            MainController controller = loader.getController();
            controller.setRole(role);

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            Scene scene = new Scene(dashboard);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("Failed to load dashboard: " + e.getMessage());
        }
    }

    @FXML
    public void handleSignUp(ActionEvent event) {
        try {
            Stage stage = (Stage) linkSignUp.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/SignUp.fxml"));
            AnchorPane signUpPage = loader.load();
            Scene scene = new Scene(signUpPage);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("Error loading SignUp page.");
        }
    }
}
