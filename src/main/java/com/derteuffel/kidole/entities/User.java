package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {

    @Id
    @GeneratedValue
    private  Long id;

    private String lastname;
    private String firstname;
    private String email;
    private String phone;
    private String photo;
    private String birthday;
    private String birthplace;
    private String country;
    private String region;
    private String ville;
    private ArrayList<Long> teamIds = new ArrayList<>();
    @OneToOne(mappedBy = "user")
    private Compte compte;
    @OneToMany(mappedBy = "user")
    private List<Accreditation> accreditations;

    @ManyToMany(mappedBy = "users")
    private Set<Team> teams;



}
