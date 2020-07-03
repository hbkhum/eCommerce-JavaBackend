package com.services;

import com.repositories.DataRepositories;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.interfaces.entities.security.*;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private DataRepositories dataRepositories;

    public Optional<User> getByUserName(String userName){
        return dataRepositories.userRepository.findByUserName(userName);
    }

    public boolean existsByUserName(String userName){
        return dataRepositories.userRepository.existsByUserName(userName);
    }

    public  boolean existsByEmail(String email){
        return dataRepositories.userRepository.existsByEmail(email);
    }

    public void Save(User user){
        dataRepositories.userRepository.save(user);
    }
}
