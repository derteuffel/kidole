package com.derteuffel.kidole.entities;

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

    @ManyToMany(mappedBy = "disciplines")
    private Set<Competition> competitions;

    @OneToMany(mappedBy = "discipline")
    private List<Confrontation> confrontations;
}
