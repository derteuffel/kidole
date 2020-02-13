package com.derteuffel.kidole.entities;

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

    @ManyToMany(mappedBy = "teams")
    private Set<User> users;

    @ManyToMany(mappedBy = "teams")
    private Set<Confrontation> confrontations;
}
