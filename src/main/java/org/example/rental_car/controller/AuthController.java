package org.example.rental_car.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rental_car.common.Url;
import org.example.rental_car.entities.User;
import org.example.rental_car.entities.Token;
import org.example.rental_car.request.LoginRequest;
import org.example.rental_car.request.PasswordResetRequest;
import org.example.rental_car.response.APIResponse;
import org.example.rental_car.response.JwtResponse;
import org.example.rental_car.security.JwtUtils;
import org.example.rental_car.security.UserDetails;
import org.example.rental_car.sendEmail.RegistrationCompleteEvent;
import org.example.rental_car.service.PasswordResetService;
import org.example.rental_car.service.TokenService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(Url.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;
    private final PasswordResetService passwordResetService;
    private final ApplicationEventPublisher publisher;

    @PostMapping(Url.LOGIN)
    public ResponseEntity<APIResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateTokenForUser(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
        return ResponseEntity.ok(new APIResponse("AUTHENTICATION_SUCCESS", jwtResponse));
    }

    @GetMapping(Url.VERIFY_EMAIL)
    public ResponseEntity<APIResponse> verifyEmail(@RequestParam("token")    String token) {
        String result =   tokenService.validateToken(token);
        return  switch (result){
            case "VALID" -> ResponseEntity.ok(new APIResponse("VALID_VERIFICATION_TOKEN", null));
            case "VERIFIED" -> ResponseEntity.ok(new APIResponse("TOKEN_ALREADY_VERIFIED", null));
            case "EXPIRED" ->
                    ResponseEntity.status(HttpStatus.GONE).body(new APIResponse("EXPIRED_TOKEN", null));
            case "INVALID" ->
                    ResponseEntity.status(HttpStatus.GONE).body(new APIResponse("INVALID_VERIFICATION_TOKEN", null));
            default -> ResponseEntity.internalServerError().body(new APIResponse("ERROR", null));

        } ;
    }


    @PutMapping(Url.RESEND_VERIFICATION_TOKEN)
    public ResponseEntity<APIResponse> resendVerificationToken(@RequestParam("token") String oldToken) {
        try {
            Token verificationToken = tokenService.generateNewToken(oldToken);
            User theUser = verificationToken.getUser();
            publisher.publishEvent(new RegistrationCompleteEvent(theUser));
            return ResponseEntity.ok(new APIResponse("NEW_VERIFICATION_TOKEN_SENT", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping(Url.REQUEST_PASSWORD_RESET)
    public ResponseEntity<APIResponse> requestPasswordReset(@RequestBody Map<String, String> requestBody){
        String email = requestBody.get("email");
        if (email == null || email.trim().isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new APIResponse("INVALID_EMAIL", null));
        }
        try {
            passwordResetService.requestPasswordReset(email);
            return ResponseEntity.
                    ok(new APIResponse("PASSWORD_RESET_EMAIL_SENT", null));
        }  catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new APIResponse(ex.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping(Url.RESET_PASSWORD)
    public  ResponseEntity<APIResponse> resetPassword(@RequestBody PasswordResetRequest request){
        String token = request.getToken();
        String newPassword = request.getNewPassword();
        if (token == null || token.trim().isEmpty() || newPassword == null || newPassword.trim().isEmpty()){
            return  ResponseEntity.badRequest().body(new APIResponse("MISSING_PASSWORD", null));
        }
        Optional<User> theUser = passwordResetService.findUserByPasswordResetToken(token);
        if (theUser.isEmpty()){
            return  ResponseEntity.badRequest().body(new APIResponse("INVALID_RESET_TOKEN", null));
        }
        User user = theUser.get();
        String message = passwordResetService.resetPassword(newPassword, user) ;
        return ResponseEntity.ok(new APIResponse(message, null));
    }
}
