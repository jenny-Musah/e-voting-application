package com.example.voting_app.data.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NomineeDetailsRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String position;

}
