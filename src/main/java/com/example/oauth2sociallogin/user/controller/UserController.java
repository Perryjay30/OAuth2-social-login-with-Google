package com.example.oauth2sociallogin.user.controller;

import com.example.oauth2sociallogin.user.data.dto.request.LoginRequest;
import com.example.oauth2sociallogin.user.data.dto.request.RegistrationRequest;
import com.example.oauth2sociallogin.user.data.dto.request.VerifyOtpRequest;
import com.example.oauth2sociallogin.user.data.dto.response.LoginResponse;
import com.example.oauth2sociallogin.user.data.dto.response.Response;
import com.example.oauth2sociallogin.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"api/v1/user"})
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/signUp"})
    public String signUp(@RequestBody RegistrationRequest registrationRequest) {
        return this.userService.signUp(registrationRequest);
    }

    @PostMapping({"/createAccount/{emailAddress}"})
    public Response createAccount(@PathVariable String emailAddress, @RequestBody VerifyOtpRequest verifyOtpRequest) {
        return this.userService.createAccount(emailAddress, verifyOtpRequest);
    }

    @GetMapping({"/login"})
    //Social login with google: http://localhost:2024/login/oauth2/code/google
    //Social login with google: http://localhost:2024/login/oauth2/code/github
    //Social login with facebook: https://localhost:2024/oauth2/callback/facebook
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return this.userService.login(loginRequest);
    }
}

