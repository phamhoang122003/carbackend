package org.example.rental_car.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private String comment;
    private double rating;
    private long customerId;
    private long carId;
    private String customerName;
    private String carName;
}
