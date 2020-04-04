package com.derteuffel.kidole.thymeleaf;

import com.derteuffel.kidole.repositories.AccreditationRepository;
import com.derteuffel.kidole.repositories.CompetitionRepository;
import com.derteuffel.kidole.repositories.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * Created by user on 03/04/2020.
 */
@Controller
public class ThCompetitionController {

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
