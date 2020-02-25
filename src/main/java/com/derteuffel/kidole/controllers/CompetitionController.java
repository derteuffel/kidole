package com.derteuffel.kidole.controllers;

import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.repositories.AccreditationRepository;
import com.derteuffel.kidole.repositories.CompetitionRepository;
import com.derteuffel.kidole.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kidole/competitions")
@CrossOrigin("*")
public class CompetitionController {


    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private AccreditationRepository accreditationRepository;

     //
    // ----- Competition methods -------//
                                       //
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Competition>> findAllByCountry(@PathVariable String status) {
        List<Competition> competitions = new ArrayList<>();
        try {
            competitionRepository.findAllByStatus(status).forEach(competitions :: add);
            if (competitions.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(competitions,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Competition>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("")
    public ResponseEntity<List<Competition>> findAll() {
        List<Competition> competitions = new ArrayList<>();
        try {
            competitionRepository.findAll().forEach(competitions :: add);
            if (competitions.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(competitions,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Competition>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/disciplines/{id}")
    public ResponseEntity<List<Discipline>> findAllDiscipline(@PathVariable Long id) {
        List<Discipline> disciplines = new ArrayList<>();
        try {
            Competition competition = competitionRepository.getOne(id);

            competition.getDisciplines().forEach(disciplines :: add);
            if (disciplines.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(disciplines,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Discipline>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sites/{id}")
    public ResponseEntity<List<Site>> findAllSites(@PathVariable Long id) {
        List<Site> sites = new ArrayList<>();
        try {
            Competition competition = competitionRepository.getOne(id);

            competition.getSites().forEach(sites :: add);
            if (sites.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(sites,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Site>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<List<Team>> findAllTeam(@PathVariable Long id) {
        List<Team> teams = new ArrayList<>();
        List<Discipline> disciplines = new ArrayList<>();
        try {
            Competition competition = competitionRepository.getOne(id);

            competition.getDisciplines().forEach(disciplines :: add);
            for (Discipline discipline : disciplines){
                discipline.getTeams().forEach(teams :: add);
            }
            if (teams.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(teams,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Team>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Competition>  save(@RequestBody Competition competition) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        DateFormat year = new SimpleDateFormat("yyyyMM");
        competition.setStatus(ECompetition.ATTENTE.toString());
        competition.setCode("#C"+competitionRepository.findAll().size()+year.format(new Date()));
        System.out.println(competition.getCode());
        try {
            Competition _competition = competitionRepository.save(competition);
            return new ResponseEntity<>(_competition, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>((Competition) null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Competition> findById(@PathVariable Long id) {

        Optional<Competition> competition = competitionRepository.findById(id);
        if (competition.isPresent()){
            if (competition.get().getDateDebut().equals(new Date()) && competition.get().getDateFin().after(new Date())){
                competition.get().setStatus(ECompetition.ENCOURS.toString());
            }else if (competition.get().getDateFin().before(new Date())){
                competition.get().setStatus(ECompetition.TERMINER.toString());
            }
            competitionRepository.save(competition.get());
            return new ResponseEntity<>(competition.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        try {
            competitionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/accreditations/{id}")
    public ResponseEntity<List<Accreditation>> findAllAccreditation(@PathVariable Long id) {
        List<Accreditation> accreditations = new ArrayList<>();
        try {
            accreditationRepository.findAllByCompetition_Id(id).forEach(accreditations :: add);
            if (accreditations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(accreditations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Accreditation>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<User>> findAllUsers(@PathVariable Long id) {
        List<User> users = new ArrayList<>();
        try {
            for (Accreditation accreditation : accreditationRepository.findAllByCompetition_Id(id)){
                users.add(accreditation.getUser());
            }
            if (users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<User>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Competition> update(@RequestBody Competition competition, @PathVariable Long id){
        Optional<Competition> competitionOptional = competitionRepository.findById(id);

        if (competitionOptional.isPresent()){
            Competition _competition = competitionOptional.get();
            if (_competition.getDateDebut().equals(new Date()) && competition.getDateFin().after(new Date())){
                competition.setStatus(ECompetition.ENCOURS.toString());
            }else if (competition.getDateFin().before(new Date())){
                competition.setStatus(ECompetition.TERMINER.toString());
            }
            _competition.setCode(competition.getCode());
            _competition.setDateDebut(competition.getDateDebut());
            _competition.setDateFin(competition.getDateFin());
            _competition.setDebutAccreditation(competition.getDebutAccreditation());
            _competition.setFinAccreditation(competition.getFinAccreditation());
            _competition.setName(competition.getName());
            competitionRepository.save(_competition);

            return new ResponseEntity<>(competitionRepository.save(_competition),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

     //
    // ----- Accreditation for an user to a competition  methods -------//
                                                                       //

}
