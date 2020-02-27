package com.derteuffel.kidole.controllers;

import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/kidole/accreditations")
@CrossOrigin("*")
public class AccreditationController {

    @Autowired
    private AccreditationRepository accreditationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private ArbitreRepository arbitreRepository;

    @Autowired
    private ComiteRepository comiteRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private DignitaireRepository dignitaireRepository;

    @Autowired
    private OfficielRepository officielRepository;

    @Autowired
    private SparingRepository sparingRepository;

    @Autowired
    private EntraineurRepository entraineurRepository;



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
    public ResponseEntity<List<Accreditation>> findAllByTeam(@PathVariable Long id) {
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

    @PostMapping("/{teamId}/{competitionId}")
    public ResponseEntity<Accreditation>  save(@RequestBody Accreditation accreditation, @PathVariable Long teamId, @PathVariable Long competitionId) {

        Optional<User> user = userRepository.findByFirstnameOrLastname(accreditation.getName(),accreditation.getName());
        Team team = teamRepository.getOne(teamId);
        Competition competition = competitionRepository.getOne(competitionId);
        accreditation.setCompetition(competition);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd hh:mm");
        accreditation.setDate(dateFormat.format(date));
        if (user.isPresent()){
            accreditation.setUser(user.get());
            team.getUsers().add(user.get());
            teamRepository.save(team);
            user.get().getTeamIds().add(team.getId());
            user.get().getCompetIds().add(competition.getId());
            userRepository.save(user.get());
        }else {

            if (accreditation.getType().contentEquals(EAccreditation.ARBITRE.toString())){
                Arbitre arbitre = new Arbitre();
                arbitre.setFirstname(accreditation.getName());
                arbitre.setLastname(accreditation.getName());
                arbitreRepository.save(arbitre);
                accreditation.setUser(arbitre);
                team.getUsers().add(arbitre);
                teamRepository.save(team);
                arbitre.getTeamIds().add(team.getId());
                arbitre.getCompetIds().add(competition.getId());
                arbitreRepository.save(arbitre);
            }else if (accreditation.getType().contentEquals(EAccreditation.ATHLETE.toString())){
                Athlete athlete = new Athlete();
                athlete.setFirstname(accreditation.getName());
                athlete.setLastname(accreditation.getName());
                athleteRepository.save(athlete);
                accreditation.setUser(athlete);
                team.getUsers().add(athlete);
                teamRepository.save(team);
                athlete.getTeamIds().add(team.getId());
                athlete.getCompetIds().add(competition.getId());
                athleteRepository.save(athlete);
            }else if (accreditation.getType().contentEquals(EAccreditation.COMITE.toString())){
                Comite comite = new Comite();
                comite.setFirstname(accreditation.getName());
                comite.setLastname(accreditation.getName());
                comiteRepository.save(comite);
                accreditation.setUser(comite);
                team.getUsers().add(comite);
                teamRepository.save(team);
                comite.getTeamIds().add(team.getId());
                comite.getCompetIds().add(competition.getId());
                comiteRepository.save(comite);
            }else if (accreditation.getType().contentEquals(EAccreditation.ENTRAINEUR.toString())){
                Entraineur entraineur = new Entraineur();
                entraineur.setFirstname(accreditation.getName());
                entraineur.setLastname(accreditation.getName());
                entraineurRepository.save(entraineur);
                accreditation.setUser(entraineur);
                team.getUsers().add(entraineur);
                teamRepository.save(team);
                entraineur.getTeamIds().add(team.getId());
                entraineur.getCompetIds().add(competition.getId());
                entraineurRepository.save(entraineur);
            }else if (accreditation.getType().contentEquals(EAccreditation.DIGNITAIRE.toString())){
                Dignitaire dignitaire = new Dignitaire();
                dignitaire.setFirstname(accreditation.getName());
                dignitaire.setLastname(accreditation.getName());
                dignitaireRepository.save(dignitaire);
                accreditation.setUser(dignitaire);
                team.getUsers().add(dignitaire);
                teamRepository.save(team);
                dignitaire.getTeamIds().add(team.getId());
                dignitaire.getCompetIds().add(competition.getId());
                dignitaireRepository.save(dignitaire);
            }else if (accreditation.getType().contentEquals(EAccreditation.OFFICIEL.toString())){
                Officiciel officiciel = new Officiciel();
                officiciel.setFirstname(accreditation.getName());
                officiciel.setLastname(accreditation.getName());
                officielRepository.save(officiciel);
                accreditation.setUser(officiciel);
                team.getUsers().add(officiciel);
                teamRepository.save(team);
                officiciel.getTeamIds().add(team.getId());
                officiciel.getCompetIds().add(competition.getId());
                officielRepository.save(officiciel);
            }else {
                Sparing sparing = new Sparing();
                sparing.setFirstname(accreditation.getName());
                sparing.setLastname(accreditation.getName());
                sparingRepository.save(sparing);
                accreditation.setUser(sparing);
                team.getUsers().add(sparing);
                teamRepository.save(team);
                sparing.getTeamIds().add(team.getId());
                sparing.getCompetIds().add(competition.getId());
                sparingRepository.save(sparing);
            }

        }



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
