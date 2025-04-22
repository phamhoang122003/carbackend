package org.example.rental_car.controller;

import lombok.RequiredArgsConstructor;
import org.example.rental_car.common.Url;
import org.example.rental_car.dto.UserDto;
import org.example.rental_car.entities.User;
import org.example.rental_car.request.ChangePasswordRequest;
import org.example.rental_car.request.RegisterRequest;
import org.example.rental_car.request.UpdateUserRequest;
import org.example.rental_car.response.APIResponse;
import org.example.rental_car.sendEmail.RegistrationCompleteEvent;
import org.example.rental_car.service.ChangePasswordService;
import org.example.rental_car.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(Url.USER)
public class UserController {
    private final UserService userService;
    private final ChangePasswordService changePasswordService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping(Url.GET_BY_ID)
    public ResponseEntity<APIResponse> getUserById(@PathVariable long id) {
        UserDto user = userService.getUserDetails(id);
        return ResponseEntity.ok(new APIResponse("Found", user));
    }

    @PostMapping(Url.REGISTER)
    public ResponseEntity<APIResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        User user = userService.createUser(registerRequest);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user));
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return ResponseEntity.ok(new APIResponse("SUCCESS", userDto));
    }

    @PutMapping(Url.UPDATE_BY_ID)
    public ResponseEntity<APIResponse> updateUserById(@PathVariable int id, @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userService.updateUserById(id, updateUserRequest);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return ResponseEntity.ok(new APIResponse("UPDATE_SUCCESS", userDto));
    }

    @DeleteMapping(Url.DELETE_BY_ID)
    public ResponseEntity<APIResponse> deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new APIResponse("DELETE_SUCCESS", null));
    }

    @GetMapping(Url.GET_BY_EMAIL)
    public ResponseEntity<APIResponse> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new APIResponse("FOUND", user));
    }

    @GetMapping(Url.GET_ALL)
    public ResponseEntity<APIResponse> getAllUsers() {
        List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(new APIResponse("FOUND", userDtos));
    }

    @PutMapping(Url.CHANGE_PASSWORD)
    public ResponseEntity<APIResponse> changePassword(@PathVariable Long userId,
                                                      @RequestBody ChangePasswordRequest request) {
        try {
            changePasswordService.changePassword(userId, request);
            return ResponseEntity.ok(new APIResponse("SUCCESS", null));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new APIResponse(e.getMessage(), null));
        }catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }
}
