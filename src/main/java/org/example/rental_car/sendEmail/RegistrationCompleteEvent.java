package org.example.rental_car.sendEmail;

import lombok.Getter;
import lombok.Setter;
import org.example.rental_car.entities.User;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    public RegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}
