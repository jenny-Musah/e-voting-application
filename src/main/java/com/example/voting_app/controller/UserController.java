package com.example.voting_app.controller;


import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.dto.response.ApiResponse;
import com.example.voting_app.service.userService.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping()
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserRegisterRequest userRegisterRequest) throws MessagingException {
        return ResponseEntity.ok(userService.registerUser(userRegisterRequest));
    }

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.login(loginRequest));
    }



}
