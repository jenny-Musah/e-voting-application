package com.example.voting_app.data.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse {

    private Object data;

    private boolean isSuccessful;

}
