package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "compte")
public class Compte implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private Boolean active;
    private String email;

    @OneToOne
    private User user;
}
