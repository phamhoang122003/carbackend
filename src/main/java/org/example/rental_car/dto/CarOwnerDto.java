package org.example.rental_car.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CarOwnerDto {
    private Long nationalId;
    private String name;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String address;
    private String role;
    private List<CarDto> carDtos;
}
