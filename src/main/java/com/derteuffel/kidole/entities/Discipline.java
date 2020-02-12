package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "discipline")
public class Discipline implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String type;
}
