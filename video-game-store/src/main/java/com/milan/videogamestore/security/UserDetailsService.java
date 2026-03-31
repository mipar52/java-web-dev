package com.milan.videogamestore.security;

import com.milan.videogamestore.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsernameWithRole(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        String roleName = user.getRole().getName();
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

        return User
                .withUsername(user.getUsername())
                .password((user.getPasswordHash()))
                .authorities(authorities)
                .build();
    }
}
