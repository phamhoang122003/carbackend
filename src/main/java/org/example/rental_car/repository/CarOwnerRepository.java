package org.example.rental_car.repository;

import org.example.rental_car.entities.CarOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Long> {
    @Query("SELECT co FROM CarOwner co JOIN co.cars c WHERE c.id = :carId")
    CarOwner findByCarId(@Param("carId") long carId);
}
