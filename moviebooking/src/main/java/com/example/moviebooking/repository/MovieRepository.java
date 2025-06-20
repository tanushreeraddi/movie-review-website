package com.example.moviebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.moviebooking.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}