package com.example.voting_app.data.dto.requests;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor
@NoArgsConstructor
public class AddVoteRequest {
        private long nomineeId;
        private long electionId;
}
