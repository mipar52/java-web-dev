package com.milan.videogamestore.config.security.logEntries;

import com.milan.videogamestore.model.logs.LogEntry;
import com.milan.videogamestore.repository.LogEntryRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class LogAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final LogEntryRepository repo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        var a = new LogEntry();
        a.setUsername(authentication.getName());
        a.setLoggedInAt(OffsetDateTime.now());
        a.setIpAddress(RequestIpUtils.getClientIp(request));
        a.setUserAgent(request.getHeader("User-Agent"));
        a.setSuccess(true);
        repo.save(a);

        // default flow (redirect na success url)
        response.sendRedirect("/games");
    }
}
