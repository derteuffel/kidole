package com.derteuffel.kidole.controllers;

import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.repositories.AccreditationRepository;
import com.derteuffel.kidole.repositories.CompetitionRepository;
import com.derteuffel.kidole.repositories.UserCompetRepository;
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
    private UserCompetRepository userCompetRepository;

    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("")
    public ResponseEntity<Competition>  save(@RequestBody Competition competition) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        competition.setStatus(ECompetition.ATTENTE.toString());
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

    @PostMapping("/accreditation/{userId}/{competitionId}")
    public ResponseEntity<List<Object>>  save(@RequestBody UserCompet userCompet, @RequestBody Accreditation accreditation, @PathVariable Long userId, @PathVariable Long competitionId) {
        User user = userRepository.getOne(userId);
        Competition competition = competitionRepository.getOne(competitionId);
        accreditation.setStatus(ECompetition.ATTENTE.toString());
        if (!competition.getUsers().contains(user)){
            competition.getUsers().add(user);
            competitionRepository.save(competition);
        }else {
            new Exception("this user is already inside this competition");
        }

        try {
            userCompet.setUserId(user.getId());
            userCompet.setCompetId(competition.getId());
            UserCompet _userCompet = userCompetRepository.save(userCompet);
            accreditation.setUserCompet(_userCompet);
            accreditationRepository.save(accreditation);
            List<Object> elements = new ArrayList<>();
            elements.add(_userCompet);
            elements.add(accreditation);
            return new ResponseEntity<>(elements, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>((List<Object>) null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/accreditations/{id}")
    public ResponseEntity<Accreditation> validateAccreditation(@RequestBody Accreditation accreditation, @PathVariable Long id){
        Accreditation accreditation1 = accreditationRepository.findByUserCompet_Id(id);

        accreditation1.setStatus(ECompetition.ENCOURS.toString());
        accreditation1.setDescription(accreditation.getDescription());
        accreditation1.setName(accreditation.getName());

        try {
            accreditationRepository.save(accreditation1);
            return new ResponseEntity<>(accreditation1, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>((Accreditation)null,HttpStatus.EXPECTATION_FAILED);
        }

    }

}
