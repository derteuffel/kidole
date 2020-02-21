package com.derteuffel.kidole.controllers;

import com.derteuffel.kidole.entities.Accreditation;
import com.derteuffel.kidole.entities.Competition;
import com.derteuffel.kidole.entities.ECompetition;
import com.derteuffel.kidole.entities.User;
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
@RequestMapping("/api/kidole/accreditations")
public class AccreditationController {

    @Autowired
    private AccreditationRepository accreditationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompetitionRepository competitionRepository;



    @GetMapping("")
    public ResponseEntity<List<Accreditation>> findAll() {
        List<Accreditation> accreditations = new ArrayList<>();
        try {
            accreditationRepository.findAll().forEach(accreditations :: add);
            if (accreditations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(accreditations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Accreditation>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/competition/{id}")
    public ResponseEntity<List<Accreditation>> findAllByCompetition(@PathVariable Long id) {
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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Accreditation>> findAllByUser(@PathVariable Long id) {
        List<Accreditation> accreditations = new ArrayList<>();
        try {
            accreditationRepository.findAllByUser_Id(id).forEach(accreditations :: add);
            if (accreditations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(accreditations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Accreditation>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Accreditation>> findAllByStatus(@PathVariable String status) {
        List<Accreditation> accreditations = new ArrayList<>();
        try {
            accreditationRepository.findAllByStatus(status).forEach(accreditations :: add);
            if (accreditations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(accreditations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Accreditation>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/{competId}")
    public ResponseEntity<Accreditation>  save(@RequestBody Accreditation accreditation,@PathVariable Long userId, @PathVariable Long competId) {

        User user = userRepository.getOne(userId);
        Competition competition = competitionRepository.getOne(competId);
        accreditation.setCompetition(competition);
        accreditation.setUser(user);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd hh:mm");
        accreditation.setDate(dateFormat.format(date));
        accreditation.setStatus(ECompetition.ATTENTE.toString());
        try {
            Accreditation _accreditation = accreditationRepository.save(accreditation);
            return new ResponseEntity<>(_accreditation, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>((Accreditation) null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Accreditation> findById(@PathVariable Long id) {

        Optional<Accreditation> accreditation = accreditationRepository.findById(id);
        if (accreditation.isPresent()){
            return new ResponseEntity<>(accreditation.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        try {
            accreditationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Accreditation> update(@RequestBody Accreditation accreditation, @PathVariable Long id){
        Optional<Accreditation> accreditationOptional = accreditationRepository.findById(id);

        if (accreditationOptional.isPresent()){
            Accreditation _acc = accreditationOptional.get();
            _acc.setDate(_acc.getDate());
            _acc.setName(accreditation.getName());
            _acc.setDescription(accreditation.getDescription());
            _acc.setStatus(ECompetition.ENCOURS.toString());

            return new ResponseEntity<>(accreditationRepository.save(_acc),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
