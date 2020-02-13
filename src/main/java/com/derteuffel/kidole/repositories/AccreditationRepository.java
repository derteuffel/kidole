package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Accreditation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccreditationRepository extends JpaRepository<Accreditation, Long> {

    List<Accreditation> findAllByUser_Id(Long id);
    List<Accreditation> findAllByCompetition_Id(Long id);

    List<Accreditation> findAllByStatus(String status);
}
