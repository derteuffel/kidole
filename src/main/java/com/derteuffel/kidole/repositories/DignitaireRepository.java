package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Comite;
import com.derteuffel.kidole.entities.Dignitaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DignitaireRepository extends JpaRepository<Dignitaire,Long> {

    List<Dignitaire> findAllByCountry(String country);
    List<Dignitaire> findAllByRegion(String region);
    List<Dignitaire> findAllByVille(String ville);
    Optional<Dignitaire> findByFirstnameOrLastname(String firstname, String lastname);
    List<Dignitaire> findAllByTitre(String titre);
}
