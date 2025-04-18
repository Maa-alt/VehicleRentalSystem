package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class VehiclesController {

    @FXML
    private TextField txtBrand, txtModel, txtCategory, txtRentalPrice, txtSearch;

    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private TableColumn<Vehicle, String> brandColumn, modelColumn, categoryColumn;

    @FXML
    private TableColumn<Vehicle, Double> rentalPriceColumn;

    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
    private int selectedVehicleId = -1;

    private Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_rental_system", "root", "123456");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        brandColumn.setCellValueFactory(data -> data.getValue().brandProperty());
        modelColumn.setCellValueFactory(data -> data.getValue().modelProperty());
        categoryColumn.setCellValueFactory(data -> data.getValue().categoryProperty());
        rentalPriceColumn.setCellValueFactory(data -> data.getValue().rentalPriceProperty().asObject());

        // Format rental price with currency symbol "M"
        rentalPriceColumn.setCellFactory(column -> new TableCell<Vehicle, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText("M" + String.format("%.2f", price));
                }
            }
        });

        loadVehicles();
    }

    public void loadVehicles() {
        vehicleList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vehicles");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vehicleList.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("category"),
                        rs.getDouble("rental_price_per_day")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vehicleTable.setItems(vehicleList);
    }

    @FXML
    public void addVehicle() {
        String brand = txtBrand.getText();
        String model = txtModel.getText();
        String category = txtCategory.getText();
        String rentalPriceStr = txtRentalPrice.getText();

        if (brand.isEmpty() || model.isEmpty() || category.isEmpty() || rentalPriceStr.isEmpty()) {
            showAlert("All fields must be filled.");
            return;
        }

        try {
            double rentalPrice = Double.parseDouble(rentalPriceStr);
            Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO vehicles (brand, model, category, rental_price_per_day, availability_status) VALUES (?, ?, ?, ?, 1)");
            stmt.setString(1, brand);
            stmt.setString(2, model);
            stmt.setString(3, category);
            stmt.setDouble(4, rentalPrice);
            stmt.executeUpdate();
            conn.close();
            loadVehicles();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Rental price must be a number.");
        } catch (SQLException e) {
            showAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    public void updateVehicle() {
        if (selectedVehicleId == -1) {
            showAlert("Please select a vehicle from the table to update.");
            return;
        }

        String brand = txtBrand.getText();
        String model = txtModel.getText();
        String category = txtCategory.getText();
        String rentalPriceStr = txtRentalPrice.getText();

        try {
            double rentalPrice = Double.parseDouble(rentalPriceStr);
            Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE vehicles SET brand = ?, model = ?, category = ?, rental_price_per_day = ? WHERE vehicle_id = ?");
            stmt.setString(1, brand);
            stmt.setString(2, model);
            stmt.setString(3, category);
            stmt.setDouble(4, rentalPrice);
            stmt.setInt(5, selectedVehicleId);
            stmt.executeUpdate();
            conn.close();
            loadVehicles();
            clearFields();
            selectedVehicleId = -1;
        } catch (Exception e) {
            showAlert("Error updating vehicle: " + e.getMessage());
        }
    }

    @FXML
    public void deleteVehicle() {
        if (selectedVehicleId == -1) {
            showAlert("Please select a vehicle to delete.");
            return;
        }

        try {
            Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM vehicles WHERE vehicle_id = ?");
            stmt.setInt(1, selectedVehicleId);
            stmt.executeUpdate();
            conn.close();
            loadVehicles();
            clearFields();
            selectedVehicleId = -1;
        } catch (Exception e) {
            showAlert("Error deleting vehicle: " + e.getMessage());
        }
    }

    @FXML
    public void searchVehicle() {
        String searchTerm = txtSearch.getText();
        vehicleList.clear();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vehicles WHERE brand LIKE ?")) {
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicleList.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("category"),
                        rs.getDouble("rental_price_per_day")
                ));
            }
            rs.close();
        } catch (Exception e) {
            showAlert("Error searching: " + e.getMessage());
        }
        vehicleTable.setItems(vehicleList);
    }

    @FXML
    public void handleRowSelect(MouseEvent event) {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedVehicleId = selected.getId();
            txtBrand.setText(selected.getBrand());
            txtModel.setText(selected.getModel());
            txtCategory.setText(selected.getCategory());
            txtRentalPrice.setText(String.valueOf(selected.getRentalPrice()));
        }
    }

    private void clearFields() {
        txtBrand.clear();
        txtModel.clear();
        txtCategory.clear();
        txtRentalPrice.clear();
        txtSearch.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
