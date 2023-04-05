package com.example.voting_app.data.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadPortfolioRequest {
    private String firstName;
    private String lastName;
    private String position;
    private String personalStatement;
    private String occupation;

}
