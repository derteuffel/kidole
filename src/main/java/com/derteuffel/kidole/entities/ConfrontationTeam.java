package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "confrontation_team")
public class ConfrontationTeam implements Serializable {

    @Id
    @GeneratedValue
    private  Long id;
    private String decription;
}
