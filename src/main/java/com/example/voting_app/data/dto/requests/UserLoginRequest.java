package com.example.voting_app.data.dto.requests;

import lombok.Data;

@Data
public class UserLoginRequest {
    private long loginId;
    private String password;

}
