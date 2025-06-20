package com.example.moviebooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/movie-ticket-booking")
    public String home() {
        return "home";  // serves home.html
    }
    
    @GetMapping("/")
    public String redirectRoot() {
        return "redirect:/movie-ticket-booking";
    }

}