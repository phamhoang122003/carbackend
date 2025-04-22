package org.example.rental_car.service;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.entities.User;
import org.example.rental_car.entities.Token;
import org.example.rental_car.repository.UserRepository;
import org.example.rental_car.repository.TokenRepository;
import org.example.rental_car.sendEmail.PasswordResetEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public Optional<User> findUserByPasswordResetToken(String token) {
        return tokenRepository.findByToken(token).map(Token::getUser);
    }

    @Override
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresentOrElse(user -> {
            PasswordResetEvent passwordResetEvent = new PasswordResetEvent(this, user);
            eventPublisher.publishEvent(passwordResetEvent);
        }, () -> {throw new RuntimeException("NO_USER_FOUND"+email);});

    }

    @Override
    public String resetPassword(String password, User user) {
        try{
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return "PASSWORD_RESET_SUCCESS";
        }   catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
