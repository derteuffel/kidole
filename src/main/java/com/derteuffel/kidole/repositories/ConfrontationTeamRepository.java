package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.ConfrontationTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfrontationTeamRepository extends JpaRepository<ConfrontationTeam, Long> {
}
