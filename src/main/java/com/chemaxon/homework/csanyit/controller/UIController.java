package com.chemaxon.homework.csanyit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/tst")
    public String indexHandler(Model model) {
        return "index";
    }

}
