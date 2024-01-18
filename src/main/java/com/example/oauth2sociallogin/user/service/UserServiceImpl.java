package com.example.oauth2sociallogin.user.service;

import com.example.oauth2sociallogin.exceptions.CrustInterviewProjectException;
import com.example.oauth2sociallogin.notification.EmailService;
import com.example.oauth2sociallogin.otptoken.OtpToken;
import com.example.oauth2sociallogin.otptoken.OtpTokenRepository;
import com.example.oauth2sociallogin.otptoken.Token;
import com.example.oauth2sociallogin.security.JwtService;
import com.example.oauth2sociallogin.user.data.dto.request.LoginRequest;
import com.example.oauth2sociallogin.user.data.dto.request.RegistrationRequest;
import com.example.oauth2sociallogin.user.data.dto.request.SendOtpRequest;
import com.example.oauth2sociallogin.user.data.dto.request.VerifyOtpRequest;
import com.example.oauth2sociallogin.user.data.dto.response.LoginResponse;
import com.example.oauth2sociallogin.user.data.dto.response.Response;
import com.example.oauth2sociallogin.user.data.model.AuthProvider;
import com.example.oauth2sociallogin.user.data.model.Role;
import com.example.oauth2sociallogin.user.data.model.Status;
import com.example.oauth2sociallogin.user.data.model.User;
import com.example.oauth2sociallogin.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OtpTokenRepository otpTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, EmailService emailService, OtpTokenRepository otpTokenRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.otpTokenRepository = otpTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public String signUp(RegistrationRequest registrationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (userRepository.findByEmailAddress(registrationRequest.getEmailAddress()).isPresent()) {
            throw new RuntimeException("Email Address already exists!!");
        } else {
            User user = (User)objectMapper.convertValue(registrationRequest, User.class);
            user.setRole(Role.USER);
            user.setStatus(Status.UNVERIFIED);
            user.setAuthProvider(AuthProvider.LOCAL);
            userRepository.save(user);
            SendOtpRequest sendOtpRequest = new SendOtpRequest();
            sendOtpRequest.setEmail(registrationRequest.getEmailAddress());
            return this.sendOTP(sendOtpRequest);
        }
    }

    private String sendOTP(SendOtpRequest sendOtpRequest) {
        User existingUser = this.getExistingUser(sendOtpRequest.getEmail());
        return this.generateOtpToken(sendOtpRequest, existingUser);
    }

    private String generateOtpToken(SendOtpRequest sendOtpRequest, User existingUser) {
        String token = Token.generateToken(4);
        OtpToken otpToken = new OtpToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10L), existingUser);
        this.otpTokenRepository.save(otpToken);
        this.emailService.send(sendOtpRequest.getEmail(), this.emailService.buildEmail(existingUser.getFirstName(), token));
        return "Token successfully sent to your email. Please check.";
    }

    @Override
    public Response createAccount(String email, VerifyOtpRequest verifyOtpRequest) {
        this.verifyOTP(verifyOtpRequest);
        User existingUser = this.getExistingUser(email);
        this.userRepository.enableUser(Status.VERIFIED, existingUser.getEmailAddress());
        return new Response("User registration successful");
    }

    @Override
    public User getExistingUser(String email) {
        return (User)this.userRepository.findByEmailAddress(email).orElseThrow(() -> {
            return new CrustInterviewProjectException("User not found!!");
        });
    }

    public void verifyOTP(VerifyOtpRequest verifyOtpRequest) {
        OtpToken foundToken = (OtpToken)this.otpTokenRepository.findByToken(verifyOtpRequest.getToken()).orElseThrow(() -> {
            return new RuntimeException("Token doesn't exist");
        });
        if (foundToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        } else if (foundToken.getVerifiedAt() != null) {
            throw new RuntimeException("Token has been used");
        } else if (!Objects.equals(verifyOtpRequest.getToken(), foundToken.getToken())) {
            throw new RuntimeException("Incorrect token");
        } else {
            this.otpTokenRepository.setVerifiedAt(LocalDateTime.now(), verifyOtpRequest.getToken());
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User foundUser = this.getExistingUser(loginRequest.getEmailAddress());
        if (foundUser.getStatus() != Status.VERIFIED) {
            throw new RuntimeException("Customer is not verified");
        } else {
            Authentication authenticating = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword()));
            if (authenticating.isAuthenticated()) {
                String token = this.jwtService.generateToken(loginRequest.getEmailAddress());
                String loginMessage = "Login Successful!!";
                return new LoginResponse(loginMessage, token);
            } else {
                throw new UsernameNotFoundException("Invalid credentials!!");
            }
        }
    }
}

