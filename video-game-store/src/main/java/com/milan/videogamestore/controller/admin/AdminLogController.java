package com.milan.videogamestore.controller.admin;

import com.milan.videogamestore.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Controller
@RequiredArgsConstructor
public class AdminLogController {
    private final LogEntryRepository repo;

    @GetMapping("/admin/audit/logins")
    public String list(@RequestParam(required = false) String username,
                       @RequestParam(required = false) LocalDate from,
                       @RequestParam(required = false) LocalDate to,
                       @RequestParam(required = false) Boolean success,
                       Model model) {

        OffsetDateTime fromTs = from == null ? null : from.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime toTs = to == null ? null : to.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC).minusNanos(1);

        model.addAttribute("rows", repo.adminSearch(username, fromTs, toTs, success));
        model.addAttribute("username", username);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("success", success);

        return "admin/login-audit";
    }
}
