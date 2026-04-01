package com.milan.videogamestore.controller.admin;

import com.milan.videogamestore.model.console.Console;
import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.game.GameCover;
import com.milan.videogamestore.model.game.GameType;
import com.milan.videogamestore.model.game.Genre;
import com.milan.videogamestore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AdminGameController {

    private final GameRepository gameRepository;
    private final GameTypeRepository gameTypeRepository;
    private final GenreRepository genreRepository;
    private final ConsoleRepository consoleRepository;
    private final GameCoverRepository gameCoverRepository;

    @GetMapping("/admin/games")
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("games", gameRepository.adminSearch(q));
        model.addAttribute("q", q);
        return "admin/games";
    }

    @GetMapping("/admin/games/new")
    public String createForm(Model model) {
        model.addAttribute("game", new Game());
        populateLookups(model);
        return "admin/game-details";
    }

    @GetMapping("/admin/games/{id}")
    public String details(@PathVariable Long id, Model model) {
        var game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));

        model.addAttribute("game", game);
        model.addAttribute("releaseDateLocal",
                game.getReleaseDate() == null ? null : game.getReleaseDate().toLocalDate());

        populateLookups(model);
        return "admin/game-details";
    }

    @PostMapping("/admin/games")
    public String create(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate releaseDate,
            @RequestParam(required = false) String gameUrl,
            @RequestParam(required = false) Double metacriticScore,
            @RequestParam(defaultValue = "false") boolean wonGameOfTheYearAward,
            @RequestParam(required = false) String imageUrl,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) Long gameTypeId,
            @RequestParam(required = false, name = "genreIds") Set<Long> genreIds,
            @RequestParam(required = false, name = "consoleIds") Set<Long> consoleIds
    ) {
        var game = new Game();
        applyGameFields(game, name, description, releaseDate, gameUrl, metacriticScore, wonGameOfTheYearAward,
                imageUrl, price, gameTypeId, genreIds, consoleIds);

        gameRepository.save(game);
        return "redirect:/admin/games/" + game.getId();
    }

    @PostMapping("/admin/games/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate releaseDate,
            @RequestParam(required = false) String gameUrl,
            @RequestParam(required = false) Double metacriticScore,
            @RequestParam(defaultValue = "false") boolean wonGameOfTheYearAward,
            @RequestParam(required = false) String imageUrl,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) Long gameTypeId,
            @RequestParam(required = false, name = "genreIds") Set<Long> genreIds,
            @RequestParam(required = false, name = "consoleIds") Set<Long> consoleIds
    ) {
        var game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));

        applyGameFields(game, name, description, releaseDate, gameUrl, metacriticScore, wonGameOfTheYearAward,
                imageUrl, price, gameTypeId, genreIds, consoleIds);

        gameRepository.save(game);
        return "redirect:/admin/games/" + id;
    }

    @PostMapping("/admin/games/{id}/delete")
    public String delete(@PathVariable Long id) {
        // NOTE: ovo može failati ako postoje OrderItem referencije.
        gameRepository.deleteById(id);
        return "redirect:/admin/games";
    }

    private void populateLookups(Model model) {
        model.addAttribute("types", gameTypeRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("consoles", consoleRepository.findAll());
    }

    private void applyGameFields(
            Game game,
            String name,
            String description,
            LocalDate releaseDate,
            String gameUrl,
            Double metacriticScore,
            boolean wonGameOfTheYearAward,
            String imageUrl,
            BigDecimal price,
            Long gameTypeId,
            Set<Long> genreIds,
            Set<Long> consoleIds
    ) {
        game.setName(name == null ? "" : name.trim());
        game.setDescription(description);
        game.setGameUrl(gameUrl);
        game.setMetacriticScore(metacriticScore);
        game.setWonGameOfTheYearAward(wonGameOfTheYearAward);
        game.setImageUrl(imageUrl);
        game.setPrice(price);

        game.setReleaseDate(releaseDate == null ? null : releaseDate.atStartOfDay().atOffset(ZoneOffset.UTC));

        GameType type = null;
        if (gameTypeId != null) {
            type = gameTypeRepository.findById(gameTypeId).orElse(null);
        }
        game.setGameType(type);

        Set<Genre> newGenres = new HashSet<>();
        if (genreIds != null && !genreIds.isEmpty()) {
            newGenres.addAll(genreRepository.findAllById(genreIds));
        }
        game.setGameGenres(newGenres);

        Set<Console> newConsoles = new HashSet<>();
        if (consoleIds != null && !consoleIds.isEmpty()) {
            newConsoles.addAll(consoleRepository.findAllById(consoleIds));
        }
        game.setConsoles(newConsoles);
    }

    @PostMapping("/admin/games/{id}/cover")
    public String uploadCoverToDb(@PathVariable Long id,
                                  @RequestParam("cover") MultipartFile cover) throws Exception {

        var game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));

        if (cover == null || cover.isEmpty()) {
            System.out.println("got nothing");
            return "redirect:/admin/games/" + id;
        }

        String contentType = cover.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image uploads are allowed");
        }

        byte[] bytes = cover.getBytes();
        if (bytes.length > 3_000_000) { // 3MB limit
            throw new IllegalArgumentException("Cover image too large (max 3MB)");
        }

        GameCover gameCover = gameCoverRepository.findById(id).orElseGet(GameCover::new);
        gameCover.setGame(game);
        gameCover.setContentType(contentType);
        gameCover.setData(bytes);

        gameCoverRepository.save(gameCover);
        System.out.println("Uploaded!");
        return "redirect:/admin/games/" + id;
    }
}