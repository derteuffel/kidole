package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Athlete;
import com.derteuffel.kidole.entities.Sparing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SparingRepository extends JpaRepository<Sparing,Long> {
    List<Sparing> findAllByCountry(String country);
    List<Sparing> findAllByRegion(String region);
    List<Sparing> findAllByVille(String ville);
    Optional<Sparing> findByFirstnameOrLastname(String firstname, String lastname);
    List<Sparing> findAllByDiscipline(String discipline);
    List<Sparing> findAllByManualite(String manualite);
    List<Sparing> findAllByCategory(String category);
}
