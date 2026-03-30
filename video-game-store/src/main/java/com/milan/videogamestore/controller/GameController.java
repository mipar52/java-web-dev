package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.repository.GameRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameController {

    private final GameRepository gameRepository;

    @GetMapping
    public String listGames(Model model) {
        model.addAttribute("games", gameRepository.findAll());
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
