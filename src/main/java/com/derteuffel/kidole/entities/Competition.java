package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "competition")
public class Competition implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String code;
    private String name;
    private Date dateDebut;
    private Date dateFin;
    private Date debutAccreditation;
    private Date finAccreditation;
    private String status;

    @ManyToMany(mappedBy = "competitions")
    private Set<User> users;

    @ManyToMany
    @JoinTable(
            name = "compet_discipline",
            joinColumns = @JoinColumn(name = "compet_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private Set<Discipline> disciplines;
}
