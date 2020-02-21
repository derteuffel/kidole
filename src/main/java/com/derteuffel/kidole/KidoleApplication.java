package com.derteuffel.kidole;

import com.derteuffel.kidole.service.IKidoleInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KidoleApplication implements CommandLineRunner {

    @Autowired
    private IKidoleInitService iKidoleInitService;

    public static void main(String[] args) {
        SpringApplication.run(KidoleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        iKidoleInitService.initAccreditations();
        iKidoleInitService.initCompetDisciplines();
        iKidoleInitService.initCompetitions();
        iKidoleInitService.initComptes();
        iKidoleInitService.initConfrontations();
        iKidoleInitService.initConfrontationTeams();
        iKidoleInitService.initDisciplines();
        iKidoleInitService.initPoules();
        iKidoleInitService.initPouleSite();
        iKidoleInitService.initSites();
        iKidoleInitService.initTeams();
        iKidoleInitService.initUsers();
        iKidoleInitService.initUserCompet();
        iKidoleInitService.initUserTeams();



    }
}
