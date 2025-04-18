package com.example.demo;

public class Revenue {
    private String date;
    private String totalRentals;
    private String revenue;

    public Revenue(String date, String totalRentals, String revenue) {
        this.date = date;
        this.totalRentals = totalRentals;
        this.revenue = revenue;
    }

    // Getters
    public String getDate() { return date; }
    public String getTotalRentals() { return totalRentals; }
    public String getRevenue() { return revenue; }
}

