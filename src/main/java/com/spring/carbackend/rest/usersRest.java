package com.spring.carbackend.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.carbackend.DTO.UserAddDTO;
import com.spring.carbackend.DTO.UserEditDTO;
import com.spring.carbackend.entities.RoleOfUser;
import com.spring.carbackend.entities.Users;
import com.spring.carbackend.repository.RoleUserRepository;
import com.spring.carbackend.repository.UsersRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/")
public class usersRest {

    final UsersRepository usersRepository;
    final RoleUserRepository roleUserRepository;

    public usersRest(UsersRepository usersRepository, RoleUserRepository roleUserRepository) {
        this.usersRepository = usersRepository;
        this.roleUserRepository = roleUserRepository;
    }


    @PostMapping("/register")
    public String addUser(@RequestBody UserAddDTO userAddDTO) {
        Users users = Users.builder()
                .email(userAddDTO.getEmail())
                .address(userAddDTO.getAddress())
                .name(userAddDTO.getName())
                .password(userAddDTO.getPassword())
                .phoneNo(userAddDTO.getPhoneNo())
                .address(userAddDTO.getAddress())
                .dateOfBirth(userAddDTO.getDateOfBirth())
                .nationalIdNo(userAddDTO.getNationalIdNo())
                .wallet(userAddDTO.getWallet())
                .build();
        usersRepository.save(users);

        RoleOfUser role = RoleOfUser.builder()
                .user(users)
                .roleEnum(userAddDTO.getRoleEnum())
                .build();
        roleUserRepository.save(role);

        List<RoleOfUser> roles = new ArrayList<>();
        roles.add(role);
        users.setRoles(roles);
        usersRepository.save(users);

        return "User registered successfully";
    }

    @PutMapping("/editUser")
    public String editUser(
            @RequestBody UserEditDTO userEditDTO
    ){
        Users users = usersRepository.findById(userEditDTO.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        users.setName(userEditDTO.getName());
        users.setEmail(userEditDTO.getEmail());
        users.setPhoneNo(userEditDTO.getPhoneNo());
        users.setAddress(userEditDTO.getAddress());
        users.setDateOfBirth(userEditDTO.getDateOfBirth());
        users.setNationalIdNo(userEditDTO.getNationalIdNo());
        users.setWallet(userEditDTO.getWallet());
        users.setDrivingLicense(userEditDTO.getDrivingLicense());
        users.setPassword(userEditDTO.getPassword());
        usersRepository.save(users);
        return "User successfully";
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(
            @RequestParam(value = "id") int id
    ){
        Users users = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        RoleOfUser role = roleUserRepository.findByUser(users);
        roleUserRepository.delete(role);
        usersRepository.delete(users);
        return "delete success";
    }

}
