package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "site")
@AllArgsConstructor @NoArgsConstructor
public class Site implements Serializable {

    @Id
    @GeneratedValue
    private  Long id;
    private String name;
    private String libelle;

    @ManyToMany(mappedBy = "sites")
    @JsonIgnoreProperties("sites")
    private Set<Poule> poules;
}
