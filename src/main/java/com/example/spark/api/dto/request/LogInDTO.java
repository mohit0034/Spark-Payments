package com.example.spark.api.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LogInDTO {
    @NotNull
    private String username;
    @Size(min = 8)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
