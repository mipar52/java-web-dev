package com.milan.videogamestore.controller.admin;

import com.milan.videogamestore.model.game.GameType;
import com.milan.videogamestore.repository.GameTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdminGameTypeController {

    private final GameTypeRepository gameTypeRepository;

    @GetMapping("/admin/game-types")
    public String list(Model model) {
        model.addAttribute("types", gameTypeRepository.findAll());
        return "admin/game-types";
    }

    @GetMapping("/admin/game-types/{id}")
    public String details(@PathVariable Long id, Model model) {
        var type = gameTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GameType not found: " + id));
        model.addAttribute("type", type);
        return "admin/game-type-details";
    }

    @PostMapping("/admin/game-types")
    public String create(@RequestParam("name") String name) {
        var type = new GameType();
        type.setName(name.trim());
        gameTypeRepository.save(type);
        return "redirect:/admin/game-types";
    }

    @PostMapping("/admin/game-types/{id}")
    public String update(@PathVariable Long id, @RequestParam("name") String name) {
        var type = gameTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GameType not found: " + id));
        type.setName(name.trim());
        gameTypeRepository.save(type);
        return "redirect:/admin/game-types/" + id;
    }

    @PostMapping("/admin/game-types/{id}/delete")
    public String delete(@PathVariable Long id) {
        gameTypeRepository.deleteById(id);
        return "redirect:/admin/game-types";
    }
}