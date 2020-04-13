package com.derteuffel.kidole.thymeleaf;

import com.derteuffel.kidole.entities.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by user on 04/04/2020.
 */
@Controller
@RequestMapping("/coordinator/kidole")
public class ThOrganisateurController {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private AccreditationRepository accreditationRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;
    @Autowired
    private CompteService compteService;
    @Autowired
    private CompteRepository compteRepository;

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
        return "coordinator/home";
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
    public String getCompetition(@PathVariable Long id, Model model){
        Competition competition = competitionRepository.getOne(id);
        model.addAttribute("competition",competition);
        model.addAttribute("discipline", new Discipline());
        return "coordinator/competition/detail";
    }


    @GetMapping("/discipline/update/{id}")
    public String updateDiscipline(@PathVariable Long id, Model model){
        Discipline discipline = disciplineRepository.getOne(id);
        model.addAttribute("discipline",discipline);
        return "coordinator/discipline/edit";
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
    public String disciplineUpdate(@Valid Discipline discipline, @PathVariable Long id, RedirectAttributes redirectAttributes){
        Competition competition = competitionRepository.getOne(id);

            discipline.setName(discipline.getName().toString());
            discipline.setCompetition(competition);
            disciplineRepository.save(discipline);
            redirectAttributes.addFlashAttribute("success","Vous avez ajouter avec succes une discipline a cette competition");
            return "redirect:/coordinator/kidole/competition/detail/"+competition.getId();


    }

    @GetMapping("/discipline/detail/{id}")
    public String disciplineDetail(@PathVariable Long id, Model model){
        Discipline discipline = disciplineRepository.getOne(id);
        model.addAttribute("discipline",discipline);
        model.addAttribute("team", new Team());
        return "coordinator/discipline/detail";
    }

    @Autowired
    private TeamRepository teamRepository;


    @PostMapping("/teams/save/{id}")
    public String teamSave(@Valid Team team, @PathVariable Long id, RedirectAttributes redirectAttributes){
        Discipline discipline = disciplineRepository.getOne(id);
        team.setDiscipline(discipline);
        teamRepository.save(team);
        discipline.setNbreEquipe((discipline.getNbreEquipe()+1));
        disciplineRepository.save(discipline);
        redirectAttributes.addFlashAttribute("success","Vous avez ajouter avec succes une equipe a cette discipline");

        return "redirect:/coordinator/kidole/discipline/detail/"+discipline.getId();
    }
}