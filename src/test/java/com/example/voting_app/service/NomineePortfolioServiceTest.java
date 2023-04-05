package com.example.voting_app.service;

import com.example.voting_app.data.dto.requests.UploadPortfolioRequest;
import com.example.voting_app.data.models.NomineePortfolio;
import com.example.voting_app.service.nomineeService.NomineePortfolioService;
import jdk.jfr.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class NomineePortfolioServiceTest {

    @Autowired
    private NomineePortfolioService nomineePortfolioService;

    @Test
    @Name("Test that nominee portfolio can be created")
    public void testThatNomineePortfolioCanBeCreate(){
        UploadPortfolioRequest uploadPortfolioRequest = new UploadPortfolioRequest(
                "Jennifer", "Musah", "Class captain","i love leadership", "Doctor"
        );
        NomineePortfolio nomineePortfolio = nomineePortfolioService.uploadPortfolio(uploadPortfolioRequest);
        assertEquals("Jennifer",nomineePortfolio.getFirstName());
    }
}
