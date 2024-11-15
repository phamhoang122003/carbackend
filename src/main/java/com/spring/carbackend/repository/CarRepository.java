package com.spring.carbackend.repository;

import com.spring.carbackend.entities.Car;
import com.spring.carbackend.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Integer> {
    Page<Car> findByUser(Users users,Pageable pageable);
}
