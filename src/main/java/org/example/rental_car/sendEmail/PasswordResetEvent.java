package org.example.rental_car.sendEmail;

import lombok.Getter;
import lombok.Setter;
import org.example.rental_car.entities.User;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PasswordResetEvent extends ApplicationEvent {
    private final User user;
    public PasswordResetEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
