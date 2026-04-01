package com.milan.videogamestore.controller.admin;

import com.milan.videogamestore.model.game.Genre;
import com.milan.videogamestore.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdminGameGenreController {

    private final GenreRepository genreRepository;

    @GetMapping("/admin/genres")
    public String list(Model model) {
        model.addAttribute("genres", genreRepository.findAll());
        return "admin/genres";
    }

    @GetMapping("/admin/genres/{id}")
    public String details(@PathVariable Long id, Model model) {
        var genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found: " + id));
        model.addAttribute("genre", genre);
        return "admin/genre-details";
    }

    @PostMapping("/admin/genres")
    public String create(@RequestParam("name") String name) {
        var genre = new Genre();
        genre.setName(name.trim());
        genreRepository.save(genre);
        return "redirect:/admin/genres";
    }

    @PostMapping("/admin/genres/{id}")
    public String update(@PathVariable Long id, @RequestParam("name") String name) {
        var genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found: " + id));
        genre.setName(name.trim());
        genreRepository.save(genre);
        return "redirect:/admin/genres/" + id;
    }

    @PostMapping("/admin/genres/{id}/delete")
    public String delete(@PathVariable Long id) {
        genreRepository.deleteById(id);
        return "redirect:/admin/genres";
    }
}