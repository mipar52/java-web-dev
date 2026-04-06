package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.dto.ReviewForm;
import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.repository.GameRepository;
import com.milan.videogamestore.repository.GameReviewRepository;
import com.milan.videogamestore.repository.GenreRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameController {

    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final GameReviewRepository reviewRepository;

    @GetMapping("/")
    public String home() {
        return "redirect:/games";
    }

    @GetMapping
    public String listGames(
            @RequestParam(name = "genreId", required = false) List<Long> genreIds,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "3") int size,
            @RequestHeader(value = "HX-Request", required = false) String hxRequest,
            HttpServletRequest request,
            Model model
    ) {
        var selected = (genreIds == null) ? List.<Long>of() : genreIds.stream().distinct().toList();

        Page<Game> gamesPage;

        if (selected.isEmpty()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            gamesPage = gameRepository.findAll(pageable);
        } else {
            // manual paging: get ids matching ALL selected genres, then page ids
            var ids = gameRepository.findIdsByAllGenres(selected, selected.size());

            int total = ids.size();
            int fromIndex = Math.min(page * size, total);
            int toIndex = Math.min(fromIndex + size, total);
            var pageIds = ids.subList(fromIndex, toIndex);

            var content = pageIds.isEmpty() ? List.<Game>of() : gameRepository.findAllByIdInWithGraph(pageIds);

            Pageable pageable = PageRequest.of(page, size);
            gamesPage = new PageImpl<>(content, pageable, total);
        }

        String currentPath = request.getRequestURI()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        model.addAttribute("gamesPage", gamesPage);
        model.addAttribute("allGenres", genreRepository.findAll());
        model.addAttribute("selectedGenreIds", selected);
        model.addAttribute("currentPath", currentPath);

        if (hxRequest != null) {
            return "games/_games-root :: gamesRoot";
        }

        return "games/list";
    }

    @GetMapping("/{id}")
    public String gameDetails(@PathVariable Long id, HttpServletRequest request, Model model) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));
        var reviews = reviewRepository.findAllByGame_IdOrderByCreatedAtDesc(id);
        model.addAttribute("reviews", reviews);

        var avg = reviewRepository.findAverageRatingByGameId(id).orElse(null);
        var ratingsCount = reviewRepository.countRatingsByGameId(id);

        model.addAttribute("averageRating", avg);
        model.addAttribute("ratingsCount", ratingsCount);

        model.addAttribute("game", game);
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPat", request.getRequestURI());
        model.addAttribute("reviewForm", new ReviewForm());
        return "games/details";
    }
}
