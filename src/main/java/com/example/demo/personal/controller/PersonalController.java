package com.example.demo.personal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonalController {

    @GetMapping("/personal/personalMain")
    public String personalMain(Model model){

        model.addAttribute("mode", "personal");

        return "personal/personalMain";
    }

}
