package com.example.voting_app.data.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long loginId;
    private String emailAddress;
    private String password;
    @OneToOne
    @Enumerated
    private VoterCard voterCard;
    @ElementCollection
    private List<Roles> usersRoles = new ArrayList<>();
}
