package com.DTO;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtDTO {
    private String token;
    private String bearer = "Bearer";
    private String userName;
    private String refreshToken;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtDTO(String token, String userName, String refreshToken,  Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.userName = userName;
        this.authorities = authorities;
        this.refreshToken=refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getRefreshToken(){
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken){
        this.refreshToken=refreshToken;
    }
}
