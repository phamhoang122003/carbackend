package org.example.rental_car.request;

import lombok.Data;
import org.example.rental_car.entities.User;

import java.util.Date;

@Data
public class TokenRequest {
    private String token;
    private Date expirationTime;
    private User user;
}
