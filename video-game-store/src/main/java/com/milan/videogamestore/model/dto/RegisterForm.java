package com.milan.videogamestore.model.dto;

import lombok.Data;

@Data
public class RegisterForm {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String mobilePhone;
}
