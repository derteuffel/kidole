package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Entraineur;
import com.derteuffel.kidole.entities.Sparing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EntraineurRepository extends JpaRepository<Entraineur, Long> {

    List<Entraineur> findAllByCountry(String country);
    List<Entraineur> findAllByRegion(String region);
    List<Entraineur> findAllByVille(String ville);
    Set<Entraineur> findAllByTeams_Id(Long id);
    Optional<Entraineur> findByFirstnameOrLastname(String firstname, String lastname);
    List<Entraineur> findAllByDiscipline(String discipline);
    List<Entraineur> findAllByEquipe(String equipe);
    List<Sparing> findAllByCategory(String category);
}
