package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Arbitre;
import com.derteuffel.kidole.entities.Comite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComiteRepository extends JpaRepository<Comite,Long> {

    List<Comite> findAllByCountry(String country);
    List<Comite> findAllByRegion(String region);
    List<Comite> findAllByVille(String ville);
    Optional<Comite> findByFirstnameOrLastname(String firstname, String lastname);
    List<Comite> findAllByFonction(String fonction);
}
