package com.milan.videogamestore.config.security;

import com.milan.videogamestore.config.security.logEntries.LogAuthFailureHandler;
import com.milan.videogamestore.config.security.logEntries.LogAuthSuccessHandler;
import com.milan.videogamestore.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LogEntryRepository logEntryRepository;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers("/media/**").permitAll()
                        .requestMatchers("/games/**", "/genres/**", "/cart/**").permitAll()
                        .requestMatchers("/checkout/**", "/orders/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .defaultSuccessUrl("/games", true)
                                .successHandler(new LogAuthSuccessHandler(logEntryRepository))
                                .failureHandler(new LogAuthFailureHandler(logEntryRepository))
                                .permitAll()
                                .permitAll())
                .logout(Customizer.withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}