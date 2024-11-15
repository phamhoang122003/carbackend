package com.spring.carbackend.repository;

import com.spring.carbackend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarOwnerRepository extends JpaRepository<Users, Integer> {

}
