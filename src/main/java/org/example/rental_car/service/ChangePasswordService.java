package org.example.rental_car.service;

import org.example.rental_car.request.ChangePasswordRequest;

public interface ChangePasswordService {
    void changePassword(long userId, ChangePasswordRequest passwordRequest);
}
