package com.spring.carbackend.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.carbackend.DTO.CarAddDTO;
import com.spring.carbackend.DTO.CarEditDTO;
import com.spring.carbackend.entities.Car;
import com.spring.carbackend.entities.CarStatus;
import com.spring.carbackend.entities.Users;
import com.spring.carbackend.repository.CarOwnerRepository;
import com.spring.carbackend.repository.CarRepository;
import com.spring.carbackend.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/rest/")
public class carRest {

    final UsersRepository usersRepository;

    final CarRepository carRepository;

    final CarOwnerRepository carOwnerRepository;

    private static final String UPLOAD_IMAGES = "D:\\carbackend\\src\\main\\resources\\static\\images\\car-owner";
    private static final String UPLOAD_FILE = "D:\\carbackend\\src\\main\\resources\\static\\documents\\car-owner";

    public carRest(UsersRepository usersRepository, CarRepository carRepository, CarOwnerRepository carOwnerRepository) {
        this.usersRepository = usersRepository;
        this.carRepository = carRepository;
        this.carOwnerRepository = carOwnerRepository;
    }

    @PostMapping("/car-owner/mycar/add")
    @ResponseBody
    public String addCar(
            @RequestParam(value = "carDto") String carRequest,
            @RequestParam("file0") MultipartFile file0,
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3,
            @RequestParam("file4") MultipartFile file4,
            @RequestParam("file5") MultipartFile file5,
            @RequestParam("file6") MultipartFile file6
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        CarAddDTO carDto;
        try {
            carDto = objectMapper.readValue(carRequest, CarAddDTO.class);
        }catch (JsonProcessingException e)  {
            throw new RuntimeException("loi parse");
        }


        String address = carDto.getCity() + ", " + carDto.getDistrict() + ", " + carDto.getWard();
        if (carDto.getHomeNumber() != null && !carDto.getHomeNumber().isEmpty()) {
            address += ", " + carDto.getHomeNumber();
        }
        String carName = carDto.getBrand() + " " + carDto.getModel();

        String carFunction = "";
        if (carDto.getFunctions() != null) {
            carFunction = String.join(", ", carDto.getFunctions());
        }

        String carTerm = "";
        if (carDto.getTerms() != null) {
            Integer[] termArray = {0, 0, 0, 0};
            for (int i = 0; i < termArray.length; i++) {
                for (Integer j : carDto.getTerms()) {
                    if ((i + 1) == j) {
                        termArray[i] = j;
                        break;
                    }
                }
            }
            String termString = Arrays.stream(termArray)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            carTerm = termString;
        }

        if (carDto.getOtherTerm() != null && !carDto.getOtherTerm().isEmpty()) {
            carTerm += "_" + carDto.getOtherTerm();
        }

        // Xử lý mileage
        BigDecimal carMileage = null;
        try {
            carMileage = carDto.getMileage() != null && !carDto.getMileage().isEmpty()
                    ? new BigDecimal(carDto.getMileage().replaceAll("[^\\d.]", ""))  // Loại bỏ ký tự không phải số
                    : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid mileage format: " + carDto.getMileage());
        }

// Xử lý fuelConsumption
        BigDecimal carFuelConsumption = null;
        try {
            carFuelConsumption = carDto.getFuelConsumption() != null && !carDto.getFuelConsumption().isEmpty()
                    ? new BigDecimal(carDto.getFuelConsumption().replaceAll("[^\\d.]", ""))
                    : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid fuel consumption format: " + carDto.getFuelConsumption());
        }

// Tương tự cho basePrice và deposit
        BigDecimal carBasePrice = null;
        try {
            carBasePrice = carDto.getBasePrice() != null && !carDto.getBasePrice().isEmpty()
                    ? new BigDecimal(carDto.getBasePrice().replaceAll("[^\\d.]", ""))
                    : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid base price format: " + carDto.getBasePrice());
        }

        BigDecimal carDeposit = null;
        try {
            carDeposit = carDto.getDeposit() != null && !carDto.getDeposit().isEmpty()
                    ? new BigDecimal(carDto.getDeposit().replaceAll("[^\\d.]", ""))
                    : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid deposit format: " + carDto.getDeposit());
        }

        String carImages = "";
        MultipartFile[] files = {file0, file1, file2, file3, file4, file5, file6};
        for (int i = 0; i < files.length; i++) {
            if (i <= 2) {
                carImages = carImages + upFiles(files[i]) + ", ";
            } else if (i < 6) {
                carImages = carImages + upImages(files[i]) + ", ";
            } else {
                carImages = carImages + upImages(files[i]);
            }
        }
        // Bước 1: Lấy thông tin Users từ userId trong CarDto
        Users user = usersRepository.findById(1).orElse(null);

//        // Bước 2: Tạo và lưu CarOwner với liên kết đến Users
//        CarOwner carOwner = new CarOwner();
//        carOwner.setUser(user); // Gán user cho CarOwner
//        carOwnerRepository.save(carOwner);


        Car car = Car.builder()
                .additionalFunctions(carFunction)
                .address(address)
                .basePrice(carBasePrice)
                .brand(carDto.getBrand())
                .color(carDto.getColor())
                .deposit(carDeposit)
                .description(carDto.getDescription())
                .fuelConsumption(carFuelConsumption)
                .fuelType(carDto.getFuel())
                .licensePlate(carDto.getLicense())
                .mileage(carMileage)
                .model(carDto.getModel())
                .name(carName)
                .numberOfSeats(carDto.getSeatNumber())
                .productionYears(carDto.getProductionYear())
                .termsOfUse(carTerm)
                .transmissionType(carDto.getTransmission())
                .images(carImages)
                .carStatus(CarStatus.Available)
                .user(user)
                .build();

        carRepository.save(car);
        return "Success";
    }

    @PutMapping("/car-owner/mycar/edit")
    @ResponseBody
    public String editCar(@RequestParam(value = "carDto") String carRequest,
                          @RequestParam("file0") MultipartFile file0,
                          @RequestParam("file1") MultipartFile file1,
                          @RequestParam("file2") MultipartFile file2,
                          @RequestParam("file3") MultipartFile file3,
                          @RequestParam("file4") MultipartFile file4,
                          @RequestParam("file5") MultipartFile file5,
                          @RequestParam("file6") MultipartFile file6){


        ObjectMapper objectMapper = new ObjectMapper();
        CarEditDTO carDto;
        try {
            carDto = objectMapper.readValue(carRequest, CarEditDTO.class);
        }catch (JsonProcessingException e)  {
            throw new RuntimeException("loi parse");
        }

        Car car0= carRepository.findById(carDto.getId()).orElse(null);
        if(car0 == null) throw new RuntimeException("car not found");


        String address = carDto.getCity() + ", " + carDto.getDistrict() + ", " + carDto.getWard();
        if (carDto.getHomeNumber() != null && !carDto.getHomeNumber().isEmpty()) {
            address += ", " + carDto.getHomeNumber();
        }
        String carName = carDto.getBrand() + " " + carDto.getModel();

        String carFunction = "";
        if (carDto.getFunctions() != null) {
            carFunction = String.join(", ", carDto.getFunctions());
        }

        String carTerm = "";
        if (carDto.getTerms() != null) {
            Integer[] termArray = {0, 0, 0, 0};
            for (int i = 0; i < termArray.length; i++) {
                for (Integer j : carDto.getTerms()) {
                    if ((i + 1) == j) {
                        termArray[i] = j;
                        break;
                    }
                }
            }
            String termString = Arrays.stream(termArray)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            carTerm = termString;
        }

        if (carDto.getOtherTerm() != null && !carDto.getOtherTerm().isEmpty()) {
            carTerm += "_" + carDto.getOtherTerm();
        }

        // Xử lý mileage
        BigDecimal carMileage = null;
        try {
            carMileage = carDto.getMileage() != null && !carDto.getMileage().isEmpty()
                    ? new BigDecimal(carDto.getMileage().replaceAll("[^\\d.]", ""))  // Loại bỏ ký tự không phải số
                    : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid mileage format: " + carDto.getMileage());
        }

// Xử lý fuelConsumption
        BigDecimal carFuelConsumption = null;
        try {
            carFuelConsumption = carDto.getFuelConsumption() != null && !carDto.getFuelConsumption().isEmpty()
                    ? new BigDecimal(carDto.getFuelConsumption().replaceAll("[^\\d.]", ""))
                    : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid fuel consumption format: " + carDto.getFuelConsumption());
        }

// Tương tự cho basePrice và deposit
        BigDecimal carBasePrice = null;
        try {
            carBasePrice = carDto.getBasePrice() != null && !carDto.getBasePrice().isEmpty()
                    ? new BigDecimal(carDto.getBasePrice().replaceAll("[^\\d.]", ""))
                    : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid base price format: " + carDto.getBasePrice());
        }

        BigDecimal carDeposit = null;
        try {
            carDeposit = carDto.getDeposit() != null && !carDto.getDeposit().isEmpty()
                    ? new BigDecimal(carDto.getDeposit().replaceAll("[^\\d.]", ""))
                    : null;
        } catch (NumberFormatException e) {
            System.err.println("Invalid deposit format: " + carDto.getDeposit());
        }

        String carImages = "";
        MultipartFile[] files = {file0, file1, file2, file3, file4, file5, file6};
        for (int i = 0; i < files.length; i++) {
            if (i <= 2) {
                carImages = carImages + upFiles(files[i]) + ", ";
            } else if (i < 6) {
                carImages = carImages + upImages(files[i]) + ", ";
            } else {
                carImages = carImages + upImages(files[i]);
            }
        }
        car0.setAdditionalFunctions(carFunction);
        car0.setAddress(address);
        car0.setBasePrice(carBasePrice);
        car0.setBrand(carDto.getBrand());
        car0.setColor(carDto.getColor());
        car0.setDeposit(carDeposit);
        car0.setDescription(carDto.getDescription());
        car0.setFuelConsumption(carFuelConsumption);
        car0.setFuelType(carDto.getFuel());
        car0.setLicensePlate(carDto.getLicense());
        car0.setMileage(carMileage);
        car0.setModel(carDto.getModel());
        car0.setName(carName);
        car0.setNumberOfSeats(carDto.getSeatNumber());
        car0.setProductionYears(carDto.getProductionYear());
        car0.setTermsOfUse(carTerm);
        car0.setTransmissionType(carDto.getTransmission());
        car0.setImages(carImages);
        if(carDto.getCarStatus().equals(CarStatus.Available)){
            car0.setCarStatus(CarStatus.Available);
        } else {
            car0.setCarStatus(CarStatus.Stopped);
        }
        carRepository.save(car0);

        return "success";
    }


    @GetMapping("/car-owner/mycar/list")
    public List<Car> getAllCarByCarOwnerId(
            @RequestParam(value = "page" ,defaultValue = "1") Integer pageNumber
    ) {
        int pageSizeDefault = 5;
        Pageable pageable = PageRequest.of(pageNumber-1, pageSizeDefault);
        Page<Car> page = null;
            try {
                Users user = usersRepository.findById(1).orElse(null);
                page = carRepository.findByUser(user,pageable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return page.getContent();
    }


    private String upImages(MultipartFile file) {
        try {
            // Lưu tệp vào thư mục chỉ định
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); //unique name
            String uploadFilePath = UPLOAD_IMAGES + File.separator + uniqueFileName;
            file.transferTo(new File(uploadFilePath));
            return "/static/images/car-owner/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String upFiles(MultipartFile file) {
        try {
            // Lưu tệp vào thư mục chỉ định
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); //unique name
            String uploadFilePath = UPLOAD_FILE + File.separator + uniqueFileName;
            file.transferTo(new File(uploadFilePath));
            return "/static/documents/car-owner/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
