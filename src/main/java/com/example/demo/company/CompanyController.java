package com.example.demo.company;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyController {

    @GetMapping("/company/companyMain")
    public String companyMain(Model model){
        model.addAttribute("mode", "company");
        return "company/companyMain";
    }





}
