package com.example.voting_app.utils;

import com.example.voting_app.data.dto.response.ApiResponse;

public class GeneratedResponse {


    public static ApiResponse okResponse(Object data){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccessful(true);
        apiResponse.setData(data);
        return apiResponse;
    }
 public static ApiResponse badResponse(Object data){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccessful(false);
        apiResponse.setData(data);
        return apiResponse;
    }

}
