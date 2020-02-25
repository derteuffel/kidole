package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Officiciel extends User {

    private String type;
    private String equipe;
    private String fonction;


}
