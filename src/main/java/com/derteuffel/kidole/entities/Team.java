package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

@Data @AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team")
public class Team implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String libelle;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "team_user", joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users;

    private ArrayList<Long> confrontaionsIds = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    private Discipline discipline;
}
