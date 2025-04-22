package org.example.rental_car.request;

import lombok.Data;
import org.example.rental_car.entities.BookingStatus;

@Data
public class BookingUpdateRequest {
    private BookingStatus status;
}
