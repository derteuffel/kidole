package com.derteuffel.kidole.repositories;

import com.derteuffel.kidole.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by user on 22/03/2020.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);
}
