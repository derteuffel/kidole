package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "confrontation")
@AllArgsConstructor @NoArgsConstructor
public class Confrontation implements Serializable {


    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Date dateConfrontation;
    private String resume;

    @ManyToMany
    @JoinTable(
            name = "confrontation_team",
            joinColumns = @JoinColumn(name = "confrontation_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @JsonIgnoreProperties("confrontations")
    private Set<Team> teams;

    @ManyToOne
    @JsonIgnore
    private Poule poule;

    @ManyToOne
    @JsonIgnore
    private Discipline discipline;



}
