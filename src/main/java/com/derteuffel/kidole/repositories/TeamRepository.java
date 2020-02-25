package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {

    List<Team> findAllByDiscipline_Id(Long id);
    Optional<Team> findByDiscipline_Id(Long id);
    Set<Team> findAllByUsers_Id(Long id);
}
