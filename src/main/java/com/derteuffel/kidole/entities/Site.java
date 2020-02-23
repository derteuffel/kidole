package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "site")
public class Site implements Serializable {

    @Id
    @GeneratedValue
    private  Long id;
    private String name;
    private String libelle;

    @ManyToMany@JoinTable(
            name = "poule_site",
            joinColumns = @JoinColumn(name = "poule_id"),
            inverseJoinColumns = @JoinColumn(name = "site_id"))
    private Set<Poule> poules;

    @ManyToOne
    @JsonIgnore
    private Competition competition;

    @OneToMany(mappedBy = "site")
    private List<Confrontation> confrontations;
}
