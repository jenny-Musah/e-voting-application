package com.example.voting_app.service.userService;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import jakarta.mail.MessagingException;

public interface UserService {

    Response registerUser(UserRegisterRequest userRegisterRequest) throws MessagingException;
    LoginResponse login(LoginRequest loginRequest);
}
