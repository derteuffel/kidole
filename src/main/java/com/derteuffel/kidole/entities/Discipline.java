package com.derteuffel.kidole.entities;

import org.hibernate.annotations.Cache;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data @AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discipline")
public class Discipline implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String type;
    private int nbreEquipe;

    @ManyToOne
    @JsonIgnoreProperties("disciplines")
    private Competition competition;

    @OneToMany(mappedBy = "discipline")
    @OnDelete(action= OnDeleteAction.NO_ACTION)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Team> teams;
}
