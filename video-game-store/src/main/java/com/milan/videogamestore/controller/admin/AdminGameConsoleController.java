package com.milan.videogamestore.controller.admin;

import com.milan.videogamestore.model.console.Console;
import com.milan.videogamestore.repository.ConsoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdminGameConsoleController {

    private final ConsoleRepository consoleRepository;

    @GetMapping("/admin/consoles")
    public String list(Model model) {
        model.addAttribute("consoles", consoleRepository.findAll());
        return "admin/consoles";
    }

    @GetMapping("/admin/consoles/{id}")
    public String details(@PathVariable Long id, Model model) {
        var console = consoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Console not found: " + id));
        model.addAttribute("console", console);
        return "admin/console-details";
    }

    @PostMapping("/admin/consoles")
    public String create(@RequestParam("name") String name) {
        var console = new Console();
        console.setName(name.trim());
        consoleRepository.save(console);
        return "redirect:/admin/consoles";
    }

    @PostMapping("/admin/consoles/{id}")
    public String update(@PathVariable Long id, @RequestParam("name") String name) {
        var console = consoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Console not found: " + id));
        console.setName(name.trim());
        consoleRepository.save(console);
        return "redirect:/admin/consoles/" + id;
    }

    @PostMapping("/admin/consoles/{id}/delete")
    public String delete(@PathVariable Long id) {
        consoleRepository.deleteById(id);
        return "redirect:/admin/consoles";
    }
}