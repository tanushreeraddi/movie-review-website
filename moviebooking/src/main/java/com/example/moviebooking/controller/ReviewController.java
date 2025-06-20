package com.example.moviebooking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.moviebooking.model.Review;
import com.example.moviebooking.repository.ReviewRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/movie/review")
public class ReviewController {

	@Autowired
	private ReviewRepository repository;

	@PostMapping
	public String submitReview(@RequestParam String movieName, @RequestParam int rating, @RequestParam String imdbId,
			@RequestParam String comment, HttpSession session, RedirectAttributes redirectAttributes) {

		System.out.println("jhhjgghjh");
		String userEmail = (String) session.getAttribute("email");

		Optional<Review> existing = repository.findByImdbIdAndUserEmail(imdbId, userEmail);
		Review review = existing.orElse(new Review());

		review.setMovieName(movieName);
		review.setComment(comment);
		review.setRating(rating);
		review.setImdbId(imdbId);
		review.setUserEmail(userEmail);

		repository.save(review);

		redirectAttributes.addFlashAttribute("message", "Review  successfully submitted for movie : " + movieName);
		return "redirect:/user-loggedin-home";

	}

}
