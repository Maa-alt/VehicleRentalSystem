package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;
import java.sql.*;

public class PaymentsControllers {

    @FXML
    private TableColumn<PaymentRecord, String> amountPaidColumn, booking_idColumn, dateColumn, paymentMethodColumn;

    @FXML
    private Button btnGenerate, btnPay, btnPrint;

    @FXML
    private AnchorPane paymentBillingAnchorPane;

    @FXML
    private ToggleGroup paymentToggleGroup;

    @FXML
    private RadioButton radioCard, radioCash, radioOnline;

    @FXML
    private TableView<PaymentRecord> tableInfo;

    @FXML
    private TextField txtAdditionalServices, txtBooking_id, txtCustomerName, txtLateFees, txtRentalDuration, txtRentalFee, txtTotalAmount, txtvehicleModel;

    private final ObservableList<PaymentRecord> paymentList = FXCollections.observableArrayList();

    private final String CONNECTION_URL = "jdbc:mysql://localhost:3306/vehicle_rental_system";
    private final String USER = "root";
    private final String PASSWORD = "123456";

    @FXML
    public void initialize() {
        booking_idColumn.setCellValueFactory(cellData -> cellData.getValue().bookingIDProperty());
        amountPaidColumn.setCellValueFactory(cellData -> cellData.getValue().amountPaidProperty());
        paymentMethodColumn.setCellValueFactory(cellData -> cellData.getValue().paymentMethodProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tableInfo.setItems(paymentList);
        loadPayments();
        addAutoCalculationListeners();
    }

    private void loadPayments() {
        String query = "SELECT booking_id, amount, payment_method, payment_date FROM payments";
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                PaymentRecord record = new PaymentRecord(
                        rs.getString("booking_id"),
                        rs.getString("amount"),
                        rs.getString("payment_method"),
                        rs.getString("payment_date")
                );
                paymentList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load payments from database: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addAutoCalculationListeners() {
        txtRentalFee.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalAmount());
        txtAdditionalServices.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalAmount());
        txtLateFees.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalAmount());
    }

    private void calculateTotalAmount() {
        try {
            double rentalFee = parseDouble(txtRentalFee.getText());
            double additionalServices = parseDouble(txtAdditionalServices.getText());
            double lateFees = parseDouble(txtLateFees.getText());
            double totalAmount = rentalFee + additionalServices + lateFees;
            txtTotalAmount.setText(String.format("%.2f", totalAmount));
        } catch (NumberFormatException e) {
            txtTotalAmount.setText("Invalid input!");
        }
    }

    private double parseDouble(String text) {
        if (text == null || text.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @FXML
    void handleGenerateInvoice(ActionEvent event) {
        if (txtTotalAmount.getText().trim().isEmpty() || txtTotalAmount.getText().equals("Invalid input!")) {
            showAlert("Error", "Total amount is invalid! Please check the values.", Alert.AlertType.ERROR);
            return;
        }

        String invoice = "----- INVOICE -----\n" +
                "Booking ID: " + txtBooking_id.getText() + "\n" +
                "Customer: " + txtCustomerName.getText() + "\n" +
                "Vehicle Model: " + txtvehicleModel.getText() + "\n" +
                "Rental Duration: " + txtRentalDuration.getText() + " days\n" +
                "Rental Fee: M" + txtRentalFee.getText() + "\n" +
                "Additional Services: M" + txtAdditionalServices.getText() + "\n" +
                "Late Fees: M" + txtLateFees.getText() + "\n" +
                "Total Amount: M" + txtTotalAmount.getText() + "\n" +
                "----------------------";

        showAlert("Invoice Generated", invoice, Alert.AlertType.INFORMATION);
    }

    @FXML
    void handlePayment(ActionEvent event) {
        if (txtTotalAmount.getText().trim().isEmpty() || txtTotalAmount.getText().equals("Invalid input!")) {
            showAlert("Error", "Total amount is invalid! Please check the values.", Alert.AlertType.ERROR);
            return;
        }

        int bookingId = Integer.parseInt(txtBooking_id.getText());
        if (!isBookingValid(bookingId)) {
            showAlert("Error", "Booking ID does not exist. Please check the booking ID.", Alert.AlertType.ERROR);
            return;
        }

        String paymentMethod = getSelectedPaymentMethod();
        if (paymentMethod.equals("Unknown")) {
            showAlert("Error", "Please select a payment method.", Alert.AlertType.ERROR);
            return;
        }

        savePaymentToDatabase();
        showAlert("Payment Successful", "Payment of M" + txtTotalAmount.getText() + " made via " + paymentMethod, Alert.AlertType.INFORMATION);
    }

    @FXML
    void handlePrintInvoice(ActionEvent event) {
        if (txtTotalAmount.getText().trim().isEmpty() || txtTotalAmount.getText().equals("Invalid input!")) {
            showAlert("Error", "Please generate a valid invoice first before printing.", Alert.AlertType.ERROR);
            return;
        }

        String invoice = "----- INVOICE -----\n" +
                "Booking ID: " + txtBooking_id.getText() + "\n" +
                "Customer: " + txtCustomerName.getText() + "\n" +
                "Vehicle Model: " + txtvehicleModel.getText() + "\n" +
                "Rental Duration: " + txtRentalDuration.getText() + " days\n" +
                "Rental Fee: M" + txtRentalFee.getText() + "\n" +
                "Additional Services: M" + txtAdditionalServices.getText() + "\n" +
                "Late Fees: M" + txtLateFees.getText() + "\n" +
                "Total Amount: M" + txtTotalAmount.getText() + "\n" +
                "----------------------";

        showAlert("Invoice Printed", invoice, Alert.AlertType.INFORMATION);

        PaymentRecord paymentRecord = new PaymentRecord(
                txtBooking_id.getText(),
                txtTotalAmount.getText(),
                getSelectedPaymentMethod(),
                java.time.LocalDate.now().toString()
        );

        paymentList.add(paymentRecord);
        savePaymentToDatabase();
    }

    private boolean isBookingValid(int bookingId) {
        String query = "SELECT COUNT(*) FROM bookings WHERE booking_id = ?";
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void savePaymentToDatabase() {
        String query = "INSERT INTO payments (booking_id, payment_date, amount, payment_method) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Integer.parseInt(txtBooking_id.getText()));
            preparedStatement.setDate(2, Date.valueOf(java.time.LocalDate.now()));
            preparedStatement.setBigDecimal(3, new BigDecimal(txtTotalAmount.getText()));
            preparedStatement.setString(4, getSelectedPaymentMethod());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save payment to the database.", Alert.AlertType.ERROR);
        }
    }

    private String getSelectedPaymentMethod() {
        if (radioCard.isSelected()) return "Card";
        if (radioCash.isSelected()) return "Cash";
        if (radioOnline.isSelected()) return "Online";
        return "Unknown";
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
