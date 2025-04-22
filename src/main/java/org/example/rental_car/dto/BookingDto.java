package org.example.rental_car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.rental_car.entities.BookingStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingDto {
    private long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerName;
    private long customerId;
    private long carId;
    private String carName;
    private String paymentMethod = "WALLET";
    private BookingStatus status;
    private String bookingNo;
    private Double bill;
}
