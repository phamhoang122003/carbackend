package org.example.rental_car.service;

import org.example.rental_car.dto.CarDto;
import org.example.rental_car.entities.Car;
import org.example.rental_car.entities.CarOwner;
import org.example.rental_car.request.CarRegisterRequest;
import org.example.rental_car.request.UpdateCarRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarService {
    Car save(CarRegisterRequest carRegisterRequest, long ownerId) throws IOException;
    Page<Car> findAll(int size, int page);
    Car findById(long carId);
    List<CarDto> findByOwnerId(long ownerId);
    Car update(long carId, UpdateCarRequest carRequest);
    void deleteById(long carId);
    Car saveImage(long carId, MultipartFile file) throws IOException;
    CarOwner findCarOwnerByCarId(long carId);
    List<CarDto> findAllCarsNoPage();
    List<CarDto> findCarBySearchQuery(String ownerEmail, String carName, String brand);
    List<CarDto> findCarByAddress(String address);
    CarDto getCarDetails(long carId);
}
