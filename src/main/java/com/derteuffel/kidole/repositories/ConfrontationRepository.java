package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Confrontation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ConfrontationRepository extends JpaRepository<Confrontation,Long> {

    List<Confrontation> findAllByPoule_Id(Long id);
    List<Confrontation> findAllByDateConfrontation(String date);
    List<Confrontation> findAllByHeureConfrontation(String heure);
    List<Confrontation> findAllBySite_Id(Long id);
    Set<Confrontation> findAllByTeams_Id(Long id);
}
