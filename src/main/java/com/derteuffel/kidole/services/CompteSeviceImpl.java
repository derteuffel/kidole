package com.derteuffel.kidole.services;

import com.derteuffel.kidole.entities.Compte;
import com.derteuffel.kidole.entities.Role;
import com.derteuffel.kidole.enums.ERole;
import com.derteuffel.kidole.helpers.CompteRegistrationDto;
import com.derteuffel.kidole.repositories.CompteRepository;
import com.derteuffel.kidole.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by user on 22/03/2020.
 */
@Service
public class CompteSeviceImpl implements CompteService{

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;



    @Override
    public Compte findByUsername(String username) {
        return compteRepository.findByUsername(username);
    }

    @Override
    public Compte save(CompteRegistrationDto compteRegistrationDto, String s) {
        Compte compte = new Compte();

        compte.setEmail(compteRegistrationDto.getEmail());
        compte.setPassword(passwordEncoder.encode(compteRegistrationDto.getPassword()));
        compte.setUsername(compteRegistrationDto.getUsername());
        compte.setAvatar(s);

        Role role = new Role();

        if (compteRepository.findAll().size() <=1){
            role.setName(ERole.ROLE_ROOT.toString());
        }else {
            role.setName(ERole.ROLE_COORDINATOR.toString());
        }

        Role existRole = roleRepository.findByName(role.getName());
        if (existRole != null){
            compte.setRoles(Arrays.asList(existRole));
        }else {
            roleRepository.save(role);
            compte.setRoles(Arrays.asList(role));
        }
        compteRepository.save(compte);
        return compte;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Compte compte = compteRepository.findByUsername(username);
        if (compte == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(compte.getUsername(),
                compte.getPassword(),
                mapRolesToAuthorities(compte.getRoles()));
    }

    private Collection <? extends GrantedAuthority> mapRolesToAuthorities(Collection< Role > roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
