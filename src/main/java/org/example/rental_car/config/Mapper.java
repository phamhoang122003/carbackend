package org.example.rental_car.config;

import org.example.rental_car.dto.BookingDto;
import org.example.rental_car.dto.CarDto;
import org.example.rental_car.dto.ReviewDto;
import org.example.rental_car.entities.Booking;
import org.example.rental_car.entities.Car;
import org.example.rental_car.entities.Review;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapper {
    public ReviewDto mapReviewToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setComment(review.getComment());
        reviewDto.setRating(review.getRating());
        reviewDto.setCarName(review.getCar().getName());
        reviewDto.setCustomerName(review.getCustomer().getName());
        reviewDto.setCarId(review.getCar().getId());
        reviewDto.setCustomerId(review.getCustomer().getId());
        return reviewDto;
    }

    public BookingDto mapBookingToDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getCustomer().getName(),
                booking.getCustomer().getId(),
                booking.getCar().getId(),
                booking.getCar().getName(),
                booking.getPaymentMethod(),
                booking.getStatus(),
                booking.getBookingNo(),
                booking.getBill()
        );
    }

    public CarDto mapCarToDto(Car car, List<ReviewDto> reviews, List<BookingDto> bookings, double rating, long count) {
        return new CarDto(
                car.getId(),
                car.getName(),
                car.getLicensePlate(),
                car.getBrand(),
                car.getModel(),
                car.getColor(),
                car.getSeats(),
                car.getTransmissionType(),
                car.getFuelType(),
                car.getMileage(),
                car.getFuelConsumption(),
                car.getAdditionalFunction(),
                car.getProductionYear(),
                car.getDeposit(),
                car.getDescription(),
                car.getAddress(),
                car.getBasePrice(),
                car.getImage(),
                car.getOwner().getEmail(),
                rating,
                reviews,
                count,
                bookings);
    }
}
