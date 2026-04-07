package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.dto.RegisterForm;
import com.milan.videogamestore.model.users.AppUser;
import com.milan.videogamestore.repository.AppUserRepository;
import com.milan.videogamestore.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AppUserRepository appUserRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String REGISTER_URL = "auth/register";

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return REGISTER_URL;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("form") RegisterForm form, Model model) {
        if (appUserRepository.existsByUsername(form.getUsername())) {
            model.addAttribute("error", "Username already taken!");
            return REGISTER_URL;
        }
        if (appUserRepository.existsByEmail(form.getEmail())) {
            model.addAttribute("error", "Email is already registered!");
            return REGISTER_URL;
        }
        var role = userRoleRepository.findByName("USER").orElseThrow(() -> new IllegalArgumentException("Missing role USER!!"));

        var user = new AppUser();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        user.setRole(role);

        appUserRepository.save(user);

        return "redirect:/login?registered";

    }
}
