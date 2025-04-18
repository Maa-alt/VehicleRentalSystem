package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportsController {

    @FXML private BarChart<String, Number> barChartStats;
    @FXML private LineChart<String, Number> lineChartStats;
    @FXML private Button btnExportCSV;
    @FXML private Button btnExportPDF;
    @FXML private Button btnGenerateReport;
    @FXML private ComboBox<String> cmbReportType;
    @FXML private PieChart pieChartsStats;
    @FXML private AnchorPane reportsAnchorPane;
    @FXML private TableView<ReportData> tableReport;

    private final String CONNECTION_URL = "jdbc:mysql://localhost:3306/vehicle_rental_system";
    private final String USER = "root";
    private final String PASSWORD = "123456";

    @FXML
    public void initialize() {
        cmbReportType.setItems(FXCollections.observableArrayList("Available Vehicles"));
    }

    @FXML
    void handleExportCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        var file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                for (ReportData data : tableReport.getItems()) {
                    writer.write(data.toCSV() + "\n");
                }
                showAlert("Success", "CSV file exported successfully!", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Error", "Failed to export CSV: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleExportPDF(ActionEvent event) {
        showAlert("Coming Soon", "PDF export feature is under development.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void handleGenerate(ActionEvent event) {
        String selectedReport = cmbReportType.getValue();
        if (selectedReport == null) {
            showAlert("Error", "Please select a report type.", Alert.AlertType.ERROR);
            return;
        }

        tableReport.getColumns().clear();
        tableReport.getItems().clear();
        pieChartsStats.getData().clear();
        barChartStats.getData().clear();
        lineChartStats.getData().clear();

        if (selectedReport.equals("Available Vehicles")) {
            populateAvailableVehiclesReport();
        }
    }

    private void populateAvailableVehiclesReport() {
        ObservableList<ReportData> data = FXCollections.observableArrayList();
        // Updated query to match the correct column names
        String query = "SELECT vehicle_id, model, category FROM vehicles WHERE availability_status = 1"; // Assuming 1 means available

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                data.add(new ReportData(
                        rs.getString("vehicle_id"),
                        rs.getString("model"),
                        rs.getString("category"),
                        "Available" // Hardcoded status as "Available"
                ));
            }

        } catch (SQLException e) {
            showAlert("Database Error", "Unable to fetch data: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        TableColumn<ReportData, String> colId = new TableColumn<>("Vehicle ID");
        colId.setCellValueFactory(cell -> cell.getValue().vehicleIDProperty());

        TableColumn<ReportData, String> colModel = new TableColumn<>("Model");
        colModel.setCellValueFactory(cell -> cell.getValue().modelProperty());

        TableColumn<ReportData, String> colCategory = new TableColumn<>("Category");
        colCategory.setCellValueFactory(cell -> cell.getValue().categoryProperty());

        TableColumn<ReportData, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());

        tableReport.getColumns().addAll(colId, colModel, colCategory, colStatus);
        tableReport.setItems(data);

        Map<String, Long> typeCount = data.stream()
                .collect(Collectors.groupingBy(ReportData::getCategory, Collectors.counting()));

        typeCount.forEach((type, count) -> {
            pieChartsStats.getData().add(new PieChart.Data(type, count));
            barChartStats.getData().add(new XYChart.Series<>(FXCollections.observableArrayList(
                    new XYChart.Data<>(type, count)
            )));
            lineChartStats.getData().add(new XYChart.Series<>(FXCollections.observableArrayList(
                    new XYChart.Data<>(type, count)
            )));
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}