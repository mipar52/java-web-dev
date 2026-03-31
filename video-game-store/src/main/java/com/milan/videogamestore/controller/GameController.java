package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.repository.GameRepository;
import com.milan.videogamestore.repository.GenreRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameController {

    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;

    @GetMapping
    public String listGames(@RequestParam(name = "genreId", required = false) List<Long> genreIds, HttpServletRequest request, Model model) {
        var selected = (genreIds == null) ? List.<Long>of() : genreIds.stream().distinct().toList();

        List<Game> games;
        if (selected.isEmpty()) {
            games = gameRepository.findAll();
        } else {
            var ids = gameRepository.findIdsByAllGenres(selected, selected.size());
            games = ids.isEmpty() ? List.of() : gameRepository.findAllByIdInWithGraph(ids);
        }

        String currentPath = request.getRequestURI()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        model.addAttribute("games", games);
        model.addAttribute("allGenres", genreRepository.findAll()); // trebaš injected GameGenreRepository
        model.addAttribute("selectedGenreIds", selected);
        model.addAttribute("currentPath", currentPath);

        return "games/list";
    }


    @GetMapping("/{id}")
    public String gameDetails(@PathVariable Long id, HttpServletRequest request, Model model) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));
        model.addAttribute("game", game);
        model.addAttribute("currentPat", request.getRequestURI());
        return "games/details";
    }

}
