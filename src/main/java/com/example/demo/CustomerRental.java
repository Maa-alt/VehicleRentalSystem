package com.example.demo;

public class CustomerRental {
    private String customerName;
    private String vehicleModel;
    private String rentalStart;
    private String rentalEnd;
    private String totalAmount;

    public CustomerRental(String customerName, String vehicleModel, String rentalStart, String rentalEnd, String totalAmount) {
        this.customerName = customerName;
        this.vehicleModel = vehicleModel;
        this.rentalStart = rentalStart;
        this.rentalEnd = rentalEnd;
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getCustomerName() { return customerName; }
    public String getVehicleModel() { return vehicleModel; }
    public String getRentalStart() { return rentalStart; }
    public String getRentalEnd() { return rentalEnd; }
    public String getTotalAmount() { return totalAmount; }
}
