package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;

public class BookingsController {

    @FXML private TableView<Booking> tableBookings;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> customerNameColumn;
    @FXML private TableColumn<Booking, String> vehicleModelColumn;
    @FXML private TableColumn<Booking, LocalDate> startDateColumn;
    @FXML private TableColumn<Booking, LocalDate> endDateColumn;

    @FXML private TextField txtBookingId;
    @FXML private TextField txtCustomerName;
    @FXML private TextField txtVehicleModel;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

    @FXML private Button btnBook;
    @FXML private Button btnModify;
    @FXML private Button btnCancel;

    private final ObservableList<Booking> bookingsList = FXCollections.observableArrayList();
    private Booking selectedBooking = null;

    // MySQL setup
    private final String CONNECTION_URL = "jdbc:mysql://localhost:3306/vehicle_rental";  // Update with your database URL
    private final String USER = "root";  // my MySQL username
    private final String PASSWORD = "123456";  // my MySQL password

    public void initialize() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleModel"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        tableBookings.setItems(bookingsList);
        tableBookings.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedBooking = newSel;
        });

        loadBookingsFromDatabase();
    }

    private void loadBookingsFromDatabase() {
        bookingsList.clear();
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM bookings")) {

            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getString("bookingId"),
                        resultSet.getString("customerName"),
                        resultSet.getString("vehicleModel"),
                        resultSet.getDate("startDate").toLocalDate(),
                        resultSet.getDate("endDate").toLocalDate()
                );
                bookingsList.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBooking(ActionEvent event) {
        String id = txtBookingId.getText().trim();
        String name = txtCustomerName.getText().trim();
        String model = txtVehicleModel.getText().trim();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if (id.isEmpty() || name.isEmpty() || model.isEmpty() || start == null || end == null) {
            showAlert("Please fill all fields.");
            return;
        }

        Booking booking = new Booking(id, name, model, start, end);
        bookingsList.add(booking);
        insertBookingToDatabase(booking);
        clearInputs();
        showAlert("Booking successfully added!");
    }

    private void insertBookingToDatabase(Booking booking) {
        String sql = "INSERT INTO bookings (bookingId, customerName, vehicleModel, startDate, endDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, booking.getBookingId());
            preparedStatement.setString(2, booking.getCustomerName());
            preparedStatement.setString(3, booking.getVehicleModel());
            preparedStatement.setDate(4, Date.valueOf(booking.getStartDate()));
            preparedStatement.setDate(5, Date.valueOf(booking.getEndDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleModification(ActionEvent event) {
        if (selectedBooking == null) {
            showAlert("Please select a booking to modify.");
            return;
        }

        txtBookingId.setText(selectedBooking.getBookingId());
        txtCustomerName.setText(selectedBooking.getCustomerName());
        txtVehicleModel.setText(selectedBooking.getVehicleModel());
        startDate.setValue(selectedBooking.getStartDate());
        endDate.setValue(selectedBooking.getEndDate());

        bookingsList.remove(selectedBooking);
        deleteBookingFromDatabase(selectedBooking.getBookingId());

        selectedBooking = null;
        showAlert("Booking successfully modified!");
    }

    @FXML
    void handleCancel(ActionEvent event) {
        if (selectedBooking != null) {
            bookingsList.remove(selectedBooking);
            deleteBookingFromDatabase(selectedBooking.getBookingId());
            selectedBooking = null;
            showAlert("Booking successfully canceled!");
        } else {
            showAlert("Please select a booking to cancel.");
        }
    }

    private void deleteBookingFromDatabase(String bookingId) {
        String sql = "DELETE FROM bookings WHERE bookingId = ?";
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, bookingId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearInputs() {
        txtBookingId.clear();
        txtCustomerName.clear();
        txtVehicleModel.clear();
        startDate.setValue(null);
        endDate.setValue(null);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}