package com.derteuffel.kidole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "compte")
@AllArgsConstructor @NoArgsConstructor
public class Compte implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private Boolean active;

    @OneToOne
    private User user;
}
