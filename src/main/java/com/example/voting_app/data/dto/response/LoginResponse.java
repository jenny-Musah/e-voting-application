package com.example.voting_app.data.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginResponse {
    private long usersId;
    private int statusCode;
    private String message;
}
