package com.example.moviebooking.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.moviebooking.model.Movie;
import com.example.moviebooking.service.MovieService;
import com.example.moviebooking.service.TheaterService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/movie-ticket-booking/movie")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@Autowired
	private TheaterService theaterService;

	@GetMapping
	public String showMovies(Model model) {
		model.addAttribute("movies", movieService.getAllMovies());
		return "movie";
	}

	@GetMapping("/add")
	public String showAddMovieForm(Model model) {
		model.addAttribute("movie", new Movie());
		model.addAttribute("theaters", theaterService.getAllTheaters());
		return "addmovie";
	}
	@PostMapping("/add")
	public String addMovie(@ModelAttribute Movie movie) {
		movieService.saveMovie(movie);
		return "redirect:/movie-ticket-booking/movie";
	}

	// Show edit form with existing movie data
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		Movie movieOpt = movieService.getMovieById(id);
		if (movieOpt == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Movie not found");
			return "redirect:/movie-ticket-booking/movie";
		}
		model.addAttribute("movie", movieOpt);
		model.addAttribute("theaters", theaterService.getAllTheaters()); // for the multi-select dropdown
		return "editmovie"; // reuse the addmovie.html form for both add and edit
	}

	// Handle the edit form POST
	@PostMapping("/edit/{id}")
	public String updateMovie(@PathVariable Long id, @ModelAttribute Movie movie,
			RedirectAttributes redirectAttributes) {
		Movie existingMovieOpt = movieService.getMovieById(id);
		if (existingMovieOpt == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Movie not found");
			return "redirect:/movie-ticket-booking/movie";
		}

		// Make sure to set the id so that save updates instead of inserts
		movie.setId(id);
		movieService.saveMovie(movie);

		redirectAttributes.addFlashAttribute("successMessage", "Movie updated successfully!");
		return "redirect:/movie-ticket-booking/movie";
	}

	@GetMapping("/delete/{id}")
	public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			movieService.deleteMovieById(id);
			redirectAttributes.addFlashAttribute("successMessage", "Movie deleted successfully!");
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/movie-ticket-booking/movie"; // or whatever your movie list page URL is
	}
}
