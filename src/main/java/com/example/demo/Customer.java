package com.example.demo;

public class Customer {
    private String name;
    private String contact_info;
    private String rental_history;
    private String driving_license_number;

    public Customer(String name, String contact_info, String rental_history, String driving_license_number) {
        this.name = name;
        this.contact_info = contact_info;
        this.rental_history = rental_history;
        this.driving_license_number = driving_license_number;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getContact_info() {
        return contact_info;
    }

    public String getRentalHistory() {
        return rental_history;
    }

    public String getDrivingLicenseNumber() {
        return driving_license_number;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public void setRentalHistory(String rental_history) {
        this.rental_history = rental_history;
    }

    public void setDrivingLicenseNumber(String driving_license_number) {
        this.driving_license_number = driving_license_number;
    }
}
