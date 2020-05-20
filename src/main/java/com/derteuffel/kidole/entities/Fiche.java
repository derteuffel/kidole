package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "fiche")
public class Fiche implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

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
    private String category;
    private String discipline;
    private String equipeOrigin;
    private String height;
    private String weight;
    private String manualite;
    private String fonction;
    private String titre;
    private String equipe;
    private String type;
    private String description;

    @OneToOne
    private Accreditation accreditation;

}
