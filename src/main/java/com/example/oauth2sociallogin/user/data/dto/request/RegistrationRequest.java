package com.example.oauth2sociallogin.user.data.dto.request;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;

    public String getPassword() {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
