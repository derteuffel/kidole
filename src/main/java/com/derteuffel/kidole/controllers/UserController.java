package com.derteuffel.kidole.controllers;

import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.repositories.CompetitionRepository;
import com.derteuffel.kidole.repositories.TeamRepository;
import com.derteuffel.kidole.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/kidole/users")
@CrossOrigin("*")
public class UserController {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CompetitionRepository competitionRepository;


    @GetMapping("/country/{country}")
    public ResponseEntity<List<User>> findAllByCountry(@PathVariable String country) {
        List<User> users = new ArrayList<>();
        try {
            userRepository.findAllByCountry(country).forEach(users :: add);
            if (users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<User>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<User>> findAllByRegion(@PathVariable String region) {
        List<User> users = new ArrayList<>();
        try {
            userRepository.findAllByRegion(region).forEach(users :: add);
            if (users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<User>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ville/{ville}")
    public ResponseEntity<List<User>> findAllByVille(@PathVariable String ville) {

        List<User> users = new ArrayList<>();
        try {
            userRepository.findAllByVille(ville).forEach(users :: add);
            if (users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<User>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = new ArrayList<>();
        try {
            userRepository.findAll().forEach(users :: add);
            if (users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<User>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<User>  save(@RequestBody User user) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        user.setBirthday(user.getBirthday());
        //user.setBirthday(dateFormat.format(user.getBirthday()));
        Optional<User> userOptional = userRepository.findByFirstnameOrLastname(user.getFirstname(),user.getLastname());

        if (userOptional.isPresent()){

            return new ResponseEntity<>(HttpStatus.FOUND);
        }else {
            try {
                User _user = userRepository.save(user);
                return new ResponseEntity<>(_user, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>((User) null, HttpStatus.EXPECTATION_FAILED);
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable Long id){
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()){
            User _user = userOptional.get();
            _user.setCountry(user.getCountry());
            _user.setBirthday(user.getBirthday());
            _user.setEmail(user.getEmail());
            _user.setFirstname(user.getFirstname());
            _user.setLastname(user.getLastname());
            _user.setPhone(user.getPhone());
            _user.setVille(user.getVille());
            _user.setRegion(user.getRegion());
            userRepository.save(_user);

            return new ResponseEntity<>(userRepository.save(_user),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<List<Team>> findAllTeams(@PathVariable Long id) {
        List<Team> teams = new ArrayList<>();
        try {
            User user =  userRepository.getOne(id);
            for (Long ids : user.getTeamIds()){
                teams.add(teamRepository.getOne(ids));
            }

            if (teams.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(teams,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Team>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/compets/{id}")
    public ResponseEntity<List<Competition>> findAllCompets(@PathVariable Long id) {
        List<Competition> competitions = new ArrayList<>();
        try {
            User user =  userRepository.getOne(id);
            for (Long ids : user.getCompetIds()){
                competitions.add(competitionRepository.getOne(ids));
            }

            if (competitions.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(competitions,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Competition>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
