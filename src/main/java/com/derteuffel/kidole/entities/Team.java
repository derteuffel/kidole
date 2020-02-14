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
@Table(name = "team")
@AllArgsConstructor @NoArgsConstructor
public class Team implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String libelle;

    @ManyToMany(mappedBy = "teams")
    @JsonIgnoreProperties("teams")
    private Set<User> users;

    @ManyToMany(mappedBy = "teams")
    @JsonIgnoreProperties("teams")
    private Set<Confrontation> confrontations;
}
