package com.example.voting_app.service;

import com.example.voting_app.data.dto.requests.AddVoteRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.requests.NomineeDetailsRequest;
import com.example.voting_app.data.dto.response.ElectionResponse;
import com.example.voting_app.data.models.Election;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.repository.ElectionRepository;
import com.example.voting_app.data.repository.NomineeRepository;
import com.example.voting_app.service.electionService.ElectionService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class ElectionServiceTest {

    @Autowired
    private ElectionService electionService;

    @Autowired
    private ElectionRepository electionRepository;
    @BeforeEach
    public void setUp(){
        electionRepository.deleteAll();
    }

    @Test
    @Name("Test that election can Be Created")
    public void testThatElectionCanBeCreated() throws MessagingException {
        NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
                "Class captain");
        NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
        creatElectionRequest.setStartAt("05-04-2023");
        creatElectionRequest.setEndsAt("05-04-2023");
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

    @Test
    @Name("Test that app users can not vote when election is not activated")
    public void testThatAppUsersCanNotVoteWhenElectionIsNotActive() throws MessagingException {
        NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
                "Class captain");
        NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
        creatElectionRequest.setStartAt("08-04-2023");
        creatElectionRequest.setEndsAt("09-04-2023");
        Election election = electionService.createElection(creatElectionRequest);
        AddVoteRequest addVoteRequest = new AddVoteRequest();
        long nomineeID = 0;
        long userId = 0;
        for(Nominee nominee : election.getListOfNominee()){
          nomineeID = nominee.getNomineeId();
          userId = nominee.getId();
          break;
        }
      addVoteRequest.setNomineeId(nomineeID); addVoteRequest.setElectionId(election.getId());
      ElectionResponse response = electionService.addVote(addVoteRequest, userId);
      assertEquals("class captain election, as not yet been opened to voters", response.getMessage());
    }

  @Test
  @Name("Test app users can vote when ever election is activated")
  public void testThatAppUsersCanVoteWhenElectionIsActivated() throws MessagingException {
      NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
              "Class captain");
      NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

      DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
      creatElectionRequest.setElectionName("class captain election");
      creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
      creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
      creatElectionRequest.setStartAt("08-04-2023");
      creatElectionRequest.setEndsAt("09-04-2023");
      Election election = electionService.createElection(creatElectionRequest);
      election.setActivated(true);
      AddVoteRequest addVoteRequest = new AddVoteRequest();
      long nomineeID = 0;
      long userId = 0;
      for(Nominee nominee : election.getListOfNominee()){
          nomineeID = nominee.getNomineeId();
          userId = nominee.getId();
          break;
      }
      addVoteRequest.setNomineeId(nomineeID); addVoteRequest.setElectionId(election.getId());
      ElectionResponse response = electionService.addVote(addVoteRequest, userId);
      assertEquals("Thank you for voting Jennifer", response.getMessage());
  }
@Test
@Name("Test that nominee vote counts increases per vote")
  public void testThatNomineeVotesIncreasePerVote() throws MessagingException {
      NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
              "Class captain");
      NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

      DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
      creatElectionRequest.setElectionName("class captain election");
      creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
      creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
    creatElectionRequest.setStartAt("08-04-2023");
    creatElectionRequest.setEndsAt("09-04-2023");
      Election election = electionService.createElection(creatElectionRequest);
      election.setActivated(true);
      AddVoteRequest addVoteRequest = new AddVoteRequest();
      long nomineeID = 0;
      long userId = 0;
      long vote= 0;
      for(Nominee nominee : election.getListOfNominee()){
          nomineeID = nominee.getNomineeId();
          userId = nominee.getId();
          vote = nominee.getVotes();
          break;
      }
      assertEquals(0,vote);
      addVoteRequest.setNomineeId(nomineeID); addVoteRequest.setElectionId(election.getId());
        electionService.addVote(addVoteRequest, userId);
    for(Nominee nominee : election.getListOfNominee()){
        vote = nominee.getVotes();
        break;
    }
    assertEquals(1,vote);
  }

  @Test
  @Name("Test that app users can only vote once per election")
    public void testAppUsersCanOnlyVoteOnceInElection() throws MessagingException {
      NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
              "Class captain");
      NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

      DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
      creatElectionRequest.setElectionName("class captain election");
      creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
      creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
      creatElectionRequest.setStartAt("08-04-2023");
      creatElectionRequest.setEndsAt("09-04-2023");
      Election election = electionService.createElection(creatElectionRequest);
      election.setActivated(true);
      AddVoteRequest addVoteRequest = new AddVoteRequest();
      long nomineeID = 0;
      long  userId = 0;
      for(Nominee nominee : election.getListOfNominee()){
          nomineeID = nominee.getNomineeId();
          userId = nominee.getId();
          break;
      }
      addVoteRequest.setNomineeId(nomineeID); addVoteRequest.setElectionId(election.getId());
      ElectionResponse response = electionService.addVote(addVoteRequest, userId);
       assertEquals("Thank you for voting Jennifer", response.getMessage());
      long finalUserId = userId;
      assertThrows(InvalidDetails.class, () -> electionService.addVote(addVoteRequest, finalUserId));
  }

  @Test
  @Name("Test that app users can not vote with invalid election id")
    public void testThatAppUserCanNotVoteWithInvalidElectionId() throws MessagingException {
      NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
              "Class captain");
      NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

      DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
      creatElectionRequest.setElectionName("class captain election");
      creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
      creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
      creatElectionRequest.setStartAt("08-04-2023");
      creatElectionRequest.setEndsAt("09-04-2023");
      Election election = electionService.createElection(creatElectionRequest);
      election.setActivated(true);
      AddVoteRequest addVoteRequest = new AddVoteRequest();
      long nomineeID = 0;
      long  userId = 0;
      for(Nominee nominee : election.getListOfNominee()){
          nomineeID = nominee.getNomineeId();
          userId = nominee.getId();
          break;
      }
      addVoteRequest.setNomineeId(nomineeID); addVoteRequest.setElectionId(9085656934527L);
      long finalUserId = userId;
      assertThrows(InvalidDetails.class, () -> electionService.addVote(addVoteRequest, finalUserId));
  }
    @Test
    @Name("Test that app users can not vote with invalid nomineeid")
    public void testThatAppUserCanNotVoteWithInvalidNomineeId() throws MessagingException {
        NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
                "Class captain");
        NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
        creatElectionRequest.setStartAt("08-04-2023");
        creatElectionRequest.setEndsAt("09-04-2023");
        Election election = electionService.createElection(creatElectionRequest);
        election.setActivated(true);
        AddVoteRequest addVoteRequest = new AddVoteRequest();
        addVoteRequest.setNomineeId(0); addVoteRequest.setElectionId(election.getId());

        assertThrows(InvalidDetails.class, () -> electionService.addVote(addVoteRequest, 0));
    }

}
