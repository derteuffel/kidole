package com.derteuffel.kidole.thymeleaf;

import com.derteuffel.kidole.repositories.AccreditationRepository;
import com.derteuffel.kidole.repositories.CompetitionRepository;
import com.derteuffel.kidole.repositories.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    public String findAll(Model model){
        return "";
    }
}
