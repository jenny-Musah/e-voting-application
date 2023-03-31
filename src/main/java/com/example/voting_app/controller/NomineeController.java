package com.example.voting_app.controller;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.service.nomineeService.NomineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/nominee")
public class NomineeController {

    @Autowired
    private NomineeService nomineeService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(nomineeService.login(loginRequest), HttpStatus.OK);
    }
}
