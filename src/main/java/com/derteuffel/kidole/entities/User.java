package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "user")
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
    private String country;
    private String region;
    private String ville;
    @OneToOne(mappedBy = "user")
    private Compte compte;


    @ManyToMany(mappedBy = "users")
    private Set<Team> teams;

    private ArrayList<Long> teamIds = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Accreditation> accreditations;
}
