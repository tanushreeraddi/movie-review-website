package com.example.moviebooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <<-- THIS import is important
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.moviebooking.model.Theater;
import com.example.moviebooking.service.TheaterService;

@Controller
@RequestMapping("/movie-ticket-booking/theater")
public class TheaterController {

	@Autowired
	private TheaterService theaterService;

	@GetMapping()
	public String listTheaters(Model model) {
		model.addAttribute("theaters", theaterService.getAllTheaters());
		return "theater"; // your existing theater.html
	}

	// Show add new theater form
	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("theater", new Theater());
		return "add-theater"; // create this Thymeleaf template
	}

	// Process form submit
	@PostMapping("/add")
	public String addTheater(@ModelAttribute Theater theater) {
		theaterService.saveTheater(theater);
		return "redirect:/movie-ticket-booking/theater";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		Theater theater = theaterService.getTheaterById(id);
		model.addAttribute("theater", theater);
		return "edit-theater"; // thymeleaf template for editing
	}

	@PostMapping("/edit/{id}")
	public String updateTheater(@PathVariable("id") Long id, @ModelAttribute("theater") Theater theater) {
		theater.setId(id); // ensure the ID is set
		theaterService.updateTheater(theater);
		return "redirect:/movie-ticket-booking/theater";
	}
	
	 @GetMapping("/delete/{id}")
	    public String deleteTheater(@PathVariable Long id) {
	        theaterService.deleteTheaterById(id);
	        return "redirect:/movie-ticket-booking/theater";
	    }

}
