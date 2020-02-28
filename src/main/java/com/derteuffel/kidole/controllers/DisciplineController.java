package com.derteuffel.kidole.controllers;

import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.repositories.CompetitionRepository;
import com.derteuffel.kidole.repositories.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kidole/disciplines")
@CrossOrigin("*")
public class DisciplineController {

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @GetMapping("")
    public ResponseEntity<List<Discipline>> findAll() {
        List<Discipline> disciplines = new ArrayList<>();
        try {
            disciplineRepository.findAll().forEach(disciplines :: add);
            if (disciplines.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(disciplines,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Discipline>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/competition/{id}")
    public ResponseEntity<List<Discipline>> findAllByCompetition(@PathVariable Long id) {
        List<Discipline> disciplines = new ArrayList<>();
        try {
            disciplineRepository.findAllByCompetition_Id(id).forEach(disciplines :: add);
            if (disciplines.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(disciplines,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Discipline>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Discipline>> findAllByType(@PathVariable String type) {
        List<Discipline> disciplines = new ArrayList<>();
        try {
            disciplineRepository.findAllByType(type).forEach(disciplines :: add);
            if (disciplines.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(disciplines,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Discipline>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Discipline>> findAllByName(@PathVariable String name) {
        List<Discipline> disciplines = new ArrayList<>();
        try {
            disciplineRepository.findAllByName(name).forEach(disciplines :: add);
            if (disciplines.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(disciplines,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Discipline>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Discipline>  save(@RequestBody Discipline discipline, @PathVariable Long id) {

        Competition competition = competitionRepository.getOne(id);
        Optional<Discipline> disciplineOptional = disciplineRepository.findByNameAndCompetition_Id(discipline.getName(),competition.getId());

        if (disciplineOptional.isPresent()){

            return new ResponseEntity<>(HttpStatus.FOUND);
        }else {
            try {

                discipline.setName(discipline.getName().toString());
                discipline.setCompetition(competition);
                disciplineRepository.save(discipline);
                return new ResponseEntity<>(discipline, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>((Discipline) null, HttpStatus.EXPECTATION_FAILED);
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Discipline> findById(@PathVariable Long id) {

        Optional<Discipline> discipline = disciplineRepository.findById(id);
        if (discipline.isPresent()){
            return new ResponseEntity<>(discipline.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        try {
            disciplineRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discipline> update(@RequestBody Discipline discipline, @PathVariable Long id){
        Optional<Discipline> disciplineOptional = disciplineRepository.findById(id);

        if (disciplineOptional.isPresent()){
            Discipline _discipline = disciplineOptional.get();
            _discipline.setName(discipline.getName().toString());
            _discipline.setType(discipline.getType());
            _discipline.setNbreEquipe(discipline.getNbreEquipe());

            return new ResponseEntity<>(disciplineRepository.save(_discipline),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<List<Team>> findAllDiscipline(@PathVariable Long id) {
        List<Team> teams = new ArrayList<>();
        try {
            Discipline discipline = disciplineRepository.getOne(id);

            discipline.getTeams().forEach(teams :: add);
            if (teams.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(teams,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Team>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
