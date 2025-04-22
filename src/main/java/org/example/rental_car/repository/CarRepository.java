package org.example.rental_car.repository;

import org.example.rental_car.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("select c from Car c where c.owner.id =:ownerId")
    List<Car> findAllByOwnerId(@Param("ownerId") long ownerId);

    List<Car> findAllByBrand(String brand);

    List<Car> findAllByName(String carName);

    List<Car> findByAddressContainingIgnoreCase(String address);

    List<Car> findAllByOwnerIdAndNameAndBrand(long id, String carName, String brand);

    List<Car> findAllByOwnerIdAndName(long id, String carName);

    List<Car> findAllByOwnerIdAndBrand(long id, String brand);

    List<Car> findAllByNameAndBrand(String carName, String brand);

    boolean existsByLicensePlate(String licensePlate);
}
