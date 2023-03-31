package com.example.voting_app.data.dto.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private long loginId;
    private String password;

}
