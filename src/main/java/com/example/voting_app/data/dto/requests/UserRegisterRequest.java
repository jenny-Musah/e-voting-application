package com.example.voting_app.data.dto.requests;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String emailAddress;
    private String password;
}
