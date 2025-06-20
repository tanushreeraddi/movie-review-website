package com.example.moviebooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.moviebooking.model.MovieDto;
import com.example.moviebooking.model.OmdbMovieDto;
import com.example.moviebooking.model.WatchList;
import com.example.moviebooking.repository.WatchListRepository;

import java.util.*;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class WatchListController {

	@Autowired
	private WatchListRepository watchListRepository;

	@Value("${omdb.api.key}")
	private String apiKey;

	@Value("${omdb.api.url}")
	private String apiUrl;

	@PostMapping("/movie/watchlist/add")
	@ResponseBody
	public ResponseEntity<?> addToWatchlist(@RequestBody Map<String, List<String>> data, HttpSession session) {

		String email = (String) session.getAttribute("email");

		if (email == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		List<String> imdbIds = data.get("imdbIds");
		List<String> duplicates = new ArrayList<>();

		for (String imdbId : imdbIds) {
			boolean exists = watchListRepository.existsByUserEmailAndImdbId(email, imdbId);
			if (exists) {
				duplicates.add(imdbId);
			} else {
				watchListRepository.save(new WatchList(email, imdbId));
			}
		}

		if (!duplicates.isEmpty()) {
			// Return warning message with list of duplicates
			String message = "Selected movies are already in your watchlist: ";
			return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/movie/watchlist")
	public String getWatchList(HttpSession session, Model model) {
		String email = (String) session.getAttribute("email");
		if (email == null) {
			return "redirect:/login";
		}

		List<WatchList> watchList = watchListRepository.findByUserEmail(email);
		List<OmdbMovieDto> fullMovieList = new ArrayList<>();

		for (WatchList item : watchList) {
			String imdbId = item.getImdbId();
			try {
				RestTemplate restTemplate = new RestTemplate();
				String url = "https://www.omdbapi.com/?i=" + imdbId + "&apikey=" + apiKey;

				ResponseEntity<OmdbMovieDto> response = restTemplate.getForEntity(url, OmdbMovieDto.class);
				if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
					fullMovieList.add(response.getBody());
				}
			} catch (Exception e) {
				System.out.println("Failed to fetch from OMDb for ID: " + imdbId);
				e.printStackTrace();
			}
		}

		model.addAttribute("movies", fullMovieList);
		return "watchlist";
	}

	@Transactional
	@PostMapping("/movie/watchlist/delete")
	public String deleteFromWatchlist(@RequestParam(value = "imdbIds", required = false) List<String> imdbIds,
			HttpSession session, RedirectAttributes redirectAttributes, Model model) {

		String email = (String) session.getAttribute("email");

		if (email == null) {
			redirectAttributes.addFlashAttribute("flashMessage", "Please login first.");
			return "redirect:/login"; // or appropriate login page
		}

		if (imdbIds == null || imdbIds.isEmpty()) {
			redirectAttributes.addFlashAttribute("flashMessage", "No movies selected for deletion.");
			return "redirect:/movie/watchlist";
		}

		try {
			// delete by email + imdbId
			for (String imdbId : imdbIds) {
				watchListRepository.deleteByUserEmailAndImdbId(email, imdbId);
			}
			redirectAttributes.addFlashAttribute("flashMessage", "Selected movies removed from your Watchlist.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("flashMessage", "Error deleting movies: " + e.getMessage());
		}

		return "redirect:/movie/watchlist";
	}

}
