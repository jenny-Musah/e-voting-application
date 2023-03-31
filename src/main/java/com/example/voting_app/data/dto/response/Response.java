package com.example.voting_app.data.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Response {
    private long loginId;
    private int statusCode;
    private String message;
}
