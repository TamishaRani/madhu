package com.tamisa.superadmin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helthCheck")
public class helthCheckController {

    @GetMapping
    public String getHelthDetails() {
        return "Ok";
    }
}
