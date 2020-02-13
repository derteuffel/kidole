package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    Compte findByUsername(String username);
}
