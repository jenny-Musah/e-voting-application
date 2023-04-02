package com.example.voting_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long loginId;
    private String password;
    @ElementCollection
    private List<Roles> adminRoles = new ArrayList<>();
    @OneToOne
    @Enumerated
    private VoterCard voterCard;
}
