package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;

@Data
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Comite extends User {

    private String fonction;
}
