package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data @AllArgsConstructor
@NoArgsConstructor
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
    @JsonIgnoreProperties("sites")
    private Competition competition;

    @OneToMany(mappedBy = "site")
    private List<Confrontation> confrontations;
}
