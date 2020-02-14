package com.derteuffel.kidole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "compet_discipline")
@AllArgsConstructor @NoArgsConstructor
public class CompetDiscipline implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
