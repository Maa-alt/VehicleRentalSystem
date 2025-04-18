package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class CustomersController {

    @FXML private TextField txtName, txtContact, txtLicense, txtSearchCustomer;
    @FXML private TextArea txtHistory;
    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer, String> nameColumn, contactColumn, rentalColumn, drivingColumn;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact_info"));
        rentalColumn.setCellValueFactory(new PropertyValueFactory<>("rental_history"));
        drivingColumn.setCellValueFactory(new PropertyValueFactory<>("driving_license_number")); // updated to match actual column

        tableCustomers.setItems(customerList);
        loadCustomers();
    }

    private void loadCustomers() {
        customerList.clear();
        String query = "SELECT * FROM customers";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getString("name"),
                        rs.getString("contact_info"),
                        rs.getString("rental_history"),
                        rs.getString("driving_license_number") // updated column name
                ));
            }

        } catch (SQLException e) {
            showAlert("Error", "Could not load customers from database: " + e.getMessage());
        }
    }

    @FXML
    private void addCustomer() {
        String name = txtName.getText();
        String contact = txtContact.getText();
        String history = txtHistory.getText();
        String license = txtLicense.getText();

        if (name.isEmpty() || contact.isEmpty()) {
            showAlert("Validation Error", "Name and Contact are required.");
            return;
        }

        String query = "INSERT INTO customers (name, contact_info, rental_history, driving_license_number) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, contact);
            stmt.setString(3, history);
            stmt.setString(4, license);
            stmt.executeUpdate();

            Customer newCustomer = new Customer(name, contact, history, license);
            customerList.add(newCustomer); // Add directly to the table

            showAlert("Success", "Customer added successfully.");
            clearFields();

        } catch (SQLException e) {
            showAlert("Error", "Could not add customer: " + e.getMessage());
        }
    }

    @FXML
    private void deleteCustomer() {
        Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a customer to delete.");
            return;
        }

        String query = "DELETE FROM customers WHERE name = ? AND driving_license_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, selected.getName());
            stmt.setString(2, selected.getDrivingLicenseNumber());
            stmt.executeUpdate();

            customerList.remove(selected); // Remove from TableView
            showAlert("Deleted", "Customer deleted.");

        } catch (SQLException e) {
            showAlert("Error", "Could not delete customer: " + e.getMessage());
        }
    }

    @FXML
    private void updateCustomer() {
        Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a customer to update.");
            return;
        }

        String name = txtName.getText();
        String contact = txtContact.getText();
        String history = txtHistory.getText();
        String license = txtLicense.getText();

        String query = "UPDATE customers SET name = ?, contact_info = ?, rental_history = ? WHERE driving_license_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, contact);
            stmt.setString(3, history);
            stmt.setString(4, license);
            stmt.executeUpdate();

            selected.setName(name);
            selected.setContact_info(contact);
            selected.setRentalHistory(history);
            tableCustomers.refresh();

            showAlert("Updated", "Customer updated.");

        } catch (SQLException e) {
            showAlert("Error", "Could not update customer: " + e.getMessage());
        }
    }

    @FXML
    private void searchCustomer() {
        String search = txtSearchCustomer.getText().trim();
        if (search.isEmpty()) {
            tableCustomers.setItems(customerList);
            return;
        }

        ObservableList<Customer> filteredList = FXCollections.observableArrayList();

        for (Customer c : customerList) {
            if (c.getName().toLowerCase().contains(search.toLowerCase()) || c.getDrivingLicenseNumber().contains(search)) {
                filteredList.add(c);
            }
        }

        tableCustomers.setItems(filteredList);
    }

    private void clearFields() {
        txtName.clear();
        txtContact.clear();
        txtLicense.clear();
        txtHistory.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
