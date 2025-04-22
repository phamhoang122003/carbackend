package org.example.rental_car.request;

import lombok.Data;

import java.time.Year;

@Data
public class CarRegisterRequest {
    private String name;
    private String licensePlate;
    private String brand;
    private String model;
    private String color;
    private int seats;
    private Year productionYear;
    private String transmissionType;
    private String fuelType;
    private double mileage;
    private double fuelConsumption;
    private double basePrice;
    private double deposit;
    private String address;
    private String description;
    private String additionalFunction;
    private String termOfUse;
}
