package org.example.rental_car.service;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.config.Mapper;
import org.example.rental_car.dto.*;
import org.example.rental_car.entities.*;
import org.example.rental_car.repository.*;
import org.example.rental_car.request.RegisterRequest;
import org.example.rental_car.request.UpdateUserRequest;
import org.example.rental_car.service.BookingService;
import org.example.rental_car.service.CarService;
import org.example.rental_car.service.ReviewService;
import org.example.rental_car.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final CustomerRepository customerRepository;
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;
    private final EntityConverter<User, UserDto> userEntityConverter;
    private final BookingService bookingService;
    private final CarRepository carRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public User createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        switch (registerRequest.getRole()) {
            case "OWNER" ->{
                CarOwner user = modelMapper.map(registerRequest, CarOwner.class);
                user.setBirthday(LocalDate.parse(registerRequest.getBirthday()));
                user.setRoles(roleService.setUserRole("OWNER"));
                return carOwnerRepository.save(user);
            }
            case "CUSTOMER" ->{
                Customer user = modelMapper.map(registerRequest, Customer.class);
                user.setBirthday(LocalDate.parse(registerRequest.getBirthday()));
                user.setRoles(roleService.setUserRole("CUSTOMER"));
                return customerRepository.save(user);
            }
            default -> throw new RuntimeException("Invalid role");
        }

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("FOUND"));
    }

    @Override
    @Transactional
    public User updateUserById(long userId, UpdateUserRequest updateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("NOT_FOUND"));
        user.setName(updateRequest.getName());
        user.setNationalId(updateRequest.getNationalId());
        user.setPhone(updateRequest.getPhone());
        user.setAddress(updateRequest.getAddress());
        user.setDrivingLicense(updateRequest.getDrivingLicense());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(long userId) {
        userRepository.findById(userId).ifPresentOrElse(userToDelete -> {
            if(userToDelete.getRole().equals("CUSTOMER")) {
                List<Review> reviews = new ArrayList<>(reviewRepository.findAllByCustomerId1(userId));
                reviewRepository.deleteAll(reviews);
                List<Booking> bookings = new ArrayList<>(bookingRepository.findAllByCustomerId(userId));
                bookingRepository.deleteAll(bookings);
            }
            if(userToDelete.getRole().equals("OWNER")) {
                List<Car> cars = new ArrayList<>(carRepository.findAllByOwnerId(userId));
                carRepository.deleteAll(cars);
                List<Booking> bookings = new ArrayList<>();
                for (Car car : cars) {
                    List<Booking> carBookings = new ArrayList<>(bookingRepository.findAllByCarId(car.getId()));
                    bookings.addAll(carBookings);
                }
                bookingRepository.deleteAll(bookings);
            }
            userRepository.deleteById(userId);
        }, () -> {
            throw new RuntimeException("NOT_FOUND");
        });
    }

    @Override
    public User getUserById(long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("NOT_FOUND"));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> userEntityConverter
                        .mapEntityToDTO(user,UserDto.class))
                .toList();
    }

    @Override
    public UserDto getUserDetails(long userId) {
        User user = getUserById(userId);
        UserDto userDto = userEntityConverter.mapEntityToDTO(user,UserDto.class);
        if(user.getRole().equals("CUSTOMER")){
            userDto.setBookingDtos(bookingService.getBookingByCustomerId(user.getId()));
        }else {
            userDto.setBookingDtos(bookingService.getBookingByOwnerId(user.getId()));
        }
        userDto.setReviewDtos(reviewService.getReviewsByCustomerId(userId,0,5)
                .getContent()
                .stream()
                .map(mapper::mapReviewToDto)
                .collect(Collectors.toList())
        );
        if(user.getRole().equals("CUSTOMER")) {
            userDto.setCarDtos(null);
        } else {
            List<Car> cars = carRepository.findAllByOwnerId(user.getId());
            List<CarDto> carDtos = cars.stream().map(car -> {
                CarDto carDto = new CarDto();
                carDto.setId(car.getId());
                carDto.setName(car.getName());
                carDto.setBrand(car.getBrand());
                carDto.setOwnerEmail(car.getOwner().getEmail());
                carDto.setLicensePlate(car.getLicensePlate());
                return carDto;
            }).toList();
            userDto.setCarDtos(carDtos);
        }
        return userDto;
    }
}
