package com.example.voting_app.service;

import com.example.voting_app.data.dto.requests.NomineeDetailsRequest;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.repository.NomineeRepository;
import com.example.voting_app.service.nomineeService.NomineeService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NomineeServiceTest {

    @Autowired
    private NomineeService nomineeService;
    @Autowired NomineeRepository nomineeRepository;

    @BeforeEach
    public void setUp(){
        nomineeRepository.deleteAll();
    }

    @Test
    public void testThatNomineeCanBeCreated() throws MessagingException {
    NomineeDetailsRequest addNominee = new NomineeDetailsRequest(
                "Jenny", "Mercy","jennymusah@67.gmail.com",
                        "class captain");
        Nominee nominee = nomineeService.addNominee(addNominee);
        assertEquals("Jenny", nominee.getFirstName());
    }

    @Test
    @Name("Test that admin can not be added with wrong email")
    public void testThatAdminCanNotBeAddedWithWrongDetails() throws MessagingException {
        NomineeDetailsRequest addNominee = new NomineeDetailsRequest(
                "Jenny", "Mercy","jennymusah@67",
                "class captain");
        assertThrows(InvalidDetails.class, () ->  nomineeService.addNominee(addNominee));
    }
}
