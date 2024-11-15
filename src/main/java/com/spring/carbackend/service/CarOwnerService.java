package com.spring.carbackend.service;

import com.spring.carbackend.entities.Users;

public interface CarOwnerService {
    Users findById(Integer id);
}
