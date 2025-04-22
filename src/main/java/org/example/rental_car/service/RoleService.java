package org.example.rental_car.service;

import org.example.rental_car.entities.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role getRoleByName(String roleName);
    void saveRole(Role role);

    Set<Role> setUserRole(String role);
}
