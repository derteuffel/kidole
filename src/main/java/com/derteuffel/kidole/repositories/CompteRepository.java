package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by user on 22/03/2020.
 */
@Repository
public interface CompteRepository extends JpaRepository<Compte,Long> {

    Compte findByUsername(String username);
}
