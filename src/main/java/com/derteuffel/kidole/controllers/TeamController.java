package com.derteuffel.kidole.controllers;

import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kidole/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccreditationRepository accreditationRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private ConfrontationRepository confrontationRepository;

    @PostMapping("/{id}")
    public ResponseEntity<Team> save(@RequestBody Team team, @PathVariable Long id) {

        Discipline discipline = disciplineRepository.getOne(id);
        Optional<Team> teamOptional = teamRepository.findByDiscipline_Id(discipline.getId());

        if (teamOptional.isPresent()){

            return new ResponseEntity<>(HttpStatus.FOUND);
        }else {
            try {
                team.setDiscipline(discipline);
                teamRepository.save(team);
                return new ResponseEntity<>(team, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>((Team) null, HttpStatus.EXPECTATION_FAILED);
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> findById(@PathVariable Long id) {

        Optional<Team> teamOptional = teamRepository.findById(id);
        if (teamOptional.isPresent()){
            return new ResponseEntity<>(teamOptional.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        try {
            teamRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/{id}/{disciplineId}")
    public ResponseEntity<Team> update(@RequestBody Team team, @PathVariable Long id, @PathVariable Long disciplineId){
        Optional<Team> teamOptional = teamRepository.findById(id);

        if (teamOptional.isPresent()){
           Team team1 = teamOptional.get();
           team1.setLibelle(team.getLibelle());
           team1.setName(team.getName());
           team1.setDiscipline(disciplineRepository.getOne(disciplineId));

            return new ResponseEntity<>(teamRepository.save(team1),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<User>> findAllUsers(@PathVariable Long id) {
        List<User> users = new ArrayList<>();
        try {
            userRepository.findAllByTeams_Id(id).forEach(users :: add);
            if (users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<User>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accreditations/{id}")
    public ResponseEntity<List<Accreditation>> findAllAccreditation(@PathVariable Long id) {
        List<Accreditation> accreditations = new ArrayList<>();
        try {
            accreditationRepository.findAllByTeam_Id(id).forEach(accreditations :: add);
            if (accreditations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(accreditations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Accreditation>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/discipline/{id}")
    public ResponseEntity<List<Team>> findAllByDiscipline(@PathVariable Long id) {
        List<Team> teams = new ArrayList<>();
        try {
            teamRepository.findAllByDiscipline_Id(id).forEach(teams :: add);
            if (teams.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(teams,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Team>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}