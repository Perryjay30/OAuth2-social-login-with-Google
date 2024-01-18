package com.example.oauth2sociallogin.user.data.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String emailAddress;
    private String password;
}
