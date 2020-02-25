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
@CrossOrigin("*")
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
            teamRepository.getOne(id).getUsers().forEach(users :: add);
            if (users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<User>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/confrontations/{id}")
    public ResponseEntity<List<Confrontation>> findAllConfrontations(@PathVariable Long id) {
        List<Confrontation> confrontations = new ArrayList<>();
        try {
            Team team = teamRepository.getOne(id);
            for (Long ids : team.getConfrontaionsIds()){
                confrontations.add(confrontationRepository.getOne(ids));
            }

            if (confrontations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(confrontations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Confrontation>) null, HttpStatus.INTERNAL_SERVER_ERROR);
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
