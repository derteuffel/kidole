package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAllByCountry(String country);
    List<User> findAllByRegion(String region);
    List<User> findAllByVille(String ville);
}
