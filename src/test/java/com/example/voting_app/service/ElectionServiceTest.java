package com.example.voting_app.service;

import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.requests.NomineeDetailsRequest;
import com.example.voting_app.data.models.Election;
import com.example.voting_app.service.electionService.ElectionService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jdk.jfr.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class ElectionServiceTest {

    @Autowired
    private ElectionService electionService;

    @Test
    public void testThatElectionCanBeCreated() throws MessagingException {
        NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
                "Class captain");
        NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
        creatElectionRequest.setStartAt("01-04-2023");
        creatElectionRequest.setEndsAt("02-04-2023");
        Election election = electionService.createElection(creatElectionRequest);
        assertEquals("class captain election", election.getElectionName());
    }
    @Test
    @Name("Test that election can not be created on an invalid day")
    public void testThatElectionCanBeCreatedOnInvalidDate() throws MessagingException {
        NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
                "Class captain");
        NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");
        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
        creatElectionRequest.setStartAt("30-03-2023");
        creatElectionRequest.setEndsAt("02-04-2023");
        assertThrows(InvalidDetails.class, () ->electionService.createElection(creatElectionRequest));
    }

}
