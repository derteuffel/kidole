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
@Table(name = "user_compet")
public class UserCompet implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private Long competitionId;

    private Competition competition;

    private String description;
}
