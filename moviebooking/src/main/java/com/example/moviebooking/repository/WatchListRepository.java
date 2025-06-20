package com.example.moviebooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.moviebooking.model.WatchList;

public interface WatchListRepository extends JpaRepository<WatchList, Long>{

	@Query("SELECT w.imdbId FROM WatchList w WHERE w.userEmail = :email")
	List<String> findImdbIdsByUserEmail(@Param("email") String email);

	List<WatchList> findByUserEmail(String email);

	void deleteByUserEmailAndImdbId(String userEmail, String imdbId);

	boolean existsByUserEmailAndImdbId(String email, String imdbId);	

}
