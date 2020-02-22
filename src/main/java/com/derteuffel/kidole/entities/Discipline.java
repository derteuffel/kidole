package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "discipline")
public class Discipline implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String type;
    private int nbreEquipe;

    @ManyToOne
    @JsonIgnore
    private Competition competition;

    @OneToMany(mappedBy = "discipline")
    private List<Team> teams;
}
