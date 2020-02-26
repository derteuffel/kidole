package com.derteuffel.kidole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data @AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "competition")
public class Competition implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String code;
    private String name;
    private Date dateDebut;
    private int nbrePoule;
    private Date dateFin;
    private Date debutAccreditation;
    private Date finAccreditation;
    private String status;
    private String categorie;

    @OneToMany(mappedBy = "competition")
    private List<Discipline> disciplines;

    @OneToMany(mappedBy = "competition")
    private List<Site> sites;


}
