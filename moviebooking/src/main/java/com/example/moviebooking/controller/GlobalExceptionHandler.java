package com.example.moviebooking.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RedirectView handleMissingParams(MissingServletRequestParameterException ex, RedirectAttributes attributes) {
        // Optionally add flash message or logging
        attributes.addFlashAttribute("message", "Please login to submit a review.");
        return new RedirectView("/login");
    }
}