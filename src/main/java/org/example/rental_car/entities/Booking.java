package org.example.rental_car.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
    private String paymentMethod = "WALLET";
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private String bookingNo;
    private Double bill;

    public void setAppointmentNo() {
        this.bookingNo = String.valueOf(new Random().nextLong()).substring(1,11);
    }
    public double getOverAllPrice() {
        long rentDays = ChronoUnit.DAYS.between(this.getStartDate(), this.getEndDate());
        double price = rentDays*this.car.getBasePrice();
        if(price > this.customer.getWallet() || this.customer.getWallet() < car.getDeposit()){
            throw new IllegalArgumentException("You don't have enough money.");
        }
        return price;
    }
}
