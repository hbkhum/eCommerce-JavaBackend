package com.controllers;


import com.DTO.JwtDTO;
import com.DTO.NewUser;
import com.DTO.UserLogin;
import com.interfaces.entities.security.User;
import com.services.DataServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    DataServices dataServices;

    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) throws NoSuchProviderException, NoSuchAlgorithmException {
        if(bindingResult.hasErrors())
            return new ResponseEntity("campos vacíos o email inválido", HttpStatus.BAD_REQUEST);
        if(dataServices.userService.existsByUserName(newUser.getUserName()))
            return new ResponseEntity("ese nombre ya existe", HttpStatus.BAD_REQUEST);
        if(dataServices.userService.existsByEmail(newUser.getEmail()))
            return new ResponseEntity("ese email ya existe", HttpStatus.BAD_REQUEST);
        User user = dataServices.roleService.assignRole(newUser);
        dataServices.userService.createUser(user);

        UserLogin userLogin = new UserLogin(user.getUserName(),newUser.getPassword());
        ResponseEntity<JwtDTO> jwt= dataServices.userService.createToken(userLogin);
        return  jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody UserLogin userLogin, BindingResult bindingResult) throws NoSuchProviderException, NoSuchAlgorithmException {

        if(bindingResult.hasErrors())
            return new ResponseEntity("campos vacíos o email inválido", HttpStatus.BAD_REQUEST);


        return  dataServices.userService.createToken(userLogin);

    }


}
