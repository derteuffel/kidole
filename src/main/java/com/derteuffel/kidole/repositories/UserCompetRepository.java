package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.UserCompet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCompetRepository extends JpaRepository<UserCompet, Long> {

    UserCompet findByUserIdAndCompetId(Long userId, Long competId);
    UserCompet findByAccreditation_Id(Long id);
}
