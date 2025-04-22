package org.example.rental_car.service;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.entities.Token;
import org.example.rental_car.entities.TokenExpiration;
import org.example.rental_car.entities.User;
import org.example.rental_car.repository.TokenRepository;
import org.example.rental_car.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    @Override
    public String validateToken(String token) {
        Optional<Token> theToken = findByToken(token);
        if (theToken.isEmpty()) {
            return "INVALID_TOKEN";
        }
        User user = theToken.get().getUser();
        if (user.isEnabled()){
            return "TOKEN_ALREADY_VERIFIED";
        }
        if (isTokenExpired(token)){
            return "EXPIRED_TOKEN";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "VALID_VERIFICATION_TOKEN";
    }

    @Override
    public void saveTokenForUser(String token, User user) {
        var verificationToken = new Token(token, user);
        tokenRepository.save(verificationToken);
    }

    @Transactional
    @Override
    public Token generateNewToken(String oldToken) {
        Optional<Token> theToken = findByToken(oldToken);
        if (theToken.isPresent()) {
            var verificationToken = theToken.get();
            verificationToken.setToken(UUID.randomUUID().toString());
            verificationToken.setExpirationDate(TokenExpiration.getExpirationTime());
            return tokenRepository.save(verificationToken);
        }else {
            throw new IllegalArgumentException("INVALID_VERIFICATION_TOKEN" + oldToken);
        }

    }
    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteToken(Long tokenId) {
        tokenRepository.deleteById(tokenId);
    }

    @Override
    public boolean isTokenExpired(String token) {
        Optional<Token> theToken = findByToken(token);
        if (theToken.isEmpty()) {
            return true;
        }
        Token token1 = theToken.get();
        return token1.getExpirationDate().getTime() <= Calendar.getInstance().getTime().getTime();
    }
}
