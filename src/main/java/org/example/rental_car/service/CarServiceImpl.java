package org.example.rental_car.service;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.config.Mapper;
import org.example.rental_car.dto.BookingDto;
import org.example.rental_car.dto.CarDto;
import org.example.rental_car.dto.ReviewDto;
import org.example.rental_car.entities.Car;
import org.example.rental_car.entities.CarOwner;
import org.example.rental_car.repository.BookingRepository;
import org.example.rental_car.repository.CarOwnerRepository;
import org.example.rental_car.repository.CarRepository;
import org.example.rental_car.repository.ReviewRepository;
import org.example.rental_car.request.CarRegisterRequest;
import org.example.rental_car.request.UpdateCarRequest;
import org.example.rental_car.service.ReviewService;
import org.example.rental_car.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final Mapper mapper;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public Car save(CarRegisterRequest carRegisterRequest, long ownerId) {
        if(carRepository.existsByLicensePlate(carRegisterRequest.getLicensePlate())) {
            throw new RuntimeException("License Plate: " + carRegisterRequest.getLicensePlate() + " already exists");
        }
        Car car = modelMapper.map(carRegisterRequest, Car.class);
        CarOwner owner = carOwnerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Car owner not found"));
        car.setOwner(owner);
        return carRepository.save(car);
    }

    @Override
    public Page<Car> findAll(int size, int page) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return carRepository.findAll(pageRequest);
    }

    @Override
    public Car findById(long carId) {
        return carRepository.findById(carId).orElseThrow(() -> new RuntimeException("NOT_FOUND"));
    }

    @Override
    public List<CarDto> findByOwnerId(long ownerId) {
        List<Car> cars = carRepository.findAllByOwnerId(ownerId);
        return cars.stream()
                .map(car -> getCarDetails(car.getId()))
                .toList();
    }

    @Override
    @Transactional
    public Car update(long carId, UpdateCarRequest carRequest) {
        Car car = findById(carId);
        modelMapper.map(carRequest, car);
        return carRepository.save(car);
    }

    @Override
    public void deleteById(long carId) {
        Car car = findById(carId);
        carRepository.delete(car);
    }

    @Override
    @Transactional
    public Car saveImage(long carId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }
        Car car = findById(carId);
        car.setImage(file.getBytes());
        return carRepository.save(car);
    }

    @Override
    public CarOwner findCarOwnerByCarId(long carId) {
        Car car = findById(carId);
        return carOwnerRepository.findById(car.getOwner().getId()).orElseThrow(() -> new RuntimeException("Car owner not found"));
    }

    @Override
    public List<CarDto> findAllCarsNoPage() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(car -> getCarDetails(car.getId())).toList();
    }

    @Override
    public List<CarDto> findCarBySearchQuery(String ownerEmail, String carName, String brand) {
        List<Car> cars;
        if (ownerEmail != null && carName != null && brand != null) {
            CarOwner owner = (CarOwner) userService.getUserByEmail(ownerEmail);
            cars = carRepository.findAllByOwnerIdAndNameAndBrand(owner.getId(), carName, brand);
        } else if (ownerEmail != null && carName != null) {
            CarOwner owner = (CarOwner) userService.getUserByEmail(ownerEmail);
            cars = carRepository.findAllByOwnerIdAndName(owner.getId(), carName);
        } else if (ownerEmail != null && brand != null) {
            CarOwner owner = (CarOwner) userService.getUserByEmail(ownerEmail);
            cars = carRepository.findAllByOwnerIdAndBrand(owner.getId(), brand);
        } else if (carName != null && brand != null) {
            cars = carRepository.findAllByNameAndBrand(carName, brand);
        } else if (ownerEmail != null) {
            CarOwner owner = (CarOwner) userService.getUserByEmail(ownerEmail);
            cars = carRepository.findAllByOwnerId(owner.getId());
        } else if (carName != null) {
            cars = carRepository.findAllByName(carName);
        } else if (brand != null) {
            cars = carRepository.findAllByBrand(brand);
        } else {
            throw new RuntimeException("No search parameters provided");
        }

        return cars.stream().map(car -> getCarDetails(car.getId())).toList();
    }

    @Override
    public List<CarDto> findCarByAddress(String address) {
        List<Car> cars = carRepository.findByAddressContainingIgnoreCase(address);
        return cars.stream().map(car -> getCarDetails(car.getId())).toList();
    }

    @Override
    public CarDto getCarDetails(long carId) {
        Car car = findById(carId);
        List<ReviewDto> reviews = reviewRepository.findByCarId(carId)
                .stream().map(mapper::mapReviewToDto)
                .toList();
        List<BookingDto> bookingDtos = bookingRepository
                .findAllByCarId(carId)
                .stream()
                .map(mapper::mapBookingToDto)
                .toList();
        return mapper.mapCarToDto(car,reviews,bookingDtos,reviewService.getAverageRatingByCarId(carId),reviewRepository.countByCarId(carId));
    }
}
