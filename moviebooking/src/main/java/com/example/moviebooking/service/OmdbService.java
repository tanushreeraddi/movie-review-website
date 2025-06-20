package com.example.moviebooking.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.moviebooking.model.MovieDto;

@Service
public class OmdbService {

	@Value("${omdb.api.key}")
	private String apiKey;

	@Value("${omdb.api.url}")
	private String apiUrl;

	private final RestTemplate restTemplate = new RestTemplate();


	public List<MovieDto> searchMovies(String query) {

		if (query == null || query.trim().isEmpty()) {
			query = "a"; // default query to get a broad list
		}
		String url = apiUrl + "?s=" + query + "&apikey=" + apiKey;

		ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

		System.out.println("URL called: " + url);
		System.out.println("Response: " + response.getBody());

		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			Object searchObj = response.getBody().get("Search");
			if (searchObj instanceof List<?>) {
				List<Map<String, String>> searchResults = (List<Map<String, String>>) searchObj;
				return searchResults.stream().map(item -> {
					MovieDto movie = new MovieDto();
					movie.setTitle(item.get("Title"));
					movie.setYear(item.get("Year"));
					movie.setImdbID(item.get("imdbID"));
					movie.setType(item.get("Type"));
					movie.setPoster(item.get("Poster"));
					return movie;
				}).toList();
			} else {
				System.out.println("Search key not found or not a list");
			}
		} else {
			System.out.println("API call failed: " + response.getStatusCode());
		}

		return List.of(); // return empty list on failure
	}
}