package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "competition")
public class Competition implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String code;
    @NotNull
    private String name;
    @NotNull
    private Date dateDebut;
    @NotNull
    private Date dateFin;
    @NotNull
    private Date debutAccreditation;
    @NotNull
    private Date finAccreditation;
    @NotNull
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
