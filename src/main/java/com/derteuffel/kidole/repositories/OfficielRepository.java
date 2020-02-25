package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Dignitaire;
import com.derteuffel.kidole.entities.Officiciel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfficielRepository extends JpaRepository<Officiciel,Long> {

    List<Officiciel> findAllByCountry(String country);
    List<Officiciel> findAllByRegion(String region);
    List<Officiciel> findAllByVille(String ville);
    Optional<Officiciel> findByFirstnameOrLastname(String firstname, String lastname);
    List<Officiciel> findAllByType(String type);
    List<Officiciel> findAllByEquipe(String equipe);
    List<Officiciel> findAllByFonction(String fonction);
}
