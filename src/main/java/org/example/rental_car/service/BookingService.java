package org.example.rental_car.service;

import org.example.rental_car.dto.BookingDto;
import org.example.rental_car.entities.Booking;
import org.example.rental_car.request.BookingRequest;
import org.example.rental_car.request.BookingUpdateRequest;

import java.util.List;

public interface BookingService {
    Booking bookCar(BookingRequest bookingRequest, long carId, long customerId);
    List<Booking> getAllBookings();
    Booking getBookingById(long bookingId);
    BookingDto getBookingDtoById(long bookingId);
    Booking updateBooking(BookingUpdateRequest bookingUpdateRequest, long bookingId);
    void deleteBooking(long bookingId);
    List<BookingDto> getBookingByCustomerId(long customerId);
    List<BookingDto> getBookingByCarId(long carId);
    Booking approveBooking(long bookingId);
    Booking declineBooking(long bookingId);
    Booking completeBooking(long bookingId);
    Booking cancelBooking(long bookingId);
    List<BookingDto> getBookingByOwnerId(long ownerId);
}
