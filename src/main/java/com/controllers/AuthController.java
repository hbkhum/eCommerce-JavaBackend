package com.controllers;


import com.DTO.JwtDTO;
import com.DTO.NewUser;
import com.DTO.UserLogin;
import com.enums.RoleName;
import com.interfaces.entities.security.Role;
import com.interfaces.entities.security.User;
import com.security.JwtProvider;
import com.services.RoleService;
import com.services.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    JwtProvider jwtProvider;
    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) throws NoSuchProviderException, NoSuchAlgorithmException {
        if(bindingResult.hasErrors())
            return new ResponseEntity("campos vacíos o email inválido", HttpStatus.BAD_REQUEST);
        if(userService.existsByUserName(newUser.getUserName()))
            return new ResponseEntity("ese nombre ya existe", HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(newUser.getEmail()))
            return new ResponseEntity("ese email ya existe", HttpStatus.BAD_REQUEST);
        User user =
                new User(newUser.getName(), newUser.getUserName(), newUser.getEmail(),
                        passwordEncoder.encode(newUser.getPassword()));
        Set<String> rolesStr = newUser.getRoles();
        Set<Role> roles = new HashSet<>();
        for (String role : rolesStr) {
            switch (role) {
                case "admin":
                    Role roleAdmin = roleService.getByRoleName(RoleName.ROLE_ADMIN).get();
                    roles.add(roleAdmin);
                    break;
                default:
                    Role rolUser = roleService.getByRoleName(RoleName.ROLE_USER).get();
                    roles.add(rolUser);
            }
        }

        user.setRoles(roles);
        userService.Save(user);
        UserLogin userLogin = new UserLogin(user.getUserName(),newUser.getPassword());
        ResponseEntity<JwtDTO> jwt=createToken(userLogin);
        return  jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody UserLogin userLogin, BindingResult bindingResult) throws NoSuchProviderException, NoSuchAlgorithmException {

        if(bindingResult.hasErrors())
            return new ResponseEntity("campos vacíos o email inválido", HttpStatus.BAD_REQUEST);


        return  createToken(userLogin);

    }

    private String buildToken() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] randomBytes = new byte[32];
        secureRandomGenerator.nextBytes(randomBytes);
        byte[] valueDecoded = Base64.encodeBase64(randomBytes);
        return new String(valueDecoded);
    }

    private ResponseEntity<JwtDTO> createToken(UserLogin userLogin) throws NoSuchProviderException, NoSuchAlgorithmException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogin.getUserName(), userLogin.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDTO jwtDTO = new JwtDTO(jwt, userDetails.getUsername(),buildToken(), userDetails.getAuthorities());
        return new ResponseEntity<JwtDTO>(jwtDTO, HttpStatus.OK);
    }
}
