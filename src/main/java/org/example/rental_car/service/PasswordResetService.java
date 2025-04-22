package org.example.rental_car.service;

import org.example.rental_car.entities.User;

import java.util.Optional;

public interface PasswordResetService {
    Optional<User> findUserByPasswordResetToken(String token);
    void requestPasswordReset(String email);
    String resetPassword(String password, User user);
}
