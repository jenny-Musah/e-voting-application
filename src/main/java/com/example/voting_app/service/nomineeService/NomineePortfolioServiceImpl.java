package com.example.voting_app.service.nomineeService;

import com.example.voting_app.data.dto.requests.UploadPortfolioRequest;
import com.example.voting_app.data.models.NomineePortfolio;
import com.example.voting_app.data.repository.NomineePortfolioRepository;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class NomineePortfolioServiceImpl implements NomineePortfolioService{
    @Autowired
    private NomineePortfolioRepository nomineePortfolioRepository;

    @Override
    public void addVote(long nomineeId) {
        NomineePortfolio nomineePortfolio  = nomineePortfolioRepository.findNomineeByNomineeId(nomineeId).orElseThrow(()-> new InvalidDetails("Nominee Id is invalid"));
        nomineePortfolio.setVotes(nomineePortfolio.getVotes() + 1);
        nomineePortfolioRepository.save(nomineePortfolio);
    }

    @Override public NomineePortfolio uploadPortfolio(UploadPortfolioRequest uploadPortfolioRequest) {
        NomineePortfolio nomineePortfolio = new NomineePortfolio();
        nomineePortfolio.setNomineeId(generateId());
        nomineePortfolio.setFirstName(uploadPortfolioRequest.getFirstName());
        nomineePortfolio.setLastName(uploadPortfolioRequest.getLastName());
        nomineePortfolio.setPosition(uploadPortfolioRequest.getPosition());
        nomineePortfolio.setOccupation(uploadPortfolioRequest.getOccupation());
        nomineePortfolio.setPersonalStatement(uploadPortfolioRequest.getPersonalStatement());
      return   nomineePortfolioRepository.save(nomineePortfolio);
    }

    @Override public NomineePortfolio findPortfolio(long nomineeId) {
        return nomineePortfolioRepository.findNomineeByNomineeId(nomineeId).orElseThrow(() -> new InvalidDetails("Nominee has not yet upload a portfolio"));
    }

    private long generateId(){
        Random random = new Random();
        return Math.abs(random.nextLong());
    }

}
