package org.example.rental_car.data;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rental_car.entities.CarOwner;
import org.example.rental_car.entities.Customer;
import org.example.rental_car.entities.Role;
import org.example.rental_car.entities.User;
import org.example.rental_car.repository.RoleRepository;
import org.example.rental_car.repository.UserRepository;
import org.example.rental_car.service.RoleService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class DefaultDataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles =  Set.of("ROLE_CUSTOMER", "ROLE_OWNER");
        createDefaultRoleIfNotExits(defaultRoles);
    }

    private void createDefaultRoleIfNotExits(Set<String> roles){
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role:: new).forEach(roleRepository::save);
    }


}
