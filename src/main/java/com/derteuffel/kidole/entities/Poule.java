package com.derteuffel.kidole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data @AllArgsConstructor
@NoArgsConstructor
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

    private ArrayList<Long> siteIds = new ArrayList<>();
}
