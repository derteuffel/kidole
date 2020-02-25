package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.print.DocFlavor;
import java.util.List;


@Data
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Arbitre extends User {

    private String category;
    private String discipline;

}
