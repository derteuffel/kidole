package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
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

    @ManyToMany(mappedBy = "sites")
    private Set<Poule> poules;
}
