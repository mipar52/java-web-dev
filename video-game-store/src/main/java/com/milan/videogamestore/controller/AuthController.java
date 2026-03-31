package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.dto.RegisterForm;
import com.milan.videogamestore.model.users.AppUser;
import com.milan.videogamestore.repository.AppUserRepository;
import com.milan.videogamestore.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AppUserRepository appUserRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("form") RegisterForm form, Model model) {
        if (appUserRepository.existsByUsername(form.getUsername())) {
            model.addAttribute("error", "Username already taken!");
            return "auth/register";
        }
        if (appUserRepository.existsByEmail(form.getEmail())) {
            model.addAttribute("error", "Email is already registered!");
            return "auth/register";
        }
        var role = userRoleRepository.findByName("USER").orElseThrow(() -> new IllegalArgumentException("Missing role USER!!"));

        var user = new AppUser();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        //user.setMobilePhone(form.getMobilePhone());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        user.setRole(role);

        appUserRepository.save(user);

        return "redirect:/login?registered";

    }

}
