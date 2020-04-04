package com.derteuffel.kidole.services;

import com.derteuffel.kidole.entities.Compte;
import com.derteuffel.kidole.helpers.CompteRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by user on 22/03/2020.
 */
public interface CompteService extends UserDetailsService{

    Compte findByUsername(String username);
    Compte save(CompteRegistrationDto compteRegistrationDto, String s, Long id);

}
