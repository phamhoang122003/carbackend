package org.example.rental_car.service;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.config.Mapper;
import org.example.rental_car.dto.BookingDto;
import org.example.rental_car.entities.BookingStatus;
import org.example.rental_car.entities.*;
import org.example.rental_car.repository.BookingRepository;
import org.example.rental_car.repository.CarOwnerRepository;
import org.example.rental_car.repository.CarRepository;
import org.example.rental_car.repository.CustomerRepository;
import org.example.rental_car.request.BookingRequest;
import org.example.rental_car.request.BookingUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final Mapper mapper;

    @Override
    @Transactional
    public Booking bookCar(BookingRequest bookingRequest, long carId, long customerId) {
        Car car = getCarById(carId);
                Booking bookingBefore = bookingRepository.findByStatusAndCarId(BookingStatus.APPROVED, carId);
        if (bookingBefore != null && (checkDateInRange(bookingBefore.getStartDate(), LocalDate.parse(bookingRequest.getStartDate()), LocalDate.parse(bookingRequest.getEndDate()))
                || checkDateInRange(bookingBefore.getEndDate(), LocalDate.parse(bookingRequest.getStartDate()), LocalDate.parse(bookingRequest.getEndDate())))) {
            throw new IllegalArgumentException("Car is already booked in that time");
        }
        Customer customer = getCustomerById(customerId);
        Booking booking = new Booking();
        booking.setPaymentMethod(bookingRequest.getPaymentMethod());
        booking.setStartDate(LocalDate.parse(bookingRequest.getStartDate()));
        booking.setEndDate(LocalDate.parse(bookingRequest.getEndDate()));
        booking.setCar(car);
        booking.setCustomer(customer);
        booking.setAppointmentNo();
        booking.setBill(booking.getOverAllPrice());
        booking.setStatus(BookingStatus.WAITING_FOR_APPROVAL);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public BookingDto getBookingDtoById(long bookingId) {
        Booking booking = getBookingById(bookingId);
        return mapper.mapBookingToDto(booking);
    }

    @Override
    @Transactional
    public Booking updateBooking(BookingUpdateRequest bookingUpdateRequest, long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(bookingUpdateRequest.getStatus());
        bookingStatusHandle(booking);
        return bookingRepository.save(booking);
    }

    @Transactional
    public void bookingStatusHandle(Booking booking) {
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            Car car = getCarById(booking.getCar().getId());
            Customer customer = getCustomerById(booking.getCustomer().getId());
            CarOwner carOwner = carOwnerRepository.findByCarId(booking.getCar().getId());
            customer.setWallet(customer.getWallet() - booking.getBill() + car.getDeposit());
            carOwner.setWallet(carOwner.getWallet() + booking.getBill());
            customerRepository.save(customer);
            carOwnerRepository.save(carOwner);
        } else if (booking.getStatus() == BookingStatus.REJECTED) {
            deleteBooking(booking.getId());
        } else if (booking.getStatus() == BookingStatus.APPROVED) {
            Car car = getCarById(booking.getCar().getId());
            Customer customer = getCustomerById(booking.getCustomer().getId());
            customer.setWallet(customer.getWallet() - car.getDeposit());
            customerRepository.save(customer);
        }
    }

    public boolean checkDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    private Customer getCustomerById(long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public void deleteBooking(long bookingId) {
        Booking booking = getBookingById(bookingId);
        bookingRepository.delete(booking);
    }


    @Override
    public List<BookingDto> getBookingByCustomerId(long customerId) {
        List<Booking> bookings = bookingRepository.findAllByCustomerId(customerId);
        if (bookings.isEmpty()) {
            return null;
        }
        return bookings.stream().map(mapper::mapBookingToDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingByCarId(long carId) {
        List<Booking> bookings = bookingRepository.findAllByCarId(carId);
        if (bookings.isEmpty()) {
            return null;
        }
        return bookings.stream().map(mapper::mapBookingToDto).collect(Collectors.toList());
    }

    @Override
    public Booking approveBooking(long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(BookingStatus.APPROVED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking declineBooking(long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(BookingStatus.REJECTED);
        Customer customer = getCustomerById(booking.getCustomer().getId());
        Car car = getCarById(booking.getCar().getId());
        customer.setWallet(customer.getWallet() + car.getDeposit());
        customerRepository.save(customer);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking completeBooking(long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(BookingStatus.COMPLETED);
        Customer customer = getCustomerById(booking.getCustomer().getId());
        customer.setWallet(customer.getWallet() - booking.getBill());
        customerRepository.save(customer);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking cancelBooking(long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(BookingStatus.CANCELED);
        return bookingRepository.save(booking);
    }

    @Override
    public List<BookingDto> getBookingByOwnerId(long ownerId) {
        CarOwner owner = carOwnerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        if (owner.getCars().isEmpty()){
            return null;
        }
        List<BookingDto> bookingDtos = new ArrayList<>();
        for(Car car : owner.getCars()){
            List<BookingDto> bookingDtos1 = getBookingByCarId(car.getId());
            if(bookingDtos1 != null){
                bookingDtos.addAll(bookingDtos1);
            }
        }
        return bookingDtos;
    }

    public Car getCarById(long carId) {
        return carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
    }

}
