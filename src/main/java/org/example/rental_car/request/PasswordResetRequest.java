package org.example.rental_car.request;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String token;
    private String newPassword;

}
