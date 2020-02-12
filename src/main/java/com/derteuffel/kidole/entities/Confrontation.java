package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "confrontation")
public class Confrontation implements Serializable {


    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Date dateConfrontation;
    private String resume;

}
