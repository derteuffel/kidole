package com.derteuffel.kidole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "accreditation")
@AllArgsConstructor @NoArgsConstructor
public class Accreditation implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String status;
    private String date;
    private String description;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonIgnore
    private Competition competition;
}
