package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "competition")
@AllArgsConstructor @NoArgsConstructor
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
    private String status;

    @ManyToMany(mappedBy = "competitions")
    @JsonIgnoreProperties("competitions")
    private Set<User> users;

    @ManyToMany
    @JoinTable(
            name = "compet_discipline",
            joinColumns = @JoinColumn(name = "compet_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    @JsonIgnoreProperties("competitions")
    private Set<Discipline> disciplines;

    @OneToMany(mappedBy = "competition")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Accreditation> accreditations;
}
