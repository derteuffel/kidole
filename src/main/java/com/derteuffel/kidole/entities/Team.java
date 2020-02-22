package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "team")
public class Team implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String libelle;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "team_user", joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "team_confrontation", joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "confrontation_id", referencedColumnName = "id"))
    private Set<Confrontation> confrontations;

    @ManyToOne
    @JsonIgnore
    private Discipline discipline;
}
