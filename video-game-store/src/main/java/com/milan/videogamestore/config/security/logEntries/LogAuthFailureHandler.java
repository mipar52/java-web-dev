package com.milan.videogamestore.config.security.logEntries;

import com.milan.videogamestore.model.logs.LogEntry;
import com.milan.videogamestore.repository.LogEntryRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class LogAuthFailureHandler implements AuthenticationFailureHandler {

    private final LogEntryRepository repo;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        var a = new LogEntry();
        a.setUsername(request.getParameter("username")); // iz login forme
        a.setLoggedInAt(OffsetDateTime.now());
        a.setIpAddress(RequestIpUtils.getClientIp(request));
        a.setUserAgent(request.getHeader("User-Agent"));
        a.setSuccess(false);
        a.setFailureReason(exception.getClass().getSimpleName());
        repo.save(a);

        response.sendRedirect("/login?error");
    }
}
