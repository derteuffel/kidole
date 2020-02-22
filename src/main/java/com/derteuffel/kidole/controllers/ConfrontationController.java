package com.derteuffel.kidole.controllers;

import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.repositories.ConfrontationRepository;
import com.derteuffel.kidole.repositories.PouleRepository;
import com.derteuffel.kidole.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kidole/confrontations")
public class ConfrontationController {

    @Autowired
    private ConfrontationRepository confrontationRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PouleRepository pouleRepository;


    @PostMapping("")
    public ResponseEntity<Confrontation> save(@RequestBody Confrontation confrontation, ArrayList<Long> teamsIds, Long id) {

        try {
            Poule poule = pouleRepository.getOne(id);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            confrontation.setDateConfrontation(dateFormat.format(confrontation.getDateConfrontation()));
            confrontation.setPoule(poule);
            confrontationRepository.save(confrontation);
            if (!teamsIds.isEmpty()) {
                for (Long teamId : teamsIds) {
                    Team team = teamRepository.getOne(teamId);
                    confrontation.getTeams().add(team);
                }
            }
            Confrontation _conf = confrontationRepository.save(confrontation);
            return new ResponseEntity<>(_conf, HttpStatus.CREATED);
        }catch (Exception e) {

            return new ResponseEntity<>((Confrontation) null, HttpStatus.EXPECTATION_FAILED);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Confrontation> findById(@PathVariable Long id) {

        Optional<Confrontation> confrontationOptional = confrontationRepository.findById(id);
        if (confrontationOptional.isPresent()){
            return new ResponseEntity<>(confrontationOptional.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        try {
            confrontationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Confrontation> update(@RequestBody Confrontation confrontation, @PathVariable Long id, ArrayList<Long> teamIds){
        Optional<Confrontation> confrontationOptional = confrontationRepository.findById(id);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (confrontationOptional.isPresent()){
            Confrontation confrontation1 = confrontationOptional.get();
            confrontation1.setDateConfrontation(dateFormat.format(confrontation.getDateConfrontation()));
            confrontation1.setHeureConfrontation(confrontation.getHeureConfrontation());
            confrontation1.setName(confrontation.getName());
            confrontation1.setResume(confrontation.getResume());
            if (!teamIds.isEmpty()){
                for (Long ids : teamIds){
                    Team team = teamRepository.getOne(ids);
                    if (!confrontation1.getTeams().contains(team)){
                        confrontation1.getTeams().add(team);
                    }
                }
            }


            return new ResponseEntity<>(confrontationRepository.save(confrontation1),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<List<Team>> findAllteams(@PathVariable Long id) {
        List<Team> teams = new ArrayList<>();
        try {
            teamRepository.findAllByConfrontations_Id(id).forEach(teams :: add);
            if (teams.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(teams,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Team>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/poule/{id}")
    public ResponseEntity<List<Confrontation>> findAllByPoule(@PathVariable Long id) {
        List<Confrontation> confrontations = new ArrayList<>();
        try {
            confrontationRepository.findAllByPoule_Id(id).forEach(confrontations :: add);
            if (confrontations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(confrontations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Confrontation>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/date/{dateConfrontation}")
    public ResponseEntity<List<Confrontation>> findAllByDate(@PathVariable String dateConfrontation) {
        List<Confrontation> confrontations = new ArrayList<>();
        try {
            confrontationRepository.findAllByDateConfrontation(dateConfrontation).forEach(confrontations :: add);
            if (confrontations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(confrontations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Confrontation>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/heure/{heureConfrontation}")
    public ResponseEntity<List<Confrontation>> findAllByHeure(@PathVariable String heureConfrontation) {
        List<Confrontation> confrontations = new ArrayList<>();
        try {
            confrontationRepository.findAllByHeureConfrontation(heureConfrontation).forEach(confrontations :: add);
            if (confrontations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(confrontations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Confrontation>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
