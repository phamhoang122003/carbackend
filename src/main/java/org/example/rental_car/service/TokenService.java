package org.example.rental_car.service;

import org.example.rental_car.entities.Token;
import org.example.rental_car.entities.User;

import java.util.Optional;

public interface TokenService {
    String validateToken(String token);
    void saveTokenForUser(String token, User user);
    Token generateNewToken(String oldToken);
    Optional<Token> findByToken(String token);
    void deleteToken(Long tokenId);
    boolean isTokenExpired(String token);
}
