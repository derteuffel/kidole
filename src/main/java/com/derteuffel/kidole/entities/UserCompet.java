package com.derteuffel.kidole.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "user_compet")
@AllArgsConstructor @NoArgsConstructor
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
