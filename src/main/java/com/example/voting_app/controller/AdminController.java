package com.example.voting_app.controller;

import com.example.voting_app.data.dto.requests.AdminLoginRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.service.adminService.AdminService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


}
