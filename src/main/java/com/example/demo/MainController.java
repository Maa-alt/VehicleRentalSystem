package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button btnBookings;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnPayments;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnVehicles;

    @FXML
    private Button btnLogout;

    @FXML
    private AnchorPane mainContent;

    // Set role and control visibility
    public void setRole(String role) {
        if ("Admin".equals(role)) {
            btnVehicles.setVisible(true);
            btnCustomers.setVisible(true);
            btnReports.setVisible(true);
            btnPayments.setVisible(true);
            btnLogout.setVisible(true);
            btnBookings.setVisible(false); // Admin does not need bookings
        } else if ("Employee".equals(role)) {
            btnBookings.setVisible(true);
            btnPayments.setVisible(true);
            btnVehicles.setVisible(false);
            btnCustomers.setVisible(false);
            btnReports.setVisible(false);
            btnLogout.setVisible(true);
        }
    }

    // Load a specific FXML page into mainContent
    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/" + fxmlFile));
            AnchorPane pane = loader.load();
            mainContent.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the page: " + fxmlFile, Alert.AlertType.ERROR);
        }
    }

    // Navigation button actions
    @FXML
    void manageBookings(ActionEvent event) {
        loadPage("bookings.fxml");
    }

    @FXML
    void manageCustomers(ActionEvent event) {
        loadPage("customers.fxml");
    }

    @FXML
    void managePayments(ActionEvent event) {
        loadPage("payment.fxml");
    }

    @FXML
    void manageVehicles(ActionEvent event) {
        loadPage("vehicles.fxml");
    }

    @FXML
    void viewReports(ActionEvent event) {
        loadPage("reports.fxml");
    }

    // Handle logout
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/login.fxml"));
            Parent loginRoot = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load login screen.", Alert.AlertType.ERROR);
        }
    }

    // Show alert dialog
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
