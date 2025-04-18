package com.example.demo;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class AvailableVehicle {

    private final StringProperty vehicleID;
    private final StringProperty model;
    private final StringProperty type;
    private final StringProperty status;

    public AvailableVehicle(String vehicleID, String model, String type, String status) {
        this.vehicleID = new SimpleStringProperty(vehicleID);
        this.model = new SimpleStringProperty(model);
        this.type = new SimpleStringProperty(type);
        this.status = new SimpleStringProperty(status);
    }

    public String getVehicleID() {
        return vehicleID.get();
    }

    public StringProperty vehicleIDProperty() {
        return vehicleID;
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }
}
