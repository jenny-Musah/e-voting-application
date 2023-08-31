package com.example.voting_app.data.dto.requests;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeclareElectionRequest {

    private String electionName;
    private List<String> listOfNominee;
    private String startAt;
    private String endsAt;
}
