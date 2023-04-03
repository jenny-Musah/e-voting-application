package com.example.voting_app.data.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
public class NomineePortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long nomineeId;
    private String firstName;
    private String lastName;
    private String position;
    private long votes;
    private String personalStatement;
    private String occupation;
}
