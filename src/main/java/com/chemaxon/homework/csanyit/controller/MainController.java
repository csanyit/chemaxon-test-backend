package com.chemaxon.homework.csanyit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    public static final String URL = "/api/v1";

    @GetMapping(URL + "/ping")
    public String pingEndpointHandler() {
        return "ok";
    }

}
