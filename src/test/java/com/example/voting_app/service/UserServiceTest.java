package com.example.voting_app.service;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import com.example.voting_app.data.repository.UserRepository;
import com.example.voting_app.service.userService.UserService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
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
public class UserServiceTest {

    private UserRegisterRequest userRegisterRequest;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Name("Clears the database before each test")
    public void tearDown(){
        userRepository.deleteAll();
    }



    @Test
    @Name("Test that user can register")
    public void testThatUserCanRegister() throws MessagingException {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword("Jennymush#123");
        userRegisterRequest.setEmailAddress("jennymusah99@gmail.com");
        Response response = userService.registerUser(userRegisterRequest);
        assertEquals("Registration successful, check your mail for login details", response.getMessage());
    }

    @Test
    @Name("Test that user can not register with the same email")
    public void testThatUserCanNotRegisterWithTheSameEmailAddress() throws MessagingException {
        testThatUserCanRegister();
        userRegisterRequest.setPassword("Jennymush#123");
        userRegisterRequest.setEmailAddress("jennymusah99@gmail.com");
        assertThrows(InvalidDetails.class,() ->userService.registerUser(userRegisterRequest));
    }

    @Test
    @Name("Test that user can login")
    public void testThatUserCanLogin() throws MessagingException {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword("Jennymush#123");
        userRegisterRequest.setEmailAddress("jennymusah99@gmail.com");
        Response response = userService.registerUser(userRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId(response.getLoginId());
        loginRequest.setPassword("Jennymush#123");
        LoginResponse response2 = userService.login(loginRequest);
        assertEquals("Login successful", response2.getMessage());
    }
    @Test
    @Name("Test that user can not login with invalid login id")
    public void testThatUserCanNotLoginWithInvalidDetails() throws MessagingException {
        testThatUserCanRegister();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId(3451818290200l);
        loginRequest.setPassword("Jennymush#123");
       assertThrows(InvalidDetails.class, () -> userService.login(loginRequest));
    }

    @Test
    @Name("Test that user can not login with invalid password")
    public void testThatUserCanNotLoginWithInvalidPassword() throws MessagingException {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword("Jennymush#123");
        userRegisterRequest.setEmailAddress("jennymusah99@gmail.com");
        Response response = userService.registerUser(userRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId(response.getLoginId());
        loginRequest.setPassword("Jennymu");
        assertThrows(InvalidDetails.class, () -> userService.login(loginRequest));
    }


}
