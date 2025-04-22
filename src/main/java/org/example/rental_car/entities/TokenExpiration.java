package org.example.rental_car.entities;

import java.util.Calendar;
import java.util.Date;

public class TokenExpiration {
    private static final int EXPIRATION_TIME  = 2;

    public static Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return  new Date(calendar.getTime().getTime());

    }
}
