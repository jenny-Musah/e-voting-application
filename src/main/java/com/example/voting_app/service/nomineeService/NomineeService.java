package com.example.voting_app.service.nomineeService;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.NomineeDetailsRequest;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import com.example.voting_app.data.models.Nominee;
import jakarta.mail.MessagingException;

public interface NomineeService {

    Nominee addNominee(NomineeDetailsRequest nomineeDetailsRequest) throws MessagingException;

    void addVote(long nomineeId);

    LoginResponse login(LoginRequest loginRequest);
}
