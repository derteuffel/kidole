package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Arbitre;
import com.derteuffel.kidole.entities.Athlete;
import com.derteuffel.kidole.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ArbitreRepository extends JpaRepository<Arbitre,Long> {

    List<Arbitre> findAllByCountry(String country);
    List<Arbitre> findAllByRegion(String region);
    List<Arbitre> findAllByVille(String ville);
    Optional<Arbitre> findByFirstnameOrLastname(String firstname, String lastname);
    List<Arbitre> findAllByDiscipline(String discipline);
    List<Arbitre> findAllByCategory(String category);
}
