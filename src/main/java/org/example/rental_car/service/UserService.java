package org.example.rental_car.service;

import org.example.rental_car.dto.UserDto;
import org.example.rental_car.entities.User;
import org.example.rental_car.request.RegisterRequest;
import org.example.rental_car.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    User createUser(RegisterRequest registerRequest);
    User getUserByEmail(String email);
    User updateUserById(long userId, UpdateUserRequest updateRequest);
    void deleteUserById(long userId);
    User getUserById(long userId);
    List<UserDto> getAllUsers();
    UserDto getUserDetails(long userId);
}
