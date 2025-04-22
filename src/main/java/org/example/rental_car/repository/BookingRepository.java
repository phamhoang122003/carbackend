package org.example.rental_car.repository;

import org.example.rental_car.entities.BookingStatus;
import org.example.rental_car.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingNo(String bookingNo);

    Booking findByStatusAndCarId(BookingStatus bookingStatus, long carId);

    List<Booking> findAllByCustomerIdAndStatus(long customerId, BookingStatus bookingStatus);

    List<Booking> findAllByCarId(long carId);

    List<Booking> findAllByCustomerId(long customerId);
}
