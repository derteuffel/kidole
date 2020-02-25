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
public class Athlete extends User {
    private String equipeOrigin;
    private Float height;
    private Float weight;
    private String manualite;
    private String category;
    private String discipline;

}
