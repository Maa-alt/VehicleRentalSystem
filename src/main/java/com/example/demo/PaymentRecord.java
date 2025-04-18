package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PaymentRecord {
    private final StringProperty booking_id;
    private final StringProperty amountPaid;
    private final StringProperty paymentMethod;
    private final StringProperty date;

    public PaymentRecord(String booking_id, String amountPaid, String paymentMethod, String date) {
        this.booking_id = new SimpleStringProperty(booking_id);
        this.amountPaid = new SimpleStringProperty(amountPaid);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.date = new SimpleStringProperty(date);
    }

    public StringProperty bookingIDProperty() {
        return booking_id;
    }

    public StringProperty amountPaidProperty() {
        return amountPaid;
    }

    public StringProperty paymentMethodProperty() {
        return paymentMethod;
    }

    public StringProperty dateProperty() {
        return date;
    }
}
