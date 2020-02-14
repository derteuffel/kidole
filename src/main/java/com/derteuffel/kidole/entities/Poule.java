package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "poule")
@AllArgsConstructor @NoArgsConstructor
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Poule implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String libelle;

    @OneToMany(mappedBy = "poule")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Confrontation> confrontations;

    @ManyToMany
    @JoinTable(
            name = "poule_site",
            joinColumns = @JoinColumn(name = "poule_id"),
            inverseJoinColumns = @JoinColumn(name = "site_id")
    )
    @JsonIgnoreProperties("poules")
    private Set<Site> sites;
}
