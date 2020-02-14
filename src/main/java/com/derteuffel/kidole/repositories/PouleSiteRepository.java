package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.PouleSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PouleSiteRepository extends JpaRepository<PouleSite,Long> {
}
