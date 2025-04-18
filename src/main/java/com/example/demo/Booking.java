package com.example.demo;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;

public class Booking {
    private final StringProperty bookingId = new SimpleStringProperty();
    private final StringProperty customerName = new SimpleStringProperty();
    private final StringProperty vehicleModel = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();

    public Booking(String bookingId, String customerName, String vehicleModel, LocalDate startDate, LocalDate endDate) {
        this.bookingId.set(bookingId);
        this.customerName.set(customerName);
        this.vehicleModel.set(vehicleModel);
        this.startDate.set(startDate);
        this.endDate.set(endDate);
    }

    public String getBookingId() { return bookingId.get(); }
    public void setBookingId(String value) { bookingId.set(value); }
    public StringProperty bookingIdProperty() { return bookingId; }

    public String getCustomerName() { return customerName.get(); }
    public void setCustomerName(String value) { customerName.set(value); }
    public StringProperty customerNameProperty() { return customerName; }

    public String getVehicleModel() { return vehicleModel.get(); }
    public void setVehicleModel(String value) { vehicleModel.set(value); }
    public StringProperty vehicleModelProperty() { return vehicleModel; }

    public LocalDate getStartDate() { return startDate.get(); }
    public void setStartDate(LocalDate value) { startDate.set(value); }
    public ObjectProperty<LocalDate> startDateProperty() { return startDate; }

    public LocalDate getEndDate() { return endDate.get(); }
    public void setEndDate(LocalDate value) { endDate.set(value); }
    public ObjectProperty<LocalDate> endDateProperty() { return endDate; }
}

