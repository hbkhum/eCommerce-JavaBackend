package com.services;

import com.DTO.JwtDTO;
import com.DTO.NewUser;
import com.DTO.UserLogin;
import com.enums.RoleName;
import com.repositories.DataRepositories;
import com.security.JwtProvider;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.interfaces.entities.security.*;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {
    @Autowired
    private DataRepositories dataRepositories;

    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtProvider jwtProvider;

    public Optional<User> getByUserName(String userName){
        return dataRepositories.userRepository.findByUserName(userName);
    }

    public boolean existsByUserName(String userName){
        return dataRepositories.userRepository.existsByUserName(userName);
    }


    public  boolean existsByEmail(String email){
        return dataRepositories.userRepository.existsByEmail(email);
    }

    public void createUser(User user){
        dataRepositories.userRepository.save(user);
    }

    public String buildToken() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] randomBytes = new byte[32];
        secureRandomGenerator.nextBytes(randomBytes);
        byte[] valueDecoded = Base64.encodeBase64(randomBytes);
        return new String(valueDecoded);
    }
    public ResponseEntity<JwtDTO> createToken(UserLogin userLogin) throws NoSuchProviderException, NoSuchAlgorithmException {
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
