package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.CompetDiscipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetDisciplineRepository extends JpaRepository<CompetDiscipline,Long> {
}
