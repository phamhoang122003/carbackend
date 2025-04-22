package org.example.rental_car.controller;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.common.Url;
import org.example.rental_car.dto.CarDto;
import org.example.rental_car.entities.Car;
import org.example.rental_car.request.CarRegisterRequest;
import org.example.rental_car.request.UpdateCarRequest;
import org.example.rental_car.response.APIResponse;
import org.example.rental_car.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(Url.CAR)
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping(Url.GET_BY_ID)
    public ResponseEntity<APIResponse> getCarById(@PathVariable int id) {
        CarDto car = carService.getCarDetails(id);
        return ResponseEntity.ok(new APIResponse("FOUND", car));
    }

    @GetMapping(Url.GET_ALL)
    public ResponseEntity<APIResponse> getAllCars(@RequestParam int size,
                                                  @RequestParam int page) {
        Page<Car> cars = carService.findAll(size,page);
        return ResponseEntity.ok(new APIResponse("FOUND", cars));
    }

    @GetMapping(Url.GET_BY_OWNER_ID)
    public ResponseEntity<APIResponse> getAllCarByOwnerID(@PathVariable long ownerId) {
        List<CarDto> cars = carService.findByOwnerId(ownerId);
        return ResponseEntity.ok(new APIResponse("FOUND", cars));
    }

    @PostMapping(Url.ADD)
    public ResponseEntity<APIResponse> addCar(@RequestBody CarRegisterRequest carRegisterRequest,
                                              @RequestParam long ownerId) throws IOException {
        Car savedCar = carService.save(carRegisterRequest, ownerId);
        return ResponseEntity.ok(new APIResponse("SUCCESS", savedCar));
    }

    @PutMapping(Url.UPDATE_BY_ID)
    public ResponseEntity<APIResponse> updateCarById(@PathVariable int id, @RequestBody UpdateCarRequest carRequest) {
        Car car = carService.update(id, carRequest);
        return ResponseEntity.ok(new APIResponse("SUCCESS", car));
    }

    @PutMapping(Url.UPDATE_IMAGE_BY_ID)
    public ResponseEntity<APIResponse> updateImageById(@PathVariable long carId, @RequestParam("file") MultipartFile file) throws IOException {
        Car car = carService.saveImage(carId, file);
        return ResponseEntity.ok(new APIResponse("UPDATE_SUCCESS", car));
    }


    @DeleteMapping(Url.DELETE_BY_ID)
    public ResponseEntity<APIResponse> deleteCarById(@PathVariable int id) {
        carService.deleteById(id);
        return ResponseEntity.ok(new APIResponse("DELETE_SUCCESS", null));
    }

    @GetMapping("/get/allcars")
    public ResponseEntity<APIResponse> getAllCars() {
        List<CarDto> cars = carService.findAllCarsNoPage();
        return ResponseEntity.ok(new APIResponse("FOUND", cars));
    }

    @GetMapping("/search1")
    public ResponseEntity<APIResponse> searchCars1(@RequestParam(required = false) String ownerEmail,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String brand) {
        List<CarDto> carDtos = carService.findCarBySearchQuery(ownerEmail, name, brand);
        return ResponseEntity.ok(new APIResponse("FOUND", carDtos));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchCars(@RequestParam(required = false) String address) {
        List<CarDto> carDtos = carService.findCarByAddress(address);
        return ResponseEntity.ok(new APIResponse("FOUND", carDtos));
    }
}

