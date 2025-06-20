package com.example.moviebooking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.moviebooking.model.Theater;
import com.example.moviebooking.repository.TheaterRepository;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Theater saveTheater(Theater theater) {
        return theaterRepository.save(theater);
    }
    
    public Theater getTheaterById(Long id) {
        return theaterRepository.findById(id).orElseThrow(() -> new RuntimeException("Theater not found"));
    }

    public void updateTheater(Theater theater) {
        theaterRepository.save(theater);
    }
    

    public void deleteTheaterById(Long id) {
        theaterRepository.deleteById(id);
    }
}