package org.example.rental_car.controller;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.common.Url;
import org.example.rental_car.entities.User;
import org.example.rental_car.entities.Token;
import org.example.rental_car.repository.UserRepository;
import org.example.rental_car.request.TokenRequest;
import org.example.rental_car.response.APIResponse;
import org.example.rental_car.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Url.TOKEN_VERIFICATION)
public class TokenController {

    private final TokenService verificationTokenService;
    private final UserRepository userRepository;


    @GetMapping(Url.VALIDATE_TOKEN)
    public ResponseEntity<APIResponse> validateToken(String token) {
        String result = verificationTokenService.validateToken(token);
        APIResponse response = switch (result) {
            case "INVALID" -> new APIResponse("INVALID_TOKEN", null);
            case "VERIFIED" -> new APIResponse("TOKEN_ALREADY_VERIFIED", null);
            case "EXPIRED" -> new APIResponse("EXPIRED_TOKEN", null);
            case "VALID" -> new APIResponse("VALID_VERIFICATION_TOKEN", null);
            default -> new APIResponse("TOKEN_VALIDATION_ERROR", null);
        };
        return ResponseEntity.ok(response);
    }

    @GetMapping(Url.CHECK_TOKEN_EXPIRATION)
    public ResponseEntity<APIResponse> checkTokenExpiration(String token) {
        boolean isExpired = verificationTokenService.isTokenExpired(token);
        APIResponse response;
        if (isExpired) {
            response = new APIResponse("EXPIRED_TOKEN", null);
        } else {
            response = new APIResponse("VALID_VERIFICATION_TOKEN", null);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(Url.SAVE_TOKEN)
    public ResponseEntity<APIResponse> saveVerificationTokenForUser(@RequestBody TokenRequest request) {
        User user = userRepository.findById(request.getUser().getId())
                .orElseThrow(() -> new RuntimeException("USER_FOUND"));
        verificationTokenService.saveTokenForUser(request.getToken(), user);
        return ResponseEntity.ok(new APIResponse("TOKEN_SAVED_SUCCESS", null));
    }

    @PutMapping(Url.GENERATE_NEW_TOKEN_FOR_USER)
    public ResponseEntity<APIResponse> generateNewVerificationToken(@RequestParam String oldToken) {
        Token newToken = verificationTokenService.generateNewToken(oldToken);
        return ResponseEntity.ok(new APIResponse("", newToken));
    }

    @DeleteMapping(Url.DELETE_TOKEN)
    public ResponseEntity<APIResponse> deleteUserToken(@RequestParam Long userId) {
        verificationTokenService.deleteToken(userId);
        return ResponseEntity.ok(new APIResponse("TOKEN_DELETE_SUCCESS", null));
    }

}

