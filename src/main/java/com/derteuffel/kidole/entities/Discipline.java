package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "discipline")
@AllArgsConstructor @NoArgsConstructor
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Discipline implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String type;

    @ManyToMany(mappedBy = "disciplines")
    @JsonIgnoreProperties("disciplines")
    private Set<Competition> competitions;

    @OneToMany(mappedBy = "discipline")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Confrontation> confrontations;
}
