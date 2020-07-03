package com.services;

import com.enums.RoleName;
import com.interfaces.entities.security.Role;
import com.repositories.DataRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleService {
    @Autowired
    private DataRepositories dataRepositories;

    public Optional<Role> getByRoleName(RoleName roleName){
        return dataRepositories.roleRepository.findByRoleName(roleName);
    }
}
