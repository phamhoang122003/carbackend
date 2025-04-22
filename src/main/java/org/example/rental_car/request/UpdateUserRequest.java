package org.example.rental_car.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private Long nationalId;
    private String name;
    private String phone;
    private String address;
    private String drivingLicense;
}
