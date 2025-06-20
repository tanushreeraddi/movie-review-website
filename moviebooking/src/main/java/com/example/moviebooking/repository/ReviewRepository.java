package com.example.moviebooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.moviebooking.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	Optional<Review> findByImdbIdAndUserEmail(String imdbId, String userEmail);

	List<Review> findByUserEmail(String userEmail);

	Object findByUserEmailAndImdbId(String email, String imdbId);

}
