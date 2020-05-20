package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Fiche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FicheRepository extends JpaRepository<Fiche,Long> {

    Fiche findByAccreditation_Id(Long id);
}
