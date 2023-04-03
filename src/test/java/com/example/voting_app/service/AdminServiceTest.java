package com.example.voting_app.service;


import com.example.voting_app.data.dto.requests.AdminLoginRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.requests.NomineeDetailsRequest;
import com.example.voting_app.data.dto.response.ElectionResponse;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.repository.AdminRepository;
import com.example.voting_app.service.adminService.AdminService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    public void setUp(){
        adminRepository.deleteAll();
    }

    @Test
    public void testThatAdminCanLogin(){
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest();
        adminLoginRequest.setLoginId(90078967900000L);
        adminLoginRequest.setPassword("IamTheAdmin#123@");
       LoginResponse response =  adminService.login(adminLoginRequest);
       assertEquals("Admin logged in successfully", response.getMessage());
    }
     @Test
     @Name("Test that admin can not login with incorrect loginId")
    public void testThatAdminCanNotLoginWithIncorrectLoginId(){
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest();
        adminLoginRequest.setLoginId(8967900000L);
        adminLoginRequest.setPassword("IamTheAdmin#123@");
        assertThrows(InvalidDetails.class, () -> adminService.login(adminLoginRequest));
    }
    @Test
     @Name("Test that admin can not login with incorrect password")
    public void testThatAdminCanNotLoginWithIncorrectPassword(){
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest();
        adminLoginRequest.setLoginId(90078967900000L);
        adminLoginRequest.setPassword("Admin#123@");
        assertThrows(InvalidDetails.class, () -> adminService.login(adminLoginRequest));
    }

    @Test
    @Name("Test that admin can create election")
    public void testThatAdminCanCreateElection() throws MessagingException {
        NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah69@gmail.com",
                "Class captain");
        NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
        creatElectionRequest.setStartAt("04-04-2023");
        creatElectionRequest.setEndsAt("04-04-2023");
        ElectionResponse electionResponse = adminService.createElection(creatElectionRequest);
        assertEquals("Successfully declared an election", electionResponse.getMessage());
    }
 @Test
    @Name("Test that admin can not create an election with an invalid date")
    public void testThatAdminCanNotCreateElectionWithAnInvalidDate() throws MessagingException {
        NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah99@gmail.com",
                "Class captain");
        NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
        creatElectionRequest.setStartAt("04-01-2023");
        creatElectionRequest.setEndsAt("04-01-2023");
       assertThrows(InvalidDetails.class, () -> adminService.createElection(creatElectionRequest));
    }
    @Test
    @Name("Test that admin can not create an election with an invalid details(password)")
    public void testThatAdminCanNotCreateElectionWithAnInvalidDetails() throws MessagingException {
        NomineeDetailsRequest nomineeDetailsRequest = new NomineeDetailsRequest("Jennifer", "Musah", "jennymusah9",
                "Class captain");
        NomineeDetailsRequest nomineeDetailsRequest2 = new NomineeDetailsRequest("Mary", "Jane", "maryjane@gmail.com", "Class captain");

        DeclareElectionRequest creatElectionRequest = new DeclareElectionRequest();
        creatElectionRequest.setElectionName("class captain election");
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest);
        creatElectionRequest.getListOfNominee().add(nomineeDetailsRequest2);
        creatElectionRequest.setStartAt("04-01-2023");
        creatElectionRequest.setEndsAt("04-01-2023");
       assertThrows(InvalidDetails.class, () -> adminService.createElection(creatElectionRequest));
    }


}
