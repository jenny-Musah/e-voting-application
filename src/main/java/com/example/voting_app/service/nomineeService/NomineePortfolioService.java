package com.example.voting_app.service.nomineeService;

import com.example.voting_app.data.dto.requests.UploadPortfolioRequest;
import com.example.voting_app.data.models.NomineePortfolio;

public interface NomineePortfolioService {

    void addVote(long nomineeId);

    NomineePortfolio uploadPortfolio(UploadPortfolioRequest uploadPortfolioRequest);

    NomineePortfolio findPortfolio(long nomineeId);

}
