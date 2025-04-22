package org.example.rental_car.request;

import lombok.Data;

@Data
public class BookingRequest {
    private String startDate;
    private String endDate;
    private String paymentMethod;
}
