package com.milan.videogamestore.config.security.logentries;

import com.milan.videogamestore.model.logentry.LogEntry;
import com.milan.videogamestore.repository.LogEntryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

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
