package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Poule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PouleRepository extends JpaRepository<Poule, Long> {
}
