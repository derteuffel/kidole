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
@Table(name = "poule_site")
public class PouleSite implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private Long pouleId;
    private Long siteId;

    private Site site;

    private String description;

}
