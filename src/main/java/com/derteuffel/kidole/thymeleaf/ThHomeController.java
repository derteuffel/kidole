package com.derteuffel.kidole.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by user on 03/04/2020.
 */
@Controller
public class ThHomeController {

    @GetMapping("/")
    public String home(){
        return "index";
    }
}
