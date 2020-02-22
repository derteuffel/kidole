package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline,Long> {

    List<Discipline> findAllByName(String name);
    List<Discipline> findAllByType(String type);
    List<Discipline> findAllByCompetition_Id(Long id);
    Optional<Discipline> findByCompetition_Id(Long id);
}
