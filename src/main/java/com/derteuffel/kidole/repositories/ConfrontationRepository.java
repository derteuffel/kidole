package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Confrontation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfrontationRepository extends JpaRepository<Confrontation,Long> {

    List<Confrontation> findAllByPoule_Id(Long id);
    List<Confrontation> findAllByDiscipline_Id(Long id);
}
