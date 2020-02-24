package com.derteuffel.kidole.controllers;

import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.repositories.ConfrontationRepository;
import com.derteuffel.kidole.repositories.PouleRepository;
import com.derteuffel.kidole.repositories.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kidole/poules")
@CrossOrigin("*")
public class PouleController {
    @Autowired
    private PouleRepository pouleRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ConfrontationRepository confrontationRepository;


    @PostMapping("")
    public ResponseEntity<Poule> save(@RequestBody Poule poule) {
            try {
                pouleRepository.save(poule);
                return new ResponseEntity<>(poule, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>((Poule) null, HttpStatus.EXPECTATION_FAILED);
            }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Poule> findById(@PathVariable Long id) {

        Optional<Poule> pouleOptional = pouleRepository.findById(id);
        if (pouleOptional.isPresent()){
            return new ResponseEntity<>(pouleOptional.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        try {
            pouleRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poule> update(@RequestBody Poule poule, @PathVariable Long id){
        Optional<Poule> pouleOptional = pouleRepository.findById(id);

        if (pouleOptional.isPresent()){
            pouleOptional.get().setLibelle(poule.getLibelle());
            pouleOptional.get().setName(poule.getName());
            pouleOptional.get().setSiteIds(pouleOptional.get().getSiteIds());

            return new ResponseEntity<>(pouleRepository.save(pouleOptional.get()),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/confrontations/{id}")
    public ResponseEntity<List<Confrontation>> findAllConfrontations(@PathVariable Long id) {
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

    @GetMapping("/sites/{id}")
    public ResponseEntity<List<Site>> findAllSite(@PathVariable Long id) {
        List<Site> sites = new ArrayList<>();
        Poule poule = pouleRepository.getOne(id);
        try {
            for (Long ids : poule.getSiteIds()){
                sites.add(siteRepository.getOne(ids));
            }
            if (sites.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(sites,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Site>) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
