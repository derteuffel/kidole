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
@Table(name = "user_team")
public class UserTeam implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private Long teamId;

    private Team team;

    private String description;
}
