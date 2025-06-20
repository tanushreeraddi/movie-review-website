package com.example.moviebooking.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class WatchList {
	@Id
	@GeneratedValue
	private Long id;

	private String userEmail;
	private String imdbId;

	private String movieName;
	private String movieType;
	private Float rating;
	private String duration;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate releaseDate;

	@Column(length = 1000)
	private String description;

	private String posterUrl;

	public WatchList(Long id, String userEmail, String imdbId) {
		super();
		this.id = id;
		this.userEmail = userEmail;
		this.imdbId = imdbId;
	}
	
	

	public WatchList(Long id, String userEmail, String imdbId, String movieName, String movieType, Float rating,
			String duration, LocalDate releaseDate, String description, String posterUrl) {
		super();
		this.id = id;
		this.userEmail = userEmail;
		this.imdbId = imdbId;
		this.movieName = movieName;
		this.movieType = movieType;
		this.rating = rating;
		this.duration = duration;
		this.releaseDate = releaseDate;
		this.description = description;
		this.posterUrl = posterUrl;
	}



	public WatchList(String userEmail, String imdbId) {
		super();
		this.userEmail = userEmail;
		this.imdbId = imdbId;
	}

	public WatchList() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getMovieType() {
		return movieType;
	}

	public void setMovieType(String movieType) {
		this.movieType = movieType;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	// Constructors, getters, setters

}