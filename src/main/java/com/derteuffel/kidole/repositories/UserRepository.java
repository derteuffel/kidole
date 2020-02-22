package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAllByCountry(String country);
    List<User> findAllByRegion(String region);
    List<User> findAllByVille(String ville);
    Set<User> findAllByTeams_Id(Long id);
    Optional<User> findByFirstnameOrLastname(String firstname, String lastname);
}
