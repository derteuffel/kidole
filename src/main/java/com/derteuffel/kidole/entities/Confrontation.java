package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "confrontation")
public class Confrontation implements Serializable {


    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String dateConfrontation;
    private String resume;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "confrontation_team", joinColumns = @JoinColumn(name = "confrontation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"))
    private Set<Team> teams;

    @ManyToOne
    @JsonIgnore
    private Poule poule;



}
