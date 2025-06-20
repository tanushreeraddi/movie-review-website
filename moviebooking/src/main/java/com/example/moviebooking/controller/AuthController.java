package com.example.moviebooking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.moviebooking.model.LoginForm;
import com.example.moviebooking.model.MovieDto;
import com.example.moviebooking.model.Review;
import com.example.moviebooking.model.User;
import com.example.moviebooking.repository.ReviewRepository;
import com.example.moviebooking.repository.UserRepository;
import com.example.moviebooking.repository.WatchListRepository;
import com.example.moviebooking.service.OmdbService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private OmdbService omdbService;

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private WatchListRepository watchListRepository;

	@GetMapping("/login")
	public String showLoginPage(Model model) {
		if (!model.containsAttribute("loginForm")) {
			model.addAttribute("loginForm", new LoginForm()); // your login form backing object
		}
		return "login"; // Thymeleaf template name: login.html
	}

	@PostMapping("/login")
	public String loginUser(@RequestParam("username") String email, @RequestParam("password") String password,
			Model model, HttpSession session) {
		Optional<User> optionalUser = userRepository.findByEmail(email);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (user.getPassword().equals(password)) {
				// Save user info in session if needed
				session.setAttribute("loggedInUser", user);
				session.setAttribute("email", user.getEmail());

				return "redirect:/user-loggedin-home"; // redirect to home page
			} else {
				model.addAttribute("loginError", "Invalid password");
			}
		} else {
			model.addAttribute("loginError", "User not found");
		}

		return "login";
	}

	@GetMapping("/user-loggedin-home")
	public String showLoggedInUser(HttpSession session, Model model,
			@RequestParam(defaultValue = "Avengers") String search, @RequestParam(defaultValue = "1") Integer page) {
		String email = (String) session.getAttribute("email");

		if (email == null) {
			return "redirect:/login";
		}

		List<MovieDto> movies = omdbService.searchMovies(search);

		Map<String, Review> reviewMap = new HashMap<>();

		for (Review review : reviewRepository.findByUserEmail(email)) {
			reviewMap.put(review.getImdbId(), review);
		}
		
		
		List<String> watchlist = watchListRepository.findImdbIdsByUserEmail(email);
		model.addAttribute("watchlist", watchlist);
		
		model.addAttribute("movies", movies);
		model.addAttribute("page", page);
		model.addAttribute("search", search);
		model.addAttribute("reviews", reviewMap);

		model.addAttribute("email", email);
		return "user-loggedin-home";
	}
	
	
	@GetMapping("/search")
	public String searchMovies(HttpSession session, Model model,
			@RequestParam(defaultValue = "Avengers") String search, @RequestParam(defaultValue = "1") Integer page) {
		String email = (String) session.getAttribute("email");

		if (email == null) {
			return "redirect:/login";
		}

		List<MovieDto> movies = omdbService.searchMovies(search);

		Map<String, Review> reviewMap = new HashMap<>();

		for (Review review : reviewRepository.findByUserEmail(email)) {
			reviewMap.put(review.getImdbId(), review);
		}
		
		
		List<String> watchlist = watchListRepository.findImdbIdsByUserEmail(email);
		model.addAttribute("watchlist", watchlist);
		
		model.addAttribute("movies", movies);
		model.addAttribute("page", page);
		model.addAttribute("search", search);
		model.addAttribute("reviews", reviewMap);

		model.addAttribute("email", email);
		return "user-loggedin-home";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

}
