package com.example.moviebooking.controller;

import com.example.moviebooking.model.MovieDto;
import com.example.moviebooking.model.User;
import com.example.moviebooking.service.MovieService;
import com.example.moviebooking.service.OmdbService;
import com.example.moviebooking.service.UserService;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private OmdbService omdbService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "register";
		}

		boolean isRegistered = userService.registerUser(user);
		if (!isRegistered) {
			model.addAttribute("emailError", "Email already exists");
			return "register";
		}

		model.addAttribute("successMessage", "User registered successfully!");
		model.addAttribute("user", new User()); // clear form
		return "register";
	}

	@GetMapping("/home")
	public String home(@RequestParam(defaultValue = "Avengers") String search, Model model,
			@RequestParam(defaultValue = "1") Integer page) {
		List<MovieDto> movies = omdbService.searchMovies(search);
		model.addAttribute("movies", movies);
		model.addAttribute("page", page);
		model.addAttribute("search", search);
		return "user-home";
	}

	

}