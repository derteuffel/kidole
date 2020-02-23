package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "poule")
public class Poule implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String libelle;

    @OneToMany(mappedBy = "poule")
    private List<Confrontation> confrontations;

    @ManyToMany(mappedBy = "poules")
    private Set<Site> sites;

    private ArrayList<Long> siteIds = new ArrayList<>();
}
