package com.derteuffel.kidole.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "user_compet")
public class UserCompet implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String description;

    private Long userId;
    private Long competId;

    @OneToOne
    private Accreditation accreditation;
}
