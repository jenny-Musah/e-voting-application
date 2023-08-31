package com.example.voting_app.service;

import com.example.voting_app.data.dto.requests.AddNomineeRequest;
import com.example.voting_app.data.dto.requests.AddVoteRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.requests.UploadPortfolioRequest;
import com.example.voting_app.data.dto.response.ElectionResponse;
import com.example.voting_app.data.dto.response.UploadPortfolioResponse;
import com.example.voting_app.data.models.Election;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.repository.ElectionRepository;

import com.example.voting_app.service.electionService.ElectionService;
import com.example.voting_app.service.nomineeService.NomineeService;
import com.example.voting_app.utils.ElectionConstant;
import com.example.voting_app.utils.exceptions.ElectionException;
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
    @Autowired
    private NomineeService nomineeService;

    private Election election;
    @BeforeEach
    public void setUp(){

        electionRepository.deleteAll();

        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add( "jennymusah99@gmail.com");
        creatElectionRequest.getListOfNominee().add("maryjan344@gmail.com");
        creatElectionRequest.setStartAt("10-12-2023");
        creatElectionRequest.setEndsAt("10-12-2023");
       election = (Election) electionService.createElection(creatElectionRequest).getData();

    }


    @Test
    @Name("Test that election can be created")
    public void testThatElectionCanBeCreated(){
        assertEquals("class captain election", election.getElectionName());
    }
    @Test
    @Name("Test that election can not be created on an invalid day")
    public void testThatElectionCanNotBeCreatedWithInvalidDate() throws MessagingException {
        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add( "jennymusah99@gmail.com");
        creatElectionRequest.getListOfNominee().add("maryjan344@gmail.com");
        creatElectionRequest.setStartAt("30-03-2023");
        creatElectionRequest.setEndsAt("02-04-2023");
        assertThrows(InvalidDetails.class, () ->electionService.createElection(creatElectionRequest));
    }

    @Test
    @Name("Test that app users can not vote when election is not activated")
    public void testThatAppUsersCanNotVoteWhenElectionIsNotActive(){
        AddVoteRequest addVoteRequest = new AddVoteRequest();
        UploadPortfolioRequest uploadPortfolioRequest = new UploadPortfolioRequest(
                "Jennifer", "Musah", "Class captain","i love leadership", "Doctor"
        );
        Nominee nominee = (Nominee) nomineeService.uploadPortfolio(uploadPortfolioRequest,election.getListOfNominee().get(0).getId()).getData();
        addVoteRequest.setNomineeId(nominee.getNomineePortfolio().getNomineeId()); addVoteRequest.setElectionId(election.getId());
      assertThrows(ElectionException.class, () -> electionService.addVote(addVoteRequest, election.getListOfNominee().get(0).getUser().getId()));
    }
    @Test
    @Name("Test that nominee can not be added to twice to a particular election")
    public void testThatNomineeCanNotBeAddedTwiceToOneElection() throws MessagingException {
        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add("maryjan344@gmail.com");
        creatElectionRequest.getListOfNominee().add("jennymusah99@gmail.com");
        creatElectionRequest.getListOfNominee().add("jennymusah99@gmail.com");
        creatElectionRequest.setStartAt("08-12-2023");
        creatElectionRequest.setEndsAt("09-12-2023");
        assertThrows(InvalidDetails.class,() -> electionService.createElection(creatElectionRequest));
    }
    @Test
    @Name("Test that app users can not vote for nominee with no portfolio")
    public void testThatAppUsersCanNotVoteUserWithNoPortfolio(){
        election.setActivated(true);
        AddVoteRequest addVoteRequest = new AddVoteRequest();

        addVoteRequest.setNomineeId(90L); addVoteRequest.setElectionId(election.getId());
        assertThrows(ElectionException.class, () ->  electionService.addVote(addVoteRequest, election.getListOfNominee().get(0).getUser().getId()));
    }

  @Test
  @Name("Test app users can vote for a nominee when the nominee has uploaded portfolio and election is activated")
  public void testThatAppUsersCanVoteWhenElectionIsActivated(){
      election.setActivated(true);
      AddVoteRequest addVoteRequest = new AddVoteRequest();
      UploadPortfolioRequest uploadPortfolioRequest = new UploadPortfolioRequest(
              "Jennifer", "Musah", "Class captain","i love leadership", "Doctor"
      );
      Nominee nominee = (Nominee) nomineeService.uploadPortfolio(uploadPortfolioRequest,election.getListOfNominee().get(0).getId()).getData();
      addVoteRequest.setNomineeId(nominee.getNomineePortfolio().getNomineeId()); addVoteRequest.setElectionId(election.getId());
      election =(Election) electionService.addVote(addVoteRequest, election.getListOfNominee().get(1).getUser().getId()).getData();
      assertEquals(1, election.getListOfVoters().size());
  }
@Test
@Name("Test that nominee vote counts increases per vote")
  public void testThatNomineeVotesIncreasePerVote() {
    election.setActivated(true);
    AddVoteRequest addVoteRequest = new AddVoteRequest();
    UploadPortfolioRequest uploadPortfolioRequest = new UploadPortfolioRequest(
            "Jennifer", "Musah", "Class captain","i love leadership", "Doctor"
    );
    Nominee nominee = (Nominee) nomineeService.uploadPortfolio(uploadPortfolioRequest,election.getListOfNominee().get(0).getId()).getData();
    addVoteRequest.setNomineeId(nominee.getNomineePortfolio().getNomineeId()); addVoteRequest.setElectionId(election.getId());
    election = (Election) electionService.addVote(addVoteRequest,election.getListOfNominee().get(1).getUser().getId()).getData();
    assertEquals(1,election.getListOfNominee().get(0).getNomineePortfolio().getVotes());
  }

  @Test
  @Name("Test that app users can only vote once per election")
    public void testAppUsersCanOnlyVoteOnceInElection(){
      election.setActivated(true);
      AddVoteRequest addVoteRequest = new AddVoteRequest();
      UploadPortfolioRequest uploadPortfolioRequest = new UploadPortfolioRequest(
              "Jennifer", "Musah", "Class captain","i love leadership", "Doctor"
      );
      Nominee nominee =(Nominee) nomineeService.uploadPortfolio(uploadPortfolioRequest, election.getListOfNominee().get(1).getId()).getData();
      addVoteRequest.setNomineeId(nominee.getNomineePortfolio().getNomineeId()); addVoteRequest.setElectionId(election.getId());
      election = (Election) electionService.addVote(addVoteRequest, election.getListOfNominee().get(1).getUser().getId()).getData();
      assertThrows(InvalidDetails.class, () -> electionService.addVote(addVoteRequest, election.getListOfNominee().get(1).getUser().getId()));
  }

  @Test
  @Name("Test that app users can not vote with invalid election id")
    public void testThatAppUserCanNotVoteWithInvalidElectionId() {
      election.setActivated(true);
      AddVoteRequest addVoteRequest = new AddVoteRequest();
      UploadPortfolioRequest uploadPortfolioRequest = new UploadPortfolioRequest(
              "Jennifer", "Musah", "Class captain","i love leadership", "Doctor"
      );
      Nominee nominee =(Nominee) nomineeService.uploadPortfolio(uploadPortfolioRequest, election.getListOfNominee().get(1).getId()).getData();
      addVoteRequest.setNomineeId(nominee.getNomineePortfolio().getNomineeId()); addVoteRequest.setElectionId(234445L);
      assertThrows(InvalidDetails.class, () -> electionService.addVote(addVoteRequest, nominee.getUser().getId()));
  }
    @Test
    @Name("Test that app users can not vote with invalid nomineeId")
    public void testThatAppUserCanNotVoteWithInvalidNomineeId(){
        election.setActivated(true);
        AddVoteRequest addVoteRequest = new AddVoteRequest();
        addVoteRequest.setNomineeId(0); addVoteRequest.setElectionId(election.getId());

        assertThrows(ElectionException.class, () -> electionService.addVote(addVoteRequest, election.getListOfNominee().get(1).getUser().getId()));
    }
    @Test
    @Name("Test nominee can be added to an existing election")
    public void testThatAdminCanAddNomineeToElection() throws MessagingException {
        AddNomineeRequest addNomineeRequest = new AddNomineeRequest();
        addNomineeRequest.setNomineeMail("Funke@gmaul.com");
        addNomineeRequest.setElectionId(election.getId());
        election = (Election)  electionService.addNominee(addNomineeRequest).getData();
        assertEquals( 3, election.getListOfNominee().size());
    }

     @Test
    @Name("Test nominee can be added can not be added two times")
    public void testThatAdminCanNotAddANomineeTwoTimes() {
         AddNomineeRequest addNomineeRequest = new AddNomineeRequest();
         addNomineeRequest.setNomineeMail("jennymusah99@gmail.com");
         addNomineeRequest.setElectionId(election.getId());
         assertThrows(InvalidDetails.class, () -> electionService.addNominee(addNomineeRequest));
     }
}
