package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;

public class SignupController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private Label lblError;

    // Initialize the ComboBox with roles
    @FXML
    public void initialize() {
        // Populate ComboBox with "Admin" and "Employee"
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Employee"));
    }

    // Handle the Register button click
    @FXML
    public void handleRegister(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String role = cmbRole.getValue();

        // Basic validation
        if (username.isEmpty() || password.isEmpty() || role == null) {
            lblError.setText("Username, Password, and Role cannot be empty.");
            return;
        }

        // Create a new user with username, password, and role
        User newUser = new User(username, password, role);
        newUser.saveToDatabase();

        showAlert("Sign Up Successful", "You have signed up successfully.");
    }

    // Handle the Login hyperlink click
    @FXML
    public void handleLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show an alert for success or failure
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
