package com.derteuffel.kidole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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
    private Date dateFin;
    private Date debutAccreditation;
    private Date finAccreditation;
    private String category;
    private String status;
    private String type;
    private String categorie;
    private ArrayList<String> items =new ArrayList<>();

    @OneToMany(mappedBy = "competition")
    @OnDelete(action= OnDeleteAction.NO_ACTION)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Discipline> disciplines;

    @OneToMany(mappedBy = "competition")
    @OnDelete(action= OnDeleteAction.NO_ACTION)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Site> sites;


}
