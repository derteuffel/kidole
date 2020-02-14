package com.derteuffel.kidole.service;


import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class KidoleServiceImpl implements IKidoleInitService{

    @Autowired
    private AccreditationRepository accreditationRepository;
    @Autowired
    private CompetDisciplineRepository competDisciplineRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private ConfrontationRepository confrontationRepository;
    @Autowired
    private CompteRepository compteRepository;
    @Autowired
    private ConfrontationTeamRepository confrontationTeamRepository;
    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private PouleRepository pouleRepository;
    @Autowired
    private PouleSiteRepository pouleSiteRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserCompetRepository userCompetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTeamRepository userTeamRepository;

    @Override
    public void initAccreditations() {

    }

    @Override
    public void initCompetDisciplines() {

        Stream.of("ae").forEach(cd ->{
            CompetDiscipline competDiscipline = new CompetDiscipline();
            competDiscipline.setName(cd);
            competDisciplineRepository.save(competDiscipline);
        });
    }

    @Override
    public void initComptes() {
        Stream.of("Edouga","peguy", "django").forEach(c -> {
            Compte compte = new Compte();
            compte.setUsername(c);
            compteRepository.save(compte);
        });

    }

    @Override
    public void initConfrontations() {

    }

    @Override
    public void initConfrontationTeams() {
        Stream.of("ma première description", "une description").forEach(com ->{
            ConfrontationTeam confrontationTeam = new ConfrontationTeam();
            confrontationTeam.setDecription(com);
            confrontationTeamRepository.save(confrontationTeam);
        });

    }

    @Override
    public void initDisciplines() {

        Stream.of("football", "Basketball", "volleyball").forEach(d ->{
            Discipline discipline = new Discipline();
            discipline.setName(d);
            disciplineRepository.save(discipline);
        });
    }

    @Override
    public void initPoules() {

        Stream.of("A", "B", "C").forEach(p ->{
            Poule poule = new Poule();
            poule.setName(p);
            pouleRepository.save(poule);
        });
    }

    @Override
    public void initSites() {

    }

    @Override
    public void initTeams() {


    }

    @Override
    public void initUsers() {
       Stream.of("Enzo", "Lamazo", "kutemodojika").forEach(u->{
           User user = new User();
           user.setLastname(u);
           userRepository.save(user);
       });
    }

    @Override
    public void initUserTeams() {
        Stream.of("AZ", "AZ").forEach(ut -> {
            UserTeam userTeam = new UserTeam();
            userTeam.setDescription(ut);
            userTeamRepository.save(userTeam);
        });
    }

    @Override
    public void initPouleSite() {
        Stream.of("AB", "CD").forEach(ps ->{
            PouleSite pouleSite = new PouleSite();
            pouleSite.setDescription(ps);
            pouleSiteRepository.save(pouleSite);
        });
    }

    @Override
    public void initUserCompet() {

        Stream.of("ma", "mo").forEach( uc->{
            UserCompet userCompet = new UserCompet();
            userCompet.setDescription(uc);
            userCompetRepository.save(userCompet);
        });

    }

    @Override
    public void initCompetitions() {
        Stream.of("Coupe du monde de football", "NBA", "coupe du monde de volleyBall").forEach(c ->{
            Competition competition = new Competition();
            competition.setName(c);
            competitionRepository.save(competition);
        });
    }
}
