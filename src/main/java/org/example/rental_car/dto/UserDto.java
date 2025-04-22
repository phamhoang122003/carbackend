package org.example.rental_car.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto {
    private long id;
    private Long nationalId;
    private String name;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String address;
    private String drivingLicense;
    private double wallet;
    private String role;
    private boolean isEnabled;
    List<ReviewDto> reviewDtos;
    List<BookingDto> bookingDtos;
    List<CarDto> carDtos;
}
