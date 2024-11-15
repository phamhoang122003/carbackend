package com.spring.carbackend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CarEditDTO {
    private Integer id;
    private String license;
    private String color;
    private String brand;
    private String model;
    private Integer productionYear;
    private Integer seatNumber;
    private String transmission;
    private String fuel;
    private String mileage;
    private String city;
    private String district;
    private String ward;
    private String homeNumber;
    private String fuelConsumption;
    private String description;
    private List<String> functions;
    private String basePrice;
    private String deposit;
    private List<Integer> terms;
    private String otherTerm;
    private String carStatus;
    private Integer userId;
}
