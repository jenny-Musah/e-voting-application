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
    private long nomineeId;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String position;
    private long votes;
    @Enumerated
    private Roles roles;
    @OneToOne
    @Enumerated
    private VoterCard voterCard;
}
