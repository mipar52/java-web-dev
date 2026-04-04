package com.milan.videogamestore.model.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
