package com.spring.carbackend.repository;

import com.spring.carbackend.entities.RoleOfUser;
import com.spring.carbackend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleUserRepository extends JpaRepository<RoleOfUser, Integer> {
    RoleOfUser findByUser(Users users);
}
