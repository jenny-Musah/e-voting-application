package com.example.voting_app.controller;


import com.example.voting_app.data.dto.requests.UploadPortfolioRequest;
import com.example.voting_app.data.dto.response.ApiResponse;
import com.example.voting_app.service.nomineeService.NomineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/nominee")
public class NomineeController {

    @Autowired
    private NomineeService nomineeService;

    @PostMapping("/portfolio")
    public ResponseEntity<ApiResponse> uploadPortfolio(@RequestBody UploadPortfolioRequest uploadPortfolioRequest,@RequestParam("id") long id){
        return ResponseEntity.ok(nomineeService.uploadPortfolio(uploadPortfolioRequest,id));
    }
}
