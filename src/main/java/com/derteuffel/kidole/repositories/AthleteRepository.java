package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Athlete;
import com.derteuffel.kidole.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    List<Athlete> findAllByCountry(String country);
    List<Athlete> findAllByRegion(String region);
    List<Athlete> findAllByVille(String ville);
    Optional<Athlete> findByFirstnameOrLastname(String firstname, String lastname);
    List<Athlete> findAllByDiscipline(String discipline);
    List<Athlete> findAllByManualite(String manualite);
    List<Athlete> findAllByCategory(String category);
}
