package com.example.voting_app.service.nomineeService;

import com.example.voting_app.data.dto.requests.NomineeDetailsRequest;
import com.example.voting_app.data.models.Nominee;
import jakarta.mail.MessagingException;

public interface NomineeService {

    Nominee addNominee(NomineeDetailsRequest nomineeDetailsRequest) throws MessagingException;
}
