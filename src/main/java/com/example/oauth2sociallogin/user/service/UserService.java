package com.example.oauth2sociallogin.user.service;

import com.example.oauth2sociallogin.user.data.dto.request.LoginRequest;
import com.example.oauth2sociallogin.user.data.dto.request.RegistrationRequest;
import com.example.oauth2sociallogin.user.data.dto.request.VerifyOtpRequest;
import com.example.oauth2sociallogin.user.data.dto.response.LoginResponse;
import com.example.oauth2sociallogin.user.data.dto.response.Response;
import com.example.oauth2sociallogin.user.data.model.User;

public interface UserService {
    String signUp(RegistrationRequest registrationRequest);

    Response createAccount(String email, VerifyOtpRequest verifyOtpRequest);

    LoginResponse login(LoginRequest loginRequest);

    User getExistingUser(String email);
}
