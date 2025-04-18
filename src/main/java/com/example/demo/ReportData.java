package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReportData {
    private final StringProperty vehicleID;
    private final StringProperty model;
    private final StringProperty category;
    private final StringProperty status;

    public ReportData(String vehicleID, String model, String category, String status) {
        this.vehicleID = new SimpleStringProperty(vehicleID);
        this.model = new SimpleStringProperty(model);
        this.category = new SimpleStringProperty(category);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty vehicleIDProperty() {
        return vehicleID;
    }

    public StringProperty modelProperty() {
        return model;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getVehicleID() {
        return vehicleID.get();
    }

    public String getModel() {
        return model.get();
    }

    public String getCategory() {
        return category.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String toCSV() {
        return String.join(",", getVehicleID(), getModel(), getCategory(), getStatus());
    }
}