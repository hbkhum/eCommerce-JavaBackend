package com.services;

import com.DTO.JwtDTO;
import com.DTO.NewUser;
import com.DTO.UserLogin;
import com.enums.RoleName;
import com.interfaces.entities.security.Role;
import com.interfaces.entities.security.User;
import com.repositories.DataRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RoleService {
    @Autowired
    private DataRepositories dataRepositories;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<Role> getByRoleName(RoleName roleName){
        return dataRepositories.roleRepository.findByRoleName(roleName);
    }

    public User assignRole(NewUser newUser){
        User user =
                new User(newUser.getName(), newUser.getUserName(), newUser.getEmail(),
                        passwordEncoder.encode(newUser.getPassword()));

        Set<String> rolesStr = newUser.getRoles();
        Set<Role> roles = new HashSet<>();
        for (String role : rolesStr) {
            switch (role) {
                case "admin":
                    Role roleAdmin = getByRoleName(RoleName.ROLE_ADMIN).get();
                    roles.add(roleAdmin);
                    break;
                default:
                    Role rolUser = getByRoleName(RoleName.ROLE_USER).get();
                    roles.add(rolUser);
            }
        }
        user.setRoles(roles);
        return user;


    }
}
