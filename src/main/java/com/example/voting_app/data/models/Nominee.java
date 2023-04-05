package com.example.voting_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Nominee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long loginId;
    private String password;
    private String email;
    @Enumerated
    private Roles roles;
    @OneToOne
    @Enumerated
    private VoterCard voterCard;
    @OneToOne
    @Enumerated
    private NomineePortfolio nomineePortfolio;
}
