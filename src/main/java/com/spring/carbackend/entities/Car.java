package com.spring.carbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "License_Plate", nullable = false)
    private String licensePlate;

    @Column(name = "Brand", nullable = false)
    private String brand;

    @Column(name = "Model", nullable = false)
    private String model;

    @Column(name = "Color", nullable = false)
    private String color;

    @Column(name = "Number_Of_Seats", nullable = false)
    private Integer numberOfSeats;

    @Column(name = "Production_Years", nullable = false)
    private Integer productionYears;

    @Column(name = "Transmission_Type", nullable = false)
    private String transmissionType;

    @Column(name = "Fuel_Type", nullable = false)
    private String fuelType;

    @Column(name = "Mileage", precision = 18, scale = 2, nullable = false)
    private BigDecimal mileage;

    @Column(name = "Fuel_Consumption", precision = 18, scale = 3)
    private BigDecimal fuelConsumption;

    @Column(name = "Base_Price", precision = 18, scale = 3)
    private BigDecimal basePrice;

    @Column(name = "Deposit", precision = 18, scale = 3)
    private BigDecimal deposit;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "Description")
    private String description;

    @Column(name = "Additional_Functions")
    private String additionalFunctions;

    @Column(name = "Terms_Of_Use")
    private String termsOfUse;

    @Column(name = "Images", columnDefinition = "VARCHAR(600) NOT NULL")
    private String images;

    @Column(name= "Status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private List<Booking> bookings = new ArrayList<>();

    public double getRatingAvgStar() {
        int star = 0;
        int rateNum = 0;
        for (Booking booking : bookings) {
            if (booking.getFeedback() == null || booking.getFeedback().getRatings() == null) {
                star += 0;
            } else {
                star += booking.getFeedback().getRatings().ordinal() + 1;
                rateNum += 1;
            }
        }
        return (double) star / (double)rateNum;
    }
}
