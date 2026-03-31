package com.milan.videogamestore.controller;

import com.milan.videogamestore.repository.GenreRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping
    public String listGenres(HttpServletRequest request, Model model) {
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("currentPath", request.getRequestURI());
        return "genres/list";
    }
}
