package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition,Long> {

    List<Competition> findAllByStatus(String status);
}
