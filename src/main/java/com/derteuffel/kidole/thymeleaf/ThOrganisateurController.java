package com.derteuffel.kidole.thymeleaf;

import com.derteuffel.kidole.entities.*;
import com.derteuffel.kidole.enums.ETypeAccreditation;
import com.derteuffel.kidole.helpers.AccreditationHelper;
import com.derteuffel.kidole.helpers.CompteRegistrationDto;
import com.derteuffel.kidole.repositories.*;
import com.derteuffel.kidole.services.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.security.krb5.internal.crypto.EType;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by user on 04/04/2020.
 */
@Controller
@RequestMapping("/coordinator/kidole")
public class ThOrganisateurController {

    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private CompteService compteService;
    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private AccreditationRepository accreditationRepository;


    @Autowired
    private CompetitionRepository competitionRepository;


    @Autowired
    private TeamRepository teamRepository;

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

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ConfrontationRepository confrontationRepository;

    @Autowired
    private PouleRepository pouleRepository;

    @Autowired
    private FicheRepository ficheRepository;



    @Value("${file.upload-dir}")
    private  String fileStorage;

    public String findAll(Model model){
        return "";
    }

    @GetMapping("/login")
    public String director(){
        return "coordinator/login";
    }

    @ModelAttribute("compte")
    public CompteRegistrationDto compteRegistrationDto(){
        return new CompteRegistrationDto();
    }

    @GetMapping("/registration")
    public String registrationForm(Model model){
        return "coordinator/registration";
    }


    @PostMapping("/registration")
    public String registrationDirectionSave(@ModelAttribute("compte") @Valid CompteRegistrationDto compteDto,
                                            BindingResult result, RedirectAttributes redirectAttributes, Model model, @RequestParam("file") MultipartFile file){

        Compte existAccount = compteService.findByUsername(compteDto.getUsername());
        if (existAccount != null){
            result.rejectValue("username", null, "Il existe deja un compte avec ce nom d'utilisateur vueillez choisir un autre");
            model.addAttribute("error","Il existe deja un compte avec ce nom d'utilisateur vueillez choisir un autre");
        }

        if (result.hasErrors()) {
            return "coordinator/registration";
        }
        if (!(file.isEmpty())){
            try{
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(fileStorage + file.getOriginalFilename());
                Files.write(path, bytes);
            }catch (IOException e){
                e.printStackTrace();
            }
            compteService.save(compteDto,"/downloadFile/"+file.getOriginalFilename());
        }else {

            compteService.save(compteDto,"/img/avatar-1.jpg");
        }

        redirectAttributes.addFlashAttribute("success", "Votre enregistrement a ete effectuer avec succes");
        return "redirect:/coordinator/kidole/login";
    }

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model){
        Principal principal = request.getUserPrincipal();
        Compte compte = compteService.findByUsername(principal.getName());
        request.getSession().setAttribute("compte",compte);
        request.getSession().setAttribute("home","MES COMPETITIONS");
        Collection<Competition> competitions = compte.getCompetitions();
        model.addAttribute("lists",competitions);
        model.addAttribute("competition",new Competition());
        return "home";
    }

    @GetMapping("/form")
    public String show(Model model){

        model.addAttribute("competition", new Competition());
        return "coordinator/competition/form";
    }

    @PostMapping("/competition/save")
    public String competitionSave(Competition competition, RedirectAttributes redirectAttributes, HttpServletRequest request){
        Principal principal= request.getUserPrincipal();
        Compte compte = compteService.findByUsername(principal.getName());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        DateFormat year = new SimpleDateFormat("yyyyMM");
        if (competition.getType().equals("MONO_DISCIPLINE") && competition.getItems().size()>1){
            redirectAttributes.addFlashAttribute("error","Vous avez choisis le type mono disciplinaire vous devez choisir une seule discipline");
            return "redirect:/coordinator/kidole/home";
        }else {
            competition.setStatus(ECompetition.ATTENTE.toString());
            competition.setCode("#C" + competitionRepository.findAll().size() + year.format(new Date()));
            competitionRepository.save(competition);
            compte.getCompetitions().add(competition);
            compteRepository.save(compte);
            for (int i = 0; i < competition.getItems().size(); i++) {
                Discipline discipline = new Discipline();
                discipline.setName(competition.getItems().get(i));
                discipline.setCompetition(competition);
                System.out.println("i want to save " + discipline);
                disciplineRepository.save(discipline);
            }
        }

        return "redirect:/coordinator/kidole/home";
    }

    @GetMapping("/competition/detail/{id}")
    public String getCompetition(@PathVariable Long id, Model model, HttpServletRequest request){


        Competition competition = competitionRepository.getOne(id);
        model.addAttribute("competition",competition);
        request.getSession().setAttribute("competition",competition);
        model.addAttribute("discipline", new Discipline());
        return "coordinator/competition/detail";
    }

    @PostMapping("/disciplines/save/{id}")
    public String disciplineSave(@Valid Discipline discipline, @PathVariable Long id, RedirectAttributes redirectAttributes){
        Competition competition = competitionRepository.getOne(id);
        Optional<Discipline> disciplineOptional = disciplineRepository.findByNameAndCompetition_Id(discipline.getName(),competition.getId());
        if (disciplineOptional.isPresent()){
            redirectAttributes.addFlashAttribute("error", "Cette discipline est deja enregistrer pour cette competition");
            return "redirect:/coordinator/kidole/competition/detail/"+competition.getId();
        }else {
            discipline.setName(discipline.getName().toString());
            discipline.setCompetition(competition);
            disciplineRepository.save(discipline);
            redirectAttributes.addFlashAttribute("success","Vous avez ajouter avec succes une discipline a cette competition");
            return "redirect:/coordinator/kidole/competition/detail/"+competition.getId();
        }


    }
    @PostMapping("/disciplines/update/{id}")
    public String disciplineUpdate(@Valid Discipline discipline, @PathVariable Long id,
                                   RedirectAttributes redirectAttributes){
        Competition competition = competitionRepository.getOne(id);

            discipline.setName(discipline.getName().toString());
            discipline.setCompetition(competition);
            disciplineRepository.save(discipline);
            redirectAttributes.addFlashAttribute("success",
                    "Vous avez modifier avec succes une discipline a cette competition");
            return "redirect:/coordinator/kidole/competition/detail/"+competition.getId();


    }

    @GetMapping("/discipline/update/{id}")
    public String updateDiscipline(@PathVariable Long id, Model model){
        Discipline discipline = disciplineRepository.getOne(id);
        model.addAttribute("discipline",discipline);
        return "coordinator/discipline/edit";
    }

    @GetMapping("/discipline/detail/{id}")
    public String disciplineDetail(@PathVariable Long id, Model model){
        Discipline discipline = disciplineRepository.getOne(id);
        model.addAttribute("discipline",discipline);
        model.addAttribute("team", new Team());
        return "coordinator/discipline/detail";
    }



    @PostMapping("/teams/save/{id}")
    public String teamSave(@Valid Team team, @PathVariable Long id, RedirectAttributes redirectAttributes){
        Discipline discipline = disciplineRepository.getOne(id);
        team.setDiscipline(discipline);
        team.setName(team.getName().toUpperCase()+" ("+discipline.getName().toUpperCase()+")");
        teamRepository.save(team);
        discipline.setNbreEquipe((discipline.getNbreEquipe()+1));
        disciplineRepository.save(discipline);
        redirectAttributes.addFlashAttribute("success","Vous avez ajouter avec succes une equipe a cette discipline");

        return "redirect:/coordinator/kidole/discipline/detail/"+discipline.getId();
    }

    @GetMapping("/accreditations/{id}")
    public String findAllByTeam(@PathVariable Long id, Model model) {
        Competition competition = competitionRepository.getOne(id);
        List<Accreditation> accreditations = accreditationRepository.findAllByCompetition_Id(id);
        List<Team> teams = new ArrayList<>();
        List<Discipline> disciplines = disciplineRepository.findAllByCompetition_Id(competition.getId());
        for (Discipline discipline : disciplines){
            teams.addAll(teamRepository.findAllByDiscipline_Id(discipline.getId()));
        }

        model.addAttribute("teams",teams);
        model.addAttribute("accreditationHelper",new AccreditationHelper());
        model.addAttribute("lists",accreditations);
        model.addAttribute("home","MES ACCREDITATIONS");
        model.addAttribute("competition",competition);
        return "coordinator/accreditation/lists";
    }

    @PostMapping("/accreditation/save/{id}")
    public String  save(@Valid AccreditationHelper accreditationHelper,  Long teamId, @PathVariable Long id, RedirectAttributes redirectAttributes) {

        Optional<User> user = userRepository.findByFirstnameOrLastname(accreditationHelper.getFirstname(),accreditationHelper.getLastname());

        Competition competition = competitionRepository.getOne(id);
        Accreditation accreditation  = new Accreditation();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd hh:mm");
        accreditation.setDate(dateFormat.format(date));
        accreditation.setDescription(accreditationHelper.getDescription());
        accreditation.setName(accreditationHelper.getFirstname()+" "+accreditationHelper.getLastname());
        accreditation.setType(accreditationHelper.getType());
        accreditation.setCompetition(competition);
        Fiche fiche = new Fiche();
        fiche.setDescription(accreditationHelper.getDescription());
        fiche.setType(accreditationHelper.getType());
        if (user.isPresent()){
            if (teamId!=null) {
                Team team = teamRepository.getOne(teamId);
                accreditation.setUser(user.get());
                if (!(team.getUsers().contains(user.get()))) {
                    team.getUsers().add(user.get());
                }
                teamRepository.save(team);
                if (!(user.get().getTeamIds().contains(team.getId()))) {
                    user.get().getTeamIds().add(team.getId());
                }
                if (!(user.get().getCompetIds().contains(competition.getId()))) {
                    user.get().getCompetIds().add(competition.getId());
                }
                userRepository.save(user.get());
            }
        }else {
            if (accreditation.getType().contentEquals(EAccreditation.ARBITRE.toString())){
                Arbitre arbitre = new Arbitre();
                arbitre.setFirstname(accreditationHelper.getFirstname());
                fiche.setFirstname(accreditationHelper.getFirstname());
                arbitre.setLastname(accreditationHelper.getLastname());
                fiche.setLastname(accreditationHelper.getLastname());
                arbitre.setBirthday(accreditationHelper.getBirthday());
                fiche.setBirthday(accreditationHelper.getBirthday());
                arbitre.setEmail(accreditationHelper.getEmail());
                fiche.setEmail(accreditationHelper.getEmail());
                arbitre.setBirthplace(accreditationHelper.getBirthplace());
                fiche.setBirthplace(accreditationHelper.getBirthplace());
                arbitre.setPhone(accreditationHelper.getPhone());
                fiche.setPhone(accreditationHelper.getPhone());
                arbitre.setCountry(accreditationHelper.getCountry().toUpperCase());
                fiche.setCountry(accreditationHelper.getCountry().toUpperCase());
                arbitre.setRegion(accreditationHelper.getRegion().toUpperCase());
                fiche.setRegion(accreditationHelper.getRegion().toUpperCase());
                arbitre.setVille(accreditationHelper.getVille().toUpperCase());
                fiche.setVille(accreditationHelper.getVille().toUpperCase());
                arbitre.setPhoto("/images/icon/avatar-01.jpg");
                fiche.setPhoto("/images/icon/avatar-01.jpg");
                arbitre.setCompetIds(new ArrayList<>(Arrays.asList(competition.getId())));
                arbitreRepository.save(arbitre);
                ficheRepository.save(fiche);
                accreditation.setUser(arbitre);
            }else if (accreditation.getType().contentEquals(EAccreditation.ATHLETE.toString())){
                if (teamId!=null) {
                    Team team = teamRepository.getOne(teamId);
                    Athlete athlete = new Athlete();
                    athlete.setFirstname(accreditationHelper.getFirstname());
                    fiche.setFirstname(accreditationHelper.getFirstname());
                    athlete.setLastname(accreditationHelper.getLastname());
                    fiche.setLastname(accreditationHelper.getLastname());
                    athlete.setBirthday(accreditationHelper.getBirthday());
                    fiche.setBirthday(accreditationHelper.getBirthday());
                    athlete.setEmail(accreditationHelper.getEmail());
                    fiche.setEmail(accreditationHelper.getEmail());
                    athlete.setBirthplace(accreditationHelper.getBirthplace());
                    fiche.setBirthplace(accreditationHelper.getBirthplace());
                    athlete.setPhone(accreditationHelper.getPhone());
                    fiche.setPhone(accreditationHelper.getPhone());
                    athlete.setCountry(accreditationHelper.getCountry().toUpperCase());
                    fiche.setCountry(accreditationHelper.getCountry().toUpperCase());
                    athlete.setRegion(accreditationHelper.getRegion().toUpperCase());
                    fiche.setRegion(accreditationHelper.getRegion().toUpperCase());
                    athlete.setVille(accreditationHelper.getVille().toUpperCase());
                    fiche.setVille(accreditationHelper.getVille().toUpperCase());
                    athlete.setPhoto("/images/icon/avatar-01.jpg");
                    fiche.setPhoto("/images/icon/avatar-01.jpg");
                    athlete.setCompetIds(new ArrayList<>(Arrays.asList(competition.getId())));
                    athlete.setTeamIds(new ArrayList<>(Arrays.asList(team.getId())));
                    athlete.setDiscipline(team.getDiscipline().getName());
                    fiche.setDiscipline(team.getDiscipline().getName());
                    athlete.setEquipeOrigin(team.getName());
                    fiche.setEquipeOrigin(team.getName());
                    athlete.setCategory(competition.getCategory());
                    fiche.setCategory(competition.getCategory());
                    athleteRepository.save(athlete);
                    ficheRepository.save(fiche);
                    accreditation.setUser(athlete);
                    team.getUsers().add(athlete);
                    teamRepository.save(team);
                }else {
                    return "redirect:/coordinator/kidole/accreditations/"+competition.getId();
                }
            }else if (accreditation.getType().contentEquals(EAccreditation.COMITE.toString())){
                if (teamId!=null) {
                    Team team = teamRepository.getOne(teamId);
                    Comite comite = new Comite();
                    comite.setFirstname(accreditationHelper.getFirstname());
                    fiche.setFirstname(accreditationHelper.getFirstname());
                    comite.setLastname(accreditationHelper.getLastname());
                    fiche.setLastname(accreditationHelper.getLastname());
                    comite.setBirthday(accreditationHelper.getBirthday());
                    fiche.setBirthday(accreditationHelper.getBirthday());
                    comite.setEmail(accreditationHelper.getEmail());
                    fiche.setEmail(accreditationHelper.getEmail());
                    comite.setBirthplace(accreditationHelper.getBirthplace());
                    fiche.setBirthplace(accreditationHelper.getBirthplace());
                    comite.setPhone(accreditationHelper.getPhone());
                    fiche.setPhone(accreditationHelper.getPhone());
                    comite.setCountry(accreditationHelper.getCountry().toUpperCase());
                    fiche.setCountry(accreditationHelper.getCountry().toUpperCase());
                    comite.setRegion(accreditationHelper.getRegion().toUpperCase());
                    fiche.setRegion(accreditationHelper.getRegion().toUpperCase());
                    comite.setVille(accreditationHelper.getVille().toUpperCase());
                    fiche.setVille(accreditationHelper.getVille().toUpperCase());
                    comite.setPhoto("/images/icon/avatar-01.jpg");
                    fiche.setPhoto("/images/icon/avatar-01.jpg");
                    comite.setCompetIds(new ArrayList<>(Arrays.asList(competition.getId())));
                    comite.setTeamIds(new ArrayList<>(Arrays.asList(team.getId())));
                    comiteRepository.save(comite);
                    ficheRepository.save(fiche);
                    accreditation.setUser(comite);
                    team.getUsers().add(comite);
                    teamRepository.save(team);
                }else {
                    return "redirect:/coordinator/kidole/accreditations/"+competition.getId();
                }

            }else if (accreditation.getType().contentEquals(EAccreditation.ENTRAINEUR.toString())){
                if (teamId!=null) {
                    Team team = teamRepository.getOne(teamId);
                    Entraineur entraineur = new Entraineur();
                    entraineur.setFirstname(accreditationHelper.getFirstname());
                    fiche.setFirstname(accreditationHelper.getFirstname());
                    entraineur.setLastname(accreditationHelper.getLastname());
                    fiche.setLastname(accreditationHelper.getLastname());
                    entraineur.setBirthday(accreditationHelper.getBirthday());
                    fiche.setBirthday(accreditationHelper.getBirthday());
                    entraineur.setEmail(accreditationHelper.getEmail());
                    fiche.setEmail(accreditationHelper.getEmail());
                    entraineur.setBirthplace(accreditationHelper.getBirthplace());
                    fiche.setBirthplace(accreditationHelper.getBirthplace());
                    entraineur.setPhone(accreditationHelper.getPhone());
                    fiche.setPhone(accreditationHelper.getPhone());
                    entraineur.setCountry(accreditationHelper.getCountry().toUpperCase());
                    fiche.setCountry(accreditationHelper.getCountry().toUpperCase());
                    entraineur.setRegion(accreditationHelper.getRegion().toUpperCase());
                    fiche.setRegion(accreditationHelper.getRegion().toUpperCase());
                    entraineur.setVille(accreditationHelper.getVille().toUpperCase());
                    fiche.setVille(accreditationHelper.getVille().toUpperCase());
                    entraineur.setPhoto("/images/icon/avatar-01.jpg");
                    fiche.setPhoto("/images/icon/avatar-01.jpg");
                    entraineur.setCompetIds(new ArrayList<>(Arrays.asList(competition.getId())));
                    entraineur.setCompetIds(new ArrayList<>(Arrays.asList(competition.getId())));
                    entraineur.setTeamIds(new ArrayList<>(Arrays.asList(team.getId())));
                    entraineur.setDiscipline(team.getDiscipline().getName());
                    fiche.setDiscipline(team.getDiscipline().getName());
                    entraineur.setEquipe(team.getName());
                    fiche.setEquipe(team.getName());
                    entraineur.setCategory(competition.getCategory());
                    fiche.setCategory(competition.getCategory());
                    entraineurRepository.save(entraineur);
                    ficheRepository.save(fiche);
                    accreditation.setUser(entraineur);
                    team.getUsers().add(entraineur);
                    teamRepository.save(team);
                }else {
                    return "redirect:/coordinator/kidole/accreditations/"+competition.getId();
                }
            }else if (accreditation.getType().contentEquals(EAccreditation.DIGNITAIRE.toString())){
                if (teamId!=null) {
                    Team team = teamRepository.getOne(teamId);
                    Dignitaire dignitaire = new Dignitaire();
                    dignitaire.setFirstname(accreditationHelper.getFirstname());
                    fiche.setFirstname(accreditationHelper.getFirstname());
                    dignitaire.setLastname(accreditationHelper.getLastname());
                    fiche.setLastname(accreditationHelper.getLastname());
                    dignitaire.setBirthday(accreditationHelper.getBirthday());
                    fiche.setBirthday(accreditationHelper.getBirthday());
                    dignitaire.setEmail(accreditationHelper.getEmail());
                    fiche.setEmail(accreditationHelper.getEmail());
                    dignitaire.setBirthplace(accreditationHelper.getBirthplace());
                    fiche.setBirthplace(accreditationHelper.getBirthplace());
                    dignitaire.setPhone(accreditationHelper.getPhone());
                    fiche.setPhone(accreditationHelper.getPhone());
                    dignitaire.setCountry(accreditationHelper.getCountry().toUpperCase());
                    fiche.setCountry(accreditationHelper.getCountry().toUpperCase());
                    dignitaire.setRegion(accreditationHelper.getRegion().toUpperCase());
                    fiche.setRegion(accreditationHelper.getRegion().toUpperCase());
                    dignitaire.setVille(accreditationHelper.getVille().toUpperCase());
                    fiche.setVille(accreditationHelper.getVille().toUpperCase());
                    dignitaire.setPhoto("/images/icon/avatar-01.jpg");
                    fiche.setPhoto("/images/icon/avatar-01.jpg");
                    dignitaire.setCompetIds(new ArrayList<>(Arrays.asList(competition.getId())));
                    dignitaire.setTeamIds(new ArrayList<>(Arrays.asList(team.getId())));
                    dignitaireRepository.save(dignitaire);
                    ficheRepository.save(fiche);
                    accreditation.setUser(dignitaire);
                    team.getUsers().add(dignitaire);
                    teamRepository.save(team);
                }else {
                    return "redirect:/coordinator/kidole/accreditations/"+competition.getId();
                }
            }else if (accreditation.getType().contentEquals(EAccreditation.OFFICIEL.toString())) {
                if (teamId!=null){
                    Team team = teamRepository.getOne(teamId);
                Officiciel officiciel = new Officiciel();
                officiciel.setFirstname(accreditationHelper.getFirstname());
                fiche.setFirstname(accreditationHelper.getFirstname());
                officiciel.setLastname(accreditationHelper.getLastname());
                fiche.setLastname(accreditationHelper.getLastname());
                officiciel.setBirthday(accreditationHelper.getBirthday());
                fiche.setBirthday(accreditationHelper.getBirthday());
                officiciel.setEmail(accreditationHelper.getEmail());
                fiche.setEmail(accreditationHelper.getEmail());
                officiciel.setBirthplace(accreditationHelper.getBirthplace());
                fiche.setBirthplace(accreditationHelper.getBirthplace());
                officiciel.setPhone(accreditationHelper.getPhone());
                fiche.setPhone(accreditationHelper.getPhone());
                officiciel.setCountry(accreditationHelper.getCountry().toUpperCase());
                fiche.setCountry(accreditationHelper.getCountry().toUpperCase());
                officiciel.setRegion(accreditationHelper.getRegion().toUpperCase());
                fiche.setRegion(accreditationHelper.getRegion().toUpperCase());
                officiciel.setVille(accreditationHelper.getVille().toUpperCase());
                fiche.setVille(accreditationHelper.getVille().toUpperCase());
                officiciel.setPhoto("/images/icon/avatar-01.jpg");
                fiche.setPhoto("/images/icon/avatar-01.jpg");
                officiciel.setCompetIds(new ArrayList<>(Arrays.asList(competition.getId())));
                officiciel.setCompetIds(new ArrayList<>(Arrays.asList(competition.getId())));
                officiciel.setTeamIds(new ArrayList<>(Arrays.asList(team.getId())));
                officiciel.setEquipe(team.getName());
                fiche.setEquipe(team.getName());
                officielRepository.save(officiciel);
                ficheRepository.save(fiche);
                accreditation.setUser(officiciel);
                team.getUsers().add(officiciel);
                teamRepository.save(team);
            }else {
                    return "redirect:/coordinator/kidole/accreditations/"+competition.getId();
                }
            }else {
                if (teamId!=null) {
                    Team team = teamRepository.getOne(teamId);
                    Sparing sparing = new Sparing();
                    sparing.setFirstname(accreditationHelper.getFirstname());
                    fiche.setFirstname(accreditationHelper.getFirstname());
                    sparing.setLastname(accreditationHelper.getLastname());
                    fiche.setLastname(accreditationHelper.getLastname());
                    sparing.setBirthday(accreditationHelper.getBirthday());
                    fiche.setBirthday(accreditationHelper.getBirthday());
                    sparing.setEmail(accreditationHelper.getEmail());
                    fiche.setEmail(accreditationHelper.getEmail());
                    sparing.setBirthplace(accreditationHelper.getBirthplace());
                    fiche.setBirthplace(accreditationHelper.getBirthplace());
                    sparing.setPhone(accreditationHelper.getPhone());
                    fiche.setPhone(accreditationHelper.getPhone());
                    sparing.setCountry(accreditationHelper.getCountry().toUpperCase());
                    fiche.setCountry(accreditationHelper.getCountry().toUpperCase());
                    sparing.setRegion(accreditationHelper.getRegion().toUpperCase());
                    fiche.setRegion(accreditationHelper.getRegion().toUpperCase());
                    sparing.setVille(accreditationHelper.getVille().toUpperCase());
                    fiche.setVille(accreditationHelper.getVille().toUpperCase());
                    sparing.setPhoto("/images/icon/avatar-01.jpg");
                    fiche.setPhoto("/images/icon/avatar-01.jpg");
                    sparing.setCompetIds(new ArrayList<>(Arrays.asList(competition.getId())));
                    sparing.setTeamIds(new ArrayList<>(Arrays.asList(team.getId())));
                    sparing.setCategory(competition.getCategory());
                    fiche.setCategory(competition.getCategory());
                    sparing.setDiscipline(team.getDiscipline().getName());
                    fiche.setDiscipline(team.getDiscipline().getName());
                    sparing.setEquipeOrigin(team.getName());
                    fiche.setEquipeOrigin(team.getName());
                    sparingRepository.save(sparing);
                    ficheRepository.save(fiche);
                    accreditation.setUser(sparing);
                    team.getUsers().add(sparing);
                    teamRepository.save(team);
                }else {
                    return "redirect:/coordinator/kidole/accreditations/"+competition.getId();
                }
            }

        }



        accreditation.setStatus(ECompetition.ENCOURS.toString());
            accreditationRepository.save(accreditation);
            fiche.setAccreditation(accreditation);
            ficheRepository.save(fiche);
            redirectAttributes.addFlashAttribute("success","Vous avez proceder avec success a l'accreditation de cet athlete");
            return "redirect:/coordinator/kidole/accreditations/"+competition.getId();

    }

    @GetMapping("/accreditation/fiche/{id}")
    public String getFiche(@PathVariable Long id, Model model){
        Accreditation accreditation = accreditationRepository.getOne(id);
        Fiche fiche = ficheRepository.findByAccreditation_Id(accreditation.getId());

        model.addAttribute("accreditation",accreditation);
        model.addAttribute("fiche",fiche);
        return "coordinator/accreditation/fiche";
    }

    @GetMapping("/accreditation/fiche/update/{id}")
    public String updateFiche(@PathVariable Long id, Model model){
        Fiche fiche = ficheRepository.getOne(id);
        List<Team> teams = new ArrayList<>();
        List<Discipline> disciplines = disciplineRepository.findAllByCompetition_Id(fiche.getAccreditation().getCompetition().getId());
        for (Discipline discipline : disciplines){
            teams.addAll(teamRepository.findAllByDiscipline_Id(discipline.getId()));
        }

        model.addAttribute("teams",teams);
        model.addAttribute("fiche",fiche);
        return "coordinator/accreditation/ficheUpdate";
    }

    @PostMapping("/accreditation/fiche/save/{id}")
    public String ficheUpdateSave(@Valid Fiche fiche, @RequestParam("file")MultipartFile file,@PathVariable Long id){
        Accreditation accreditation = accreditationRepository.getOne(id);
        if (!(file.isEmpty())){
            try{
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(fileStorage + file.getOriginalFilename());
                Files.write(path, bytes);
            }catch (IOException e){
                e.printStackTrace();
            }
            fiche.setPhoto("/downloadFile/"+file.getOriginalFilename());
        }else {
            fiche.setPhoto(fiche.getPhoto());
        }

        fiche.setCategory(accreditation.getCompetition().getCategory());
        fiche.setAccreditation(accreditation);
        ficheRepository.save(fiche);
        if (accreditation.getType().equals(ETypeAccreditation.ARBITRE.toString())){
            Arbitre arbitre = (Arbitre) accreditation.getUser();
            arbitre.setCategory(fiche.getCategory());
            arbitre.setDiscipline(fiche.getDiscipline());
            arbitre.setBirthday(fiche.getBirthday());
            arbitre.setCountry(fiche.getCountry());
            arbitre.setEmail(fiche.getEmail());
            arbitre.setFirstname(fiche.getFirstname());
            arbitre.setLastname(fiche.getLastname());
            arbitre.setPhone(fiche.getPhone());
            arbitre.setRegion(fiche.getRegion());
            arbitre.setVille(fiche.getVille());
            arbitre.setBirthplace(fiche.getBirthplace());
            arbitre.setPhoto(fiche.getPhoto());
            arbitreRepository.save(arbitre);
        }else if (accreditation.getType().equals(ETypeAccreditation.ATHLETE.toString())){
            Athlete athlete = (Athlete) accreditation.getUser();
            athlete.setCategory(fiche.getCategory());
            athlete.setDiscipline(fiche.getDiscipline());
            athlete.setBirthday(fiche.getBirthday());
            athlete.setCountry(fiche.getCountry());
            athlete.setEmail(fiche.getEmail());
            athlete.setFirstname(fiche.getFirstname());
            athlete.setLastname(fiche.getLastname());
            athlete.setPhone(fiche.getPhone());
            athlete.setRegion(fiche.getRegion());
            athlete.setVille(fiche.getVille());
            athlete.setBirthplace(fiche.getBirthplace());
            athlete.setPhoto(fiche.getPhoto());
            athlete.setEquipeOrigin(fiche.getEquipeOrigin());
            athlete.setHeight(fiche.getHeight());
            athlete.setWeight(fiche.getWeight());
            athlete.setManualite(fiche.getManualite());
            athlete.setEquipe(fiche.getEquipe());
            athleteRepository.save(athlete);

        }else if (accreditation.getType().equals(ETypeAccreditation.SPARING.toString())){
           Sparing sparing = (Sparing) accreditation.getUser();
            sparing.setCategory(fiche.getCategory());
            sparing.setDiscipline(fiche.getDiscipline());
            sparing.setBirthday(fiche.getBirthday());
            sparing.setCountry(fiche.getCountry());
            sparing.setEmail(fiche.getEmail());
            sparing.setFirstname(fiche.getFirstname());
            sparing.setLastname(fiche.getLastname());
            sparing.setPhone(fiche.getPhone());
            sparing.setRegion(fiche.getRegion());
            sparing.setVille(fiche.getVille());
            sparing.setBirthplace(fiche.getBirthplace());
            sparing.setPhoto(fiche.getPhoto());
            sparing.setEquipeOrigin(fiche.getEquipeOrigin());
            sparing.setHeight(fiche.getHeight());
            sparing.setWeight(fiche.getWeight());
            sparing.setManualite(fiche.getManualite());
            sparing.setEquipe(fiche.getEquipe());
            sparingRepository.save(sparing);
        }else if (accreditation.getType().equals(ETypeAccreditation.COMITE.toString())){
            Comite comite = (Comite) accreditation.getUser();
            comite.setBirthday(fiche.getBirthday());
            comite.setCountry(fiche.getCountry());
            comite.setEmail(fiche.getEmail());
            comite.setFirstname(fiche.getFirstname());
            comite.setLastname(fiche.getLastname());
            comite.setPhone(fiche.getPhone());
            comite.setRegion(fiche.getRegion());
            comite.setVille(fiche.getVille());
            comite.setBirthplace(fiche.getBirthplace());
            comite.setPhoto(fiche.getPhoto());
            comite.setFonction(fiche.getFonction());
            comiteRepository.save(comite);
        }else if (accreditation.getType().equals(ETypeAccreditation.OFFICIEL.toString())){
            Officiciel officiciel = (Officiciel) accreditation.getUser();
            officiciel.setBirthday(fiche.getBirthday());
            officiciel.setCountry(fiche.getCountry());
            officiciel.setEmail(fiche.getEmail());
            officiciel.setFirstname(fiche.getFirstname());
            officiciel.setLastname(fiche.getLastname());
            officiciel.setPhone(fiche.getPhone());
            officiciel.setRegion(fiche.getRegion());
            officiciel.setVille(fiche.getVille());
            officiciel.setBirthplace(fiche.getBirthplace());
            officiciel.setPhoto(fiche.getPhoto());
            officiciel.setFonction(fiche.getFonction());
            officiciel.setEquipe(fiche.getEquipe());
            officiciel.setType(fiche.getType());
            officielRepository.save(officiciel);
        }else if (accreditation.getType().equals(ETypeAccreditation.DIGNITAIRE.toString())){
            Dignitaire dignitaire = (Dignitaire) accreditation.getUser();
            dignitaire.setBirthday(fiche.getBirthday());
            dignitaire.setCountry(fiche.getCountry());
            dignitaire.setEmail(fiche.getEmail());
            dignitaire.setFirstname(fiche.getFirstname());
            dignitaire.setLastname(fiche.getLastname());
            dignitaire.setPhone(fiche.getPhone());
            dignitaire.setRegion(fiche.getRegion());
            dignitaire.setVille(fiche.getVille());
            dignitaire.setBirthplace(fiche.getBirthplace());
            dignitaire.setPhoto(fiche.getPhoto());
            dignitaire.setTitre(fiche.getTitre());
            dignitaireRepository.save(dignitaire);
        }else {
            Entraineur entraineur = (Entraineur) accreditation.getUser();
            entraineur.setBirthday(fiche.getBirthday());
            entraineur.setCountry(fiche.getCountry());
            entraineur.setEmail(fiche.getEmail());
            entraineur.setFirstname(fiche.getFirstname());
            entraineur.setLastname(fiche.getLastname());
            entraineur.setPhone(fiche.getPhone());
            entraineur.setRegion(fiche.getRegion());
            entraineur.setVille(fiche.getVille());
            entraineur.setBirthplace(fiche.getBirthplace());
            entraineur.setPhoto(fiche.getPhoto());
            entraineur.setCategory(fiche.getCategory());
            entraineur.setDiscipline(fiche.getDiscipline());
            entraineur.setEquipe(fiche.getEquipe());
            entraineurRepository.save(entraineur);
        }
        return "redirect:/coordinator/kidole/accreditation/fiche/"+accreditation.getId();
    }

    @GetMapping("/accreditation/update/{id}")
    public String updateAccreditetion(@PathVariable Long id, Model  model){
        Accreditation accreditation = accreditationRepository.getOne(id);
        List<Team> teams = new ArrayList<>();
        List<Discipline> disciplines = disciplineRepository.findAllByCompetition_Id(accreditation.getCompetition().getId());
        for (Discipline discipline : disciplines){
            teams.addAll(teamRepository.findAllByDiscipline_Id(discipline.getId()));
        }

        model.addAttribute("teams",teams);
        model.addAttribute("competition",comiteRepository.getOne(accreditation.getCompetition().getId()));
        model.addAttribute("accreditation",accreditation);
        return "coordinator/accreditation/update";
    }

    /* Accreditation */



    @GetMapping("/accreditation/detail/{id}")
    public String findAccreditationById(@PathVariable Long id, Model model){
        Accreditation accreditation = accreditationRepository.getOne(id);
        model.addAttribute("accreditation",accreditation);
        return "coordinator/accreditation/detail";
    }


    @GetMapping("/accreditation/delete/{id}")
    public String deleteAccreditation(@PathVariable Long id, Model model){

        Accreditation accreditation = accreditationRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("invalid accreditation id:" +id));
        accreditationRepository.deleteById(id);
        model.addAttribute("accreditation", accreditationRepository.findAll());

        return "redirect:/accreditation/all";

    }

    @GetMapping("/user/detail/{id}")
    public String user(@PathVariable Long id, Model model){
        Accreditation accreditation = accreditationRepository.getOne(id);
        if (accreditation.getType().equals(EAccreditation.ARBITRE.toString())){
            User user = arbitreRepository.getOne(accreditation.getUser().getId());
            model.addAttribute("user",user);
            return "coordinator/user/arbitre";
        }else if (accreditation.getType().equals(EAccreditation.ATHLETE.toString())){
            User user = athleteRepository.getOne(accreditation.getUser().getId());
            model.addAttribute("user",user);
            return "coordinator/user/athlete";
        }else if (accreditation.getType().equals(EAccreditation.COMITE.toString())){
            User user = comiteRepository.getOne(accreditation.getUser().getId());
            model.addAttribute("user",user);
            return "coordinator/user/comite";
        }else if (accreditation.getType().equals(EAccreditation.DIGNITAIRE.toString())){
            User user = dignitaireRepository.getOne(accreditation.getUser().getId());
            model.addAttribute("user",user);
            return "coordinator/user/dignitaire";
        }else if (accreditation.getType().equals(EAccreditation.ENTRAINEUR.toString())){
            User user = entraineurRepository.getOne(accreditation.getUser().getId());
            model.addAttribute("user",user);
            return "coordinator/user/entraineur";
        }else if (accreditation.getType().equals(EAccreditation.OFFICIEL.toString())){
            User user = officielRepository.getOne(accreditation.getUser().getId());
            model.addAttribute("user",user);
            return "coordinator/user/officiel";
        }else {
            User user = sparingRepository.getOne(accreditation.getUser().getId());
            model.addAttribute("user",user);
            return "coordinator/user/sparing";
        }
    }


    /********** Team ************/


    @GetMapping("/team/detail/{id}")
    public String findTeamById(@PathVariable Long id, Model model){
        Team team = teamRepository.getOne(id);
        model.addAttribute("team",team);
        //model.addAttribute("confrontation", new Confrontation());
        return "coordinator/team/detail";

    }

    @DeleteMapping("/team/delete/{id}")
    public String deleteTeamById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        Discipline discipline = disciplineRepository.getOne(id);
        Team team = teamRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("invalid team id:" +id));
        redirectAttributes.addFlashAttribute("success","Vous avez supprimer cette equipe");
        teamRepository.deleteById(id);
        model.addAttribute("team", teamRepository.findAll());

        return "redirect:/coordinator/kidole/discipline/detail/"+discipline.getId();
    }


    @GetMapping("/team/update/{id}")
    public String updateTeam(@PathVariable Long id, Model model){
        Team team = teamRepository.getOne(id);
        model.addAttribute("team",team);
        return "coordinator/team/edit";
    }

    @PostMapping("/teams/update/{id}")
    public String teamUpdate(@Valid Team team, @PathVariable Long id, RedirectAttributes redirectAttributes){
        Discipline discipline = disciplineRepository.getOne(id);
        team.setName(team.getName().toString());
        team.setDiscipline(discipline);
        teamRepository.save(team);
        redirectAttributes.addFlashAttribute("success","Vous avez modifier avec succes une équipe a cette discipline");
        return "redirect:/coordinator/kidole/discipline/detail/"+discipline.getId();


    }


    @GetMapping("/users/{id}")
    public String findAllUser(@PathVariable Long id){
        List<User> users = new ArrayList<>();
        try {
            teamRepository.getOne(id).getUsers().forEach(users :: add);
            if (users.isEmpty()){
                return "";
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    @GetMapping("/discipline/{id}")
    public String findAllByDiscipline(@PathVariable Long id){
        List<Team> teams = new ArrayList<>();
        try {
            teamRepository.findAllByDiscipline_Id(id).forEach(teams :: add);
            if (teams.isEmpty()){
                return "";
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    @GetMapping("/confrontations/{id}")
    public String findAllConfrontations(@PathVariable Long id){
        List<Confrontation> confrontations = new ArrayList<>();
        try {
            Team team = teamRepository.getOne(id);
            for (Long ids : team.getConfrontaionsIds()){
                confrontations.add(confrontationRepository.getOne(ids));
            }

            if (confrontations.isEmpty()){
                return "";
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }


    /********** Poule ************/


    public String savePoule(){

        return "";
    }

    public String update(@RequestBody Poule poule, @PathVariable Long id){
        Optional<Poule> pouleOptional = pouleRepository.findById(id);

        if (pouleOptional.isPresent()){
            pouleOptional.get().setLibelle(poule.getLibelle());
            pouleOptional.get().setName(poule.getName());
            pouleOptional.get().setSiteIds(pouleOptional.get().getSiteIds());

            return "";
        } else {
            return "";
        }
    }


    @GetMapping("/poule/sites/{id}")
    public String findAllSites(@PathVariable Long id) {
        List<Site> sites = new ArrayList<>();
        Poule poule = pouleRepository.getOne(id);
        try {
            for (Long ids : poule.getSiteIds()){
                sites.add(siteRepository.getOne(ids));
            }
            if (sites.isEmpty()){
                return "";
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    @GetMapping("/poule/confrontations/{id}")
    public String findAllPouleConfrontations(@PathVariable Long id) {
        List<Confrontation> confrontations = new ArrayList<>();
        try {
            confrontationRepository.findAllByPoule_Id(id).forEach(confrontations :: add);
            if (confrontations.isEmpty()){
                return "";
            }
            return "";
        } catch (Exception e) {
            return"";
        }
    }




}
