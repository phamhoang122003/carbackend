package org.example.rental_car.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private double rating;
    private String comment;
}
