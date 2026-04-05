package com.milan.videogamestore.config.security.logEntries;

import com.milan.videogamestore.model.logEntry.LogEntry;
import com.milan.videogamestore.repository.LogEntryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class LogAuthFailureListener {

    private final LogEntryRepository repo;
    private final HttpServletRequest request;

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        var a = new LogEntry();
        a.setUsername(request.getParameter("username")); // iz login forme
        a.setLoggedInAt(OffsetDateTime.now());
        a.setIpAddress(RequestIpUtils.getClientIp(request));
        a.setUserAgent(request.getHeader("User-Agent"));
        a.setSuccess(false);
        a.setFailureReason(event.getClass().getSimpleName());
        repo.save(a);
    }
}
