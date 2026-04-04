package com.milan.videogamestore.config.security.logEntries;

import com.milan.videogamestore.model.logs.LogEntry;
import com.milan.videogamestore.repository.LogEntryRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class LogAuthSuccessListener {

    private final LogEntryRepository repo;
    private final HttpServletRequest request;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent successEvent) {
        var a = new LogEntry();
        a.setUsername(successEvent.getAuthentication().getName());
        a.setLoggedInAt(OffsetDateTime.now());
        a.setIpAddress(RequestIpUtils.getClientIp(request));
        a.setUserAgent(request.getHeader("User-Agent"));
        a.setSuccess(true);
        repo.save(a);
    }
}
