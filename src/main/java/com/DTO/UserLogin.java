package com.DTO;

import javax.validation.constraints.NotBlank;

public class UserLogin {
    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public UserLogin(){}

    public UserLogin(@NotBlank String userName, @NotBlank String password) {
        this.userName = userName;
        this.password = password;
    }
}
