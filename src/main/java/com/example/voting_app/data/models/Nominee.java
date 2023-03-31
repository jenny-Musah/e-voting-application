package com.example.voting_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Nominee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long loginId;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String position;
    private long voteId;
    @Enumerated
    private Roles roles;
}
