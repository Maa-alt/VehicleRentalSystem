package com.example.demo;

import javafx.beans.property.*;

public class Vehicle {
    private final IntegerProperty id;
    private final StringProperty brand;
    private final StringProperty model;
    private final StringProperty category;
    private final DoubleProperty rentalPrice;

    public Vehicle(int id, String brand, String model, String category, double rentalPrice) {
        this.id = new SimpleIntegerProperty(id);
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.category = new SimpleStringProperty(category);
        this.rentalPrice = new SimpleDoubleProperty(rentalPrice);
    }

    // Property getters
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public StringProperty modelProperty() {
        return model;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public DoubleProperty rentalPriceProperty() {
        return rentalPrice;
    }

    // Regular getters
    public int getId() {
        return id.get();
    }

    public String getBrand() {
        return brand.get();
    }

    public String getModel() {
        return model.get();
    }

    public String getCategory() {
        return category.get();
    }

    public double getRentalPrice() {
        return rentalPrice.get();
    }
}
