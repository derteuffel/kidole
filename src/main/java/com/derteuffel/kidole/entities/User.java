package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@AllArgsConstructor @NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue
    private  Long id;

    private String lastname;
    private String firstname;
    private String email;
    private String phone;
    private String photo;
    private Date birthday;
    private String country;
    private String region;
    private String ville;

    @OneToOne(mappedBy = "user")
    private Compte compte;


    @ManyToMany
    @JoinTable(
            name = "user_team",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @JsonIgnoreProperties("users")
    private Set<Team> teams;

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Accreditation> accreditations;

    @ManyToMany
    @JoinTable(
            name = "user_compet",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "competition_id")
    )
    @JsonIgnoreProperties("users")
    private Set<Competition> competitions;
}
