package org.example.rental_car.request;

import lombok.Data;

@Data
public class UpdateCarRequest {
    private String name;
    private String licensePlate;
    private String color;
    private int seats;
    private double mileage;
    private double fuelConsumption;
    private double basePrice;
    private double deposit;
    private String address;
    private String description;
    private String additionalFunction;
    private String termOfUse;
}
