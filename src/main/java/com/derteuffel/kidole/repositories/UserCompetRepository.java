package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.UserCompet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCompetRepository extends JpaRepository<UserCompet, Long> {
}
