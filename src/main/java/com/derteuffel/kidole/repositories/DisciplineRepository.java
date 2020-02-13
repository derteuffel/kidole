package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline,Long> {

    List<Discipline> findAllByName(String name);
    List<Discipline> findAllByType(String type);
}
