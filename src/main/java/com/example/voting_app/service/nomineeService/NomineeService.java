package com.example.voting_app.service.nomineeService;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.UploadPortfolioRequest;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.UploadPortfolioResponse;
import com.example.voting_app.data.models.Nominee;
import jakarta.mail.MessagingException;

public interface NomineeService {

    Nominee addNominee(String nomineeEmails) throws MessagingException;

    UploadPortfolioResponse uploadPortfolio(UploadPortfolioRequest uploadPortfolioRequest, long id);

    LoginResponse login(LoginRequest loginRequest);

    void addVote(long nomineeId);

    Nominee findNominee(String nomineeEmail);
}
