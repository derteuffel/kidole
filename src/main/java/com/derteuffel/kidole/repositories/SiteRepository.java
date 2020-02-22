package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    Set<Site> findAllByPoules_Id(Long id);
}
