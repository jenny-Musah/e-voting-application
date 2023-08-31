package com.example.voting_app.service.userService;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.dto.response.ApiResponse;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import com.example.voting_app.data.models.Roles;
import com.example.voting_app.data.models.User;
import jakarta.mail.MessagingException;

public interface UserService {

    ApiResponse registerUser(UserRegisterRequest userRegisterRequest) throws MessagingException;
    ApiResponse login(LoginRequest loginRequest);

    User createAdmin(UserRegisterRequest userRegisterRequest, long loginId);

    User createUser(UserRegisterRequest userRegisterRequest, Roles nominee);

    User findUserByEmail(String nomineeEmails);
}
