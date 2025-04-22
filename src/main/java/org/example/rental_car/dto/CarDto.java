package org.example.rental_car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private long id;
    private String name;
    private String licensePlate;
    private String brand;
    private String model;
    private String color;
    private int seats;
    private String transmissionType;
    private String fuelType;
    private double mileage;
    private double fuelConsumption;
    private String additionalFunction;
    private Year productionYear;
    private double deposit;
    private String description;
    private String address;
    private double basePrice;
    private byte[] image;
    private String ownerEmail;
    private double averageRating;
    private List<ReviewDto> reviews = new ArrayList<>();
    private Long totalCustomers;
    private List<BookingDto> bookings = new ArrayList<>();
}
