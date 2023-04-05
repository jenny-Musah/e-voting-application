package com.example.voting_app.service;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.UploadPortfolioRequest;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.repository.NomineeRepository;
import com.example.voting_app.service.nomineeService.NomineeService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class NomineeServiceTest {

    @Autowired
    private NomineeService nomineeService;
    @Autowired
    private NomineeRepository nomineeRepository;

    @BeforeEach
    public void setUp(){
        nomineeRepository.deleteAll();
    }

    @Test
    public void testThatNomineeCanBeCreated() throws MessagingException {
        Nominee nominee = nomineeService.addNominee("jennymusah@67.gmail.com");
        assertEquals("jennymusah@67.gmail.com", nominee.getEmail());
    }

    @Test
    @Name("Test that nominee can not be added with wrong email")
    public void testThatNomineeCanNotBeAddedWithWrongDetails() throws MessagingException {
        assertThrows(InvalidDetails.class, () ->  nomineeService.addNominee("jennymusah@67"));
    }

    @Test
    @Name("Test that nominee can not login with invalid details")
    public void testThatNomineeCanLogin() throws MessagingException {;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId(1);
        loginRequest.setPassword("4e84cd2e-9NOMI#@");
       assertThrows( InvalidDetails.class,() ->nomineeService.login(loginRequest));
    }
    @Test
    @Name("Test that nominee can upload portfolio")
    public void testThatNomineeCanUploadPortfolio() throws MessagingException {
        Nominee nominee = nomineeService.addNominee("jennymusah@67.gmail.com");

        UploadPortfolioRequest uploadPortfolioRequest = new UploadPortfolioRequest(
                "Jennifer", "Musah", "Class captain","i love leadership", "Doctor"
        );
       assertEquals("Portfolio updated successfully", nomineeService.uploadPortfolio(uploadPortfolioRequest,nominee.getId()).getMessage());
    }
    @Test
    @Name("Test that nominee vote can be added")
    public void testThatNomineeVotesCanBeAdded() throws MessagingException {
        Nominee nominee = nomineeService.addNominee("jennymusah@67.gmail.com");
        UploadPortfolioRequest uploadPortfolioRequest = new UploadPortfolioRequest(
                "Jennifer", "Musah", "Class captain","i love leadership", "Doctor");
      nomineeService.uploadPortfolio(uploadPortfolioRequest,nominee.getId());
      nomineeService.addVote(nominee.getNomineePortfolio().getNomineeId());
      assertEquals(1, nominee.getNomineePortfolio().getVotes());
    }
    @Test
    @Name("Test that nominee without a portfolio can not be voted ")
    public void testThatNomineeWithOutPortfolioCanNotBeVoted() throws MessagingException {
        Nominee nominee = nomineeService.addNominee("jennymusah@67.gmail.com");
        assertThrows(InvalidDetails.class, () -> nomineeService.addVote(178930020L));
    }

}
