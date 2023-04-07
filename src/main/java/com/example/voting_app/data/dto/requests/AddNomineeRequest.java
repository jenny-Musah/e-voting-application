package com.example.voting_app.data.dto.requests;

import lombok.Data;

@Data
public class AddNomineeRequest {
    private String nomineeMail;
    private long electionId;

}
