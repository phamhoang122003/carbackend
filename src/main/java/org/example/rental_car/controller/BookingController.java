package org.example.rental_car.controller;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.common.Url;
import org.example.rental_car.dto.BookingDto;
import org.example.rental_car.entities.Booking;
import org.example.rental_car.request.BookingRequest;
import org.example.rental_car.request.BookingUpdateRequest;
import org.example.rental_car.response.APIResponse;
import org.example.rental_car.service.BookingService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Url.BOOKING)
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final ApplicationEventPublisher applicationEventPublisher;
    @PostMapping(Url.ADD)
    public ResponseEntity<APIResponse> bookCar(@RequestBody BookingRequest bookingRequest,
                                               @RequestParam long carId,
                                               @RequestParam long customerId) {
        Booking book = bookingService.bookCar(bookingRequest, carId, customerId);
        return ResponseEntity.ok(new APIResponse("SUCCESS", book.getId()));
    }

    @GetMapping(Url.GET_ALL)
    public ResponseEntity<APIResponse> getBookings() {
        List<Booking> bookingList = bookingService.getAllBookings();
        return ResponseEntity.ok(new APIResponse("SUCCESS", bookingList));
    }

    @GetMapping(Url.GET_BY_ID)
    public ResponseEntity<APIResponse> getBooking(@PathVariable long id) {
        BookingDto booking = bookingService.getBookingDtoById(id);
        return ResponseEntity.ok(new APIResponse("SUCCESS", booking));
    }

    @PutMapping(Url.UPDATE_BY_ID)
    public ResponseEntity<APIResponse> updateBooking(@PathVariable long id,
                                                     @RequestBody BookingUpdateRequest bookingUpdateRequest) {
        Booking booking = bookingService.updateBooking(bookingUpdateRequest,id);
        return ResponseEntity.ok(new APIResponse("SUCCESS", booking.getId()));
    }

    @DeleteMapping(Url.DELETE_BY_ID)
    public ResponseEntity<APIResponse> deleteBooking(@PathVariable long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(new APIResponse("DELETE_SUCCESS",null));
    }

    @PutMapping(Url.BOOKING_APPROVED)
    public ResponseEntity<APIResponse> approvedBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.approveBooking(bookingId);
        return ResponseEntity.ok(new APIResponse("SUCCESS", booking.getId()));
    }

    @PutMapping(Url.BOOKING_REJECTED)
    public ResponseEntity<APIResponse> declineBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.declineBooking(bookingId);
        return ResponseEntity.ok(new APIResponse("SUCCESS", booking.getId()));
    }

    @PutMapping(Url.BOOKING_COMPLETED)
    public ResponseEntity<APIResponse> completeBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.completeBooking(bookingId);
        return ResponseEntity.ok(new APIResponse("SUCCESS", booking.getId()));
    }

    @PutMapping(Url.BOOKING_CANCELED)
    public ResponseEntity<APIResponse> cancelBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(new APIResponse("SUCCESS", booking.getId()));
    }
}