package com.derteuffel.kidole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data @AllArgsConstructor @NoArgsConstructor
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
    private ArrayList<Long> competIds = new ArrayList<>();




}
