package com.example.voting_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany
    private List<Nominee> listOfNominee = new ArrayList<>();
    @OneToMany
    private List<VoterCard> listOfVoters = new ArrayList<>();
    private String electionName;
    private LocalDate startAt;
    private LocalDate endAt;
    private boolean isActivated;
}
